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
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import com.gate.tracker.data.drive.DriveBackupFile

/**
 * Backup & Restore section with minimal design
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
    val driveUserName by backupRestoreViewModel.driveUserName.collectAsState()
    
    var showRestoreWarning by remember { mutableStateOf(false) }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Backup and Restore Card (if signed in)
        if (backupRestoreViewModel.isSignedIn()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
                    Text(
                        text = "Backup & Restore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 0.dp)
                    )
                    
                    // User greeting
                    if (!driveUserName.isNullOrEmpty()) {
                        Text(
                            text = "Signed in as $driveUserName",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Backup and Restore Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Backup Button
                        Button(
                            onClick = {
                                android.util.Log.d("GATE_TRACKER", "Backup clicked for branch $branchId ($branchName)")
                                backupRestoreViewModel.createBackup(branchId, branchName)
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Backup",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        
                        // Restore Button
                        Button(
                            onClick = {
                                backupRestoreViewModel.loadAvailableBackups()
                                showRestoreWarning = true
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Restore",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        } else {
            // Sign in card (full width when not signed in)
            MinimalSettingCard(
                icon = Icons.Default.CloudUpload,
                title = "Sign in to Google Drive",
                subtitle = "Sign in to enable backup",
                onClick = onLaunchSignIn
            )
        }
        

    }
    
    // Simple Restore Confirmation Dialog
    if (showRestoreWarning) {
        SimpleRestoreDialog(
            backupList = backupList,
            onConfirm = { backup ->
                showRestoreWarning = false
                backupRestoreViewModel.restoreBackup(backup)
            },
            onDismiss = { showRestoreWarning = false }
        )
    }
    
    // Handle state changes
    LaunchedEffect(backupUiState) {
        when (backupUiState) {
            is BackupRestoreUiState.RestoreComplete -> {
                backupRestoreViewModel.clearState()
            }
            else -> {}
        }
    }
}
