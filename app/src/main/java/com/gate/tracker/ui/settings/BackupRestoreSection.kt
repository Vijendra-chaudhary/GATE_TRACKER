package com.gate.tracker.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.gate.tracker.data.drive.DriveBackupFile

/**
 * Backup & Restore section to be added to Settings Screen
 */
@Composable
fun BackupRestoreSection(
    backupRestoreViewModel: BackupRestoreViewModel,
    branchId: Int,
    branchName: String,
    onLaunchSignIn: () -> Unit
) {
    val backupUiState by backupRestoreViewModel.uiState.collectAsState()
    val backupList by backupRestoreViewModel.backupList.collectAsState()
    val lastBackupTime by backupRestoreViewModel.lastBackupTime.collectAsState()
    
    var showBackupDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var showRestoreWarning by remember { mutableStateOf(false) }
    var selectedBackupFile by remember { mutableStateOf<DriveBackupFile?>(null) }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Backup & Restore",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp)
        )
        
        // Backup to Drive Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (backupRestoreViewModel.isSignedIn()) {
                        showBackupDialog = true
                    } else {
                        onLaunchSignIn()
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (backupRestoreViewModel.isSignedIn()) "Create Backup" else "Sign in to Google Drive",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lastBackupTime?.let { "Last backup: $it" } 
                            ?: if (backupRestoreViewModel.isSignedIn()) "Backup your progress to Google Drive" 
                            else "Sign in to enable backup",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Restore from Drive Card (only if signed in)
        if (backupRestoreViewModel.isSignedIn()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        backupRestoreViewModel.loadAvailableBackups()
                        showRestoreDialog = true
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Restore from Backup",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Restore your progress from Google Drive",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
    
    // Dialogs
    if (showBackupDialog) {
        BackupConfirmationDialog(
            branchName = branchName,
            onConfirm = {
                showBackupDialog = false
                backupRestoreViewModel.createBackup(branchId, branchName)
            },
            onDismiss = { showBackupDialog = false }
        )
    }
    
    if (showRestoreDialog) {
        RestoreSelectionDialog(
            backupList = backupList,
            currentBranchId = branchId,
            onSelectBackup = { backup ->
                selectedBackupFile = backup
                showRestoreDialog = false
                showRestoreWarning = true
            },
            onDismiss = { showRestoreDialog = false }
        )
    }
    
    if (showRestoreWarning && selectedBackupFile != null) {
        RestoreWarningDialog(
            backupFile = selectedBackupFile!!,
            onConfirm = {
                showRestoreWarning = false
                backupRestoreViewModel.restoreBackup(selectedBackupFile!!)
            },
            onDismiss = {
                showRestoreWarning = false
                selectedBackupFile = null
            }
        )
    }
    
    // Handle state changes
    LaunchedEffect(backupUiState) {
        when (backupUiState) {
            is BackupRestoreUiState.RestoreComplete -> {
                // App needs to restart to reload data
                backupRestoreViewModel.clearState()
            }
            else -> {}
        }
    }
}
