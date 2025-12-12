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
    
    // User Display Name
    private val _driveUserName = MutableStateFlow<String?>(null)
    val driveUserName: StateFlow<String?> = _driveUserName.asStateFlow()
    
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
            _driveUserName.value = account.displayName
            loadAvailableBackups()
        } else {
             _driveUserName.value = null
        }
    }
    
    /**
     * Get sign-in intent for Google account picker
     */
    fun getSignInIntent() = driveManager.getSignInIntent()
    
    /**
     * Handle sign-in result from activity
     */
    fun handleSignInResult(
        data: android.content.Intent?,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                
                val result = driveManager.initializeDriveService(account)
                if (result.isSuccess) {
                    _uiState.value = BackupRestoreUiState.Success("Signed in successfully")
                    _driveUserName.value = account.displayName ?: "User"
                    loadAvailableBackups()
                    onSuccess()
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Failed to initialize Drive"
                    _uiState.value = BackupRestoreUiState.Error(error)
                    onFailure(error)
                }
            } catch (e: ApiException) {
                val error = "Sign-in failed: ${e.message}"
                _uiState.value = BackupRestoreUiState.Error(error)
                onFailure(error)
            }
        }
    }
    
    /**
     * Check for cloud backups and automatically restore/merge with local data
     */
    fun checkAndAutoRestore(onComplete: (success: Boolean, message: String) -> Unit) {
        viewModelScope.launch {
            try {
                android.util.Log.d("BackupRestore", "Starting auto-restore check...")
                _uiState.value = BackupRestoreUiState.Loading("Checking for cloud backups...")
                
                // Load available backups from Drive
                val backupsResult = driveManager.listBackups()
                if (backupsResult.isFailure || backupsResult.getOrNull()?.isEmpty() == true) {
                    android.util.Log.d("BackupRestore", "No cloud backups found, skipping auto-restore")
                    _uiState.value = BackupRestoreUiState.Idle
                    onComplete(false, "No cloud backups found")
                    return@launch
                }
                
                val backups = backupsResult.getOrNull() ?: emptyList()
                val latestBackup = backups.firstOrNull()
                
                if (latestBackup == null) {
                    android.util.Log.d("BackupRestore", "No backups available")
                    _uiState.value = BackupRestoreUiState.Idle
                    onComplete(false, "No backups available")
                    return@launch
                }
                
                android.util.Log.d("BackupRestore", "Found ${backups.size} backups, restoring latest: ${latestBackup.name}")
                _uiState.value = BackupRestoreUiState.Loading("Restoring your data from cloud...")
                
                // Download backup
                val downloadResult = driveManager.downloadBackup(latestBackup.id)
                if (downloadResult.isFailure) {
                    val error = "Failed to download backup: ${downloadResult.exceptionOrNull()?.message}"
                    android.util.Log.e("BackupRestore", error)
                    _uiState.value = BackupRestoreUiState.Error(error)
                    onComplete(false, error)
                    return@launch
                }
                
                val jsonContent = downloadResult.getOrNull()!!
                
                // Deserialize backup
                val deserializeResult = backupSerializer.deserialize(jsonContent)
                if (deserializeResult.isFailure) {
                    val error = "Invalid backup file: ${deserializeResult.exceptionOrNull()?.message}"
                    android.util.Log.e("BackupRestore", error)
                    _uiState.value = BackupRestoreUiState.Error(error)
                    onComplete(false, error)
                    return@launch
                }
                
                val backupData = deserializeResult.getOrNull()!!
                
                // Import with smart merge
                android.util.Log.d("BackupRestore", "Merging cloud data with local data...")
                val importResult = repository.importBackupData(backupData)
                
                if (importResult.isSuccess) {
                    android.util.Log.d("BackupRestore", "Auto-restore completed successfully!")
                    _uiState.value = BackupRestoreUiState.Success("Data restored from cloud")
                    onComplete(true, "Data restored successfully")
                } else {
                    val error = "Restore failed: ${importResult.exceptionOrNull()?.message}"
                    android.util.Log.e("BackupRestore", error)
                    _uiState.value = BackupRestoreUiState.Error(error)
                    onComplete(false, error)
                }
            } catch (e: Exception) {
                val error = "Auto-restore failed: ${e.message}"
                android.util.Log.e("BackupRestore", error, e)
                _uiState.value = BackupRestoreUiState.Error(error)
                onComplete(false, error)
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
                _driveUserName.value = null
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
                android.util.Log.d("BackupRestore", "createBackup called for branch $branchId ($branchName)")
                _uiState.value = BackupRestoreUiState.Loading("Creating backup...")
                
                android.util.Log.d("BackupRestore", "Exporting backup data...")
                // Export data from repository
                val backupData = repository.exportBackupData(branchId)
                
                android.util.Log.d("BackupRestore", "Serializing backup data...")
                // Serialize to JSON
                val jsonContent = backupSerializer.serialize(backupData)
                
                // Create filename with timestamp
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(Date())
                val fileName = "gate_tracker_${branchName}_$timestamp.json"
                
                android.util.Log.d("BackupRestore", "Uploading to Drive: $fileName")
                _uiState.value = BackupRestoreUiState.Loading("Uploading to Google Drive...")
                
                // Upload to Drive
                val result = driveManager.uploadBackup(fileName, jsonContent)
                
                if (result.isSuccess) {
                    android.util.Log.d("BackupRestore", "Backup successful!")
                    _lastBackupTime.value = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(Date())
                    _uiState.value = BackupRestoreUiState.Success("Backup created successfully")
                    loadAvailableBackups()
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Failed to upload backup"
                    android.util.Log.e("BackupRestore", "Backup failed: $error")
                    _uiState.value = BackupRestoreUiState.Error(error)
                }
            } catch (e: Exception) {
                val error = "Backup failed: ${e.message}"
                android.util.Log.e("BackupRestore", error, e)
                _uiState.value = BackupRestoreUiState.Error(error)
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
     * Delete all backups from Google Drive
     */
    fun deleteAllBackups(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreUiState.Loading("Deleting all cloud backups...")
            
            val listResult = driveManager.listBackups()
            if (listResult.isSuccess) {
                val backups = listResult.getOrNull() ?: emptyList()
                var successCount = 0
                
                backups.forEach { backup ->
                    val deleteResult = driveManager.deleteBackup(backup.id)
                    if (deleteResult.isSuccess) successCount++
                }
                
                if (successCount == backups.size) {
                    _uiState.value = BackupRestoreUiState.Success("All cloud backups deleted")
                } else {
                    _uiState.value = BackupRestoreUiState.Error("Deleted $successCount of ${backups.size} backups")
                }
            } else {
                _uiState.value = BackupRestoreUiState.Error("Failed to list backups for deletion")
            }
            
            _backupList.value = emptyList()
            _lastBackupTime.value = null
            onComplete()
        }
    }

    /**
     * Auto-backup silently in background (no UI updates)
     * Only backs up if user is signed in to Google Drive
     */
    fun autoBackupSilently(branchId: Int, branchName: String) {
        // Only backup if user is signed in
        if (!driveManager.isSignedIn()) {
            android.util.Log.d("BackupRestore", "Auto-backup skipped: user not signed in")
            return
        }
        
        viewModelScope.launch {
            try {
                android.util.Log.d("BackupRestore", "Auto-backup started silently for branch $branchId")
                
                // Export data from repository
                val backupData = repository.exportBackupData(branchId)
                
                // Serialize to JSON
                val jsonContent = backupSerializer.serialize(backupData)
                
                // Create filename with timestamp
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(Date())
                val fileName = "gate_tracker_${branchName}_$timestamp.json"
                
                // Upload to Drive (silent - no UI feedback)
                val result = driveManager.uploadBackup(fileName, jsonContent)
                
                if (result.isSuccess) {
                    android.util.Log.d("BackupRestore", "Auto-backup completed successfully")
                    // Update last backup time silently
                    _lastBackupTime.value = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(Date())
                    // Refresh backup list in background
                    loadAvailableBackups()
                } else {
                    android.util.Log.e("BackupRestore", "Auto-backup failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                android.util.Log.e("BackupRestore", "Auto-backup error: ${e.message}", e)
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
