package com.gate.tracker.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gate.tracker.ui.examdate.ExamDateViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    examDateViewModel: ExamDateViewModel,
    backupRestoreViewModel: BackupRestoreViewModel,
    branchId: Int,
    branchName: String,
    onBackClick: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onSignOut: () -> Unit,
    onResetData: () -> Unit,
    onLaunchSignIn: () -> Unit,
    selectedTheme: Int = 0,
    onThemeChange: (Int) -> Unit
) {
    val examDateMillis by examDateViewModel.examDate.collectAsState()
    val backupUiState by backupRestoreViewModel.uiState.collectAsState()
    val backupList by backupRestoreViewModel.backupList.collectAsState()
    val lastBackupTime by backupRestoreViewModel.lastBackupTime.collectAsState()
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showBackupDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var showRestoreWarning by remember { mutableStateOf(false) }
    var selectedBackupFile by remember { mutableStateOf<com.gate.tracker.data.drive.DriveBackupFile?>(null) }
    // showThemeDialog removed for inline selection
    
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val examDateString = examDateMillis?.let { dateFormat.format(Date(it)) } ?: "Not Set"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exam Date Setting Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
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
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Exam Date",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = examDateString,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Text(
                text = "Tap to change the exam date",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Appearance Section
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp)
            )

            // Determine current effective theme state for the switch
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (selectedTheme) {
                1 -> false // Light
                2 -> true  // Dark
                else -> isSystemDark // System
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DayNightSwitch(
                    isDark = isDark,
                    onToggle = {
                        // If currently Dark (2) or System-Dark, switch to Light (1)
                        // If currently Light (1) or System-Light, switch to Dark (2)
                        val newTheme = if (isDark) 1 else 2
                        onThemeChange(newTheme)
                    }
                )
                
                // Show "Reset to System" only if manually overridden (not 0)
                androidx.compose.animation.AnimatedVisibility(visible = selectedTheme != 0) {
                    TextButton(
                        onClick = { onThemeChange(0) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Reset to System Default")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Backup & Restore Section
            BackupRestoreSection(
                backupRestoreViewModel = backupRestoreViewModel,
                branchId = branchId,
                branchName = branchName,
                onLaunchSignIn = onLaunchSignIn
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Notifications Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToNotifications() },
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
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Manage notification preferences",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Switch Branch Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSignOutDialog = true },
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
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Switch Branch",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Change to a different branch",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reset Data Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showResetDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reset All Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Clear all completed chapters and progress",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
        
        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = examDateMillis ?: System.currentTimeMillis()
            )
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { selectedDate ->
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = selectedDate
                                }
                                examDateViewModel.updateExamDate(
                                    year = calendar.get(Calendar.YEAR),
                                    month = calendar.get(Calendar.MONTH),
                                    day = calendar.get(Calendar.DAY_OF_MONTH),
                                    onSuccess = { showDatePicker = false }
                                )
                            }
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        
        // Switch Branch Confirmation Dialog
        if (showSignOutDialog) {
            AlertDialog(
                onDismissRequest = { showSignOutDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(text = "Switch Branch?")
                },
                text = {
                    Text(
                        text = "You will be returned to the branch selection screen. Your progress will be saved."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSignOutDialog = false
                            onSignOut()
                        }
                    ) {
                        Text("Switch Branch", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSignOutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        
        // Reset Data Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(text = "Reset All Progress?")
                },
                text = {
                    Text(
                        text = "This will permanently delete ALL your progress including:\n\n" +
                                "• All completed chapters\n" +
                                "• Subject completion counts\n" +
                                "• Longest streak data\n\n" +
                                "This action cannot be undone. Are you sure you want to continue?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResetDialog = false
                            onResetData()
                        }
                    ) {
                        Text("Reset All Data", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}



@Composable
fun DayNightSwitch(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trackWidth = 100.dp
    val trackHeight = 44.dp
    val thumbSize = 36.dp
    val padding = 4.dp

    // Animate thumb alignment
    val alignment by androidx.compose.animation.animateColorAsState(
        if (isDark) Color.Black else Color.White,
        label = "thumbColor"
    )
    
    // 0f = Left (Light), 1f = Right (Dark)
    // We use a custom Box with offset for maximum control over the "pill" feel
    val horizontalBias by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isDark) 1f else -1f,
        label = "thumbBias",
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        )
    )
    
    val trackColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isDark) 
            Color(0xFF2C2C2C) 
        else 
            Color(0xFFE0E0E0), 
        label = "trackColor"
    )

    Box(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .clip(RoundedCornerShape(100))
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // No ripple for custom switch
                onClick = onToggle
            )
            .padding(padding)
    ) {
        // Icons on the track
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sun Icon (Left)
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = "Light Mode",
                tint = if (isDark) Color.Gray else Color(0xFFFFB300), // Active Gold or Inactive Gray
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 6.dp)
            )
            
            // Moon Icon (Right)
            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = "Dark Mode",
                tint = if (isDark) Color.White else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 6.dp)
            )
        }

        // The Sliding Thumb
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (trackWidth - thumbSize - padding * 2) / 2 * horizontalBias) // Calculate offset based on bias (-1 to 1)
                .size(thumbSize)
                .shadow(4.dp, androidx.compose.foundation.shape.CircleShape)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(Color.White)
        )
    }
}
