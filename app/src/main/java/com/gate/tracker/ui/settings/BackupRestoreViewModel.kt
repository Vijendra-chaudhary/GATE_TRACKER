package com.gate.tracker.ui.settings

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.backup.BackupSerializer
import com.gate.tracker.data.drive.DriveBackupFile
import com.gate.tracker.data.drive.DriveManager
import com.gate.tracker.data.repository.GateRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for backup and restore operations
 */
class BackupRestoreViewModel(
    private val repository: GateRepository,
    context: Context
) : ViewModel() {
    
    private val driveManager = DriveManager(context.applicationContext)
    private val backupSerializer = BackupSerializer()
    
    private val _uiState = MutableStateFlow<BackupRestoreUiState>(BackupRestoreUiState.Idle)
    val uiState: StateFlow<BackupRestoreUiState> = _uiState.asStateFlow()
    
    private val _backupList = MutableStateFlow<List<DriveBackupFile>>(emptyList())
    val backupList: StateFlow<List<DriveBackupFile>> = _backupList.asStateFlow()
    
    private val _lastBackupTime = MutableStateFlow<String?>(null)
    val lastBackupTime: StateFlow<String?> = _lastBackupTime.asStateFlow()
    
    init {
        checkSignInStatus()
    }
    
    /**
     * Check if user is signed in to Google
     */
    private fun checkSignInStatus() {
        val account = driveManager.getSignedInAccount()
        if (account != null) {
            driveManager.initializeDriveService(account)
            loadAvailableBackups()
        }
    }
    
    /**
     * Get sign-in intent for Google account picker
     */
    fun getSignInIntent() = driveManager.getSignInIntent()
    
    /**
     * Handle sign-in result from activity
     */
    fun handleSignInResult(data: android.content.Intent?) {
        viewModelScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                
                val result = driveManager.initializeDriveService(account)
                if (result.isSuccess) {
                    _uiState.value = BackupRestoreUiState.Success("Signed in successfully")
                    loadAvailableBackups()
                } else {
                    _uiState.value = BackupRestoreUiState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to initialize Drive"
                    )
                }
            } catch (e: ApiException) {
                _uiState.value = BackupRestoreUiState.Error("Sign-in failed: ${e.message}")
            }
        }
    }
    
    /**
     * Check if user is signed in
     */
    fun isSignedIn(): Boolean = driveManager.isSignedIn()
    
    /**
     * Sign out from Google account
     */
    fun signOut() {
        viewModelScope.launch {
            val result = driveManager.signOut()
            if (result.isSuccess) {
                _backupList.value = emptyList()
                _lastBackupTime.value = null
                _uiState.value = BackupRestoreUiState.Success("Signed out successfully")
            }
        }
    }
    
    /**
     * Create backup and upload to Google Drive
     */
    fun createBackup(branchId: Int, branchName: String) {
        viewModelScope.launch {
            try {
                _uiState.value = BackupRestoreUiState.Loading("Creating backup...")
                
                // Export data from repository
                val backupData = repository.exportBackupData(branchId)
                
                // Serialize to JSON
                val jsonContent = backupSerializer.serialize(backupData)
                
                // Create filename with timestamp
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(Date())
                val fileName = "gate_tracker_${branchName}_$timestamp.json"
                
                _uiState.value = BackupRestoreUiState.Loading("Uploading to Google Drive...")
                
                // Upload to Drive
                val result = driveManager.uploadBackup(fileName, jsonContent)
                
                if (result.isSuccess) {
                    _lastBackupTime.value = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(Date())
                    _uiState.value = BackupRestoreUiState.Success("Backup created successfully")
                    loadAvailableBackups()
                } else {
                    _uiState.value = BackupRestoreUiState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to upload backup"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = BackupRestoreUiState.Error("Backup failed: ${e.message}")
            }
        }
    }
    
    /**
     * Load list of available backups from Google Drive
     */
    fun loadAvailableBackups() {
        viewModelScope.launch {
            val result = driveManager.listBackups()
            if (result.isSuccess) {
                _backupList.value = result.getOrNull() ?: emptyList()
                
                // Update last backup time if available
                _backupList.value.firstOrNull()?.let { latestBackup ->
                    _lastBackupTime.value = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(Date(latestBackup.createdTime))
                }
            }
        }
    }
    
    /**
     * Restore backup from Google Drive
     */
    fun restoreBackup(backupFile: DriveBackupFile) {
        viewModelScope.launch {
            try {
                _uiState.value = BackupRestoreUiState.Loading("Downloading backup...")
                
                // Download backup from Drive
                val downloadResult = driveManager.downloadBackup(backupFile.id)
                if (downloadResult.isFailure) {
                    _uiState.value = BackupRestoreUiState.Error(
                        downloadResult.exceptionOrNull()?.message ?: "Failed to download backup"
                    )
                    return@launch
                }
                
                val jsonContent = downloadResult.getOrNull()!!
                
                _uiState.value = BackupRestoreUiState.Loading("Restoring data...")
                
                // Deserialize backup
                val deserializeResult = backupSerializer.deserialize(jsonContent)
                if (deserializeResult.isFailure) {
                    _uiState.value = BackupRestoreUiState.Error(
                        deserializeResult.exceptionOrNull()?.message ?: "Invalid backup file"
                    )
                    return@launch
                }
                
                val backupData = deserializeResult.getOrNull()!!
                
                // Import data to repository
                val importResult = repository.importBackupData(backupData)
                if (importResult.isSuccess) {
                    _uiState.value = BackupRestoreUiState.RestoreComplete(
                        "Restore completed successfully. Please restart the app."
                    )
                } else {
                    _uiState.value = BackupRestoreUiState.Error(
                        importResult.exceptionOrNull()?.message ?: "Failed to restore backup"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = BackupRestoreUiState.Error("Restore failed: ${e.message}")
            }
        }
    }
    
    /**
     * Delete backup from Google Drive
     */
    fun deleteBackup(backupFile: DriveBackupFile) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreUiState.Loading("Deleting backup...")
            
            val result = driveManager.deleteBackup(backupFile.id)
            if (result.isSuccess) {
                _uiState.value = BackupRestoreUiState.Success("Backup deleted successfully")
                loadAvailableBackups()
            } else {
                _uiState.value = BackupRestoreUiState.Error(
                    result.exceptionOrNull()?.message ?: "Failed to delete backup"
                )
            }
        }
    }
    
    /**
     * Clear UI state
     */
    fun clearState() {
        _uiState.value = BackupRestoreUiState.Idle
    }
}

/**
 * UI state for backup/restore operations
 */
sealed class BackupRestoreUiState {
    object Idle : BackupRestoreUiState()
    data class Loading(val message: String) : BackupRestoreUiState()
    data class Success(val message: String) : BackupRestoreUiState()
    data class Error(val message: String) : BackupRestoreUiState()
    data class RestoreComplete(val message: String) : BackupRestoreUiState()
}
