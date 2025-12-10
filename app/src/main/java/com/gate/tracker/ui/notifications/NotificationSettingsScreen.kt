package com.gate.tracker.ui.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.NotificationPreferencesEntity
import com.gate.tracker.ui.components.TimePickerPreference
import com.gate.tracker.ui.components.DayPickerPreference

/**
 * Notification Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    viewModel: NotificationSettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val preferences by viewModel.notificationPreferences.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        
        if (preferences == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Study Reminders Section
                item {
                    SettingsSection(
                        title = "Study Reminders",
                        icon = Icons.Default.Notifications
                    ) {
                        SwitchPreference(
                            title = "Daily Study Reminder",
                            description = "Get reminded to study every day",
                            checked = preferences?.dailyReminderEnabled ?: true,
                            onCheckedChange = { enabled ->
                                viewModel.updateDailyReminder(enabled, preferences?.dailyReminderTime ?: "09:00")
                            }
                        )
                        
                        if (preferences?.dailyReminderEnabled == true) {
                            TimePickerPreference(
                                title = "Reminder time",
                                currentTime = preferences?.dailyReminderTime ?: "09:00",
                                onTimeSelected = { time ->
                                    viewModel.updateDailyReminder(true, time)
                                }
                            )
                        }
                    }
                }
                
                // Learning & Progress Section
                item {
                    SettingsSection(
                        title = "Learning & Progress",
                        icon = Icons.Default.School
                    ) {
                        SwitchPreference(
                            title = "Revision Alerts",
                            description = "Remind me to revise chapters",
                            checked = preferences?.revisionAlertsEnabled ?: true,
                            onCheckedChange = { enabled ->
                                viewModel.updateRevisionAlerts(
                                    enabled,
                                    preferences?.revisionAlertsTime ?: "18:00",
                                    preferences?.revisionAlertsDays ?: "1,3,5"
                                )
                            }
                        )
                        
                        if (preferences?.revisionAlertsEnabled == true) {
                            TimePickerPreference(
                                title = "Reminder time",
                                currentTime = preferences?.revisionAlertsTime ?: "18:00",
                                onTimeSelected = { time ->
                                    viewModel.updateRevisionAlerts(
                                        true,
                                        time,
                                        preferences?.revisionAlertsDays ?: "1,3,5"
                                    )
                                }
                            )
                            
                            DayPickerPreference(
                                title = "Reminder days",
                                selectedDays = preferences?.revisionAlertsDays ?: "1,3,5",
                                onDaysSelected = { days ->
                                    viewModel.updateRevisionAlerts(
                                        true,
                                        preferences?.revisionAlertsTime ?: "18:00",
                                        days
                                    )
                                }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        SwitchPreference(
                            title = "Mock Test Reminders",
                            description = "Encourage regular practice tests",
                            checked = preferences?.mockTestRemindersEnabled ?: true,
                            onCheckedChange = { enabled ->
                                viewModel.updateMockTestReminders(
                                    enabled,
                                    preferences?.mockTestRemindersTime ?: "15:00",
                                    preferences?.mockTestRemindersDays ?: "0,6"
                                )
                            }
                        )
                        
                        if (preferences?.mockTestRemindersEnabled == true) {
                            TimePickerPreference(
                                title = "Reminder time",
                                currentTime = preferences?.mockTestRemindersTime ?: "15:00",
                                onTimeSelected = { time ->
                                    viewModel.updateMockTestReminders(
                                        true,
                                        time,
                                        preferences?.mockTestRemindersDays ?: "0,6"
                                    )
                                }
                            )
                            
                            DayPickerPreference(
                                title = "Reminder days",
                                selectedDays = preferences?.mockTestRemindersDays ?: "0,6",
                                onDaysSelected = { days ->
                                    viewModel.updateMockTestReminders(
                                        true,
                                        preferences?.mockTestRemindersTime ?: "15:00",
                                        days
                                    )
                                }
                            )
                        }
                    }
                }
                
                // Motivation Section
                item {
                    SettingsSection(
                        title = "Motivation",
                        icon = Icons.Default.Star
                    ) {
                        SwitchPreference(
                            title = "Exam Countdown",
                            description = "Daily exam countdown notifications",
                            checked = preferences?.examCountdownEnabled ?: true,
                            onCheckedChange = { enabled ->
                                viewModel.updateExamCountdown(
                                    enabled,
                                    preferences?.examCountdownTime ?: "20:00"
                                )
                            }
                        )
                        
                        if (preferences?.examCountdownEnabled == true) {
                            TimePickerPreference(
                                title = "Reminder time",
                                currentTime = preferences?.examCountdownTime ?: "20:00",
                                onTimeSelected = { time ->
                                    viewModel.updateExamCountdown(true, time)
                                }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        SwitchPreference(
                            title = "Motivational Quotes",
                            description = "Get daily motivation",
                            checked = preferences?.motivationalEnabled ?: true,
                            onCheckedChange = { enabled ->
                                viewModel.updateMotivational(
                                    enabled,
                                    preferences?.motivationalTime ?: "08:00"
                                )
                            }
                        )
                        
                        if (preferences?.motivationalEnabled == true) {
                            TimePickerPreference(
                                title = "Reminder time",
                                currentTime = preferences?.motivationalTime ?: "08:00",
                                onTimeSelected = { time ->
                                    viewModel.updateMotivational(true, time)
                                }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        SwitchPreference(
                            title = "Achievement Celebrations",
                            description = "Celebrate your milestones",
                            checked = preferences?.achievementNotificationsEnabled ?: true,
                            onCheckedChange = viewModel::updateAchievementNotifications
                        )
                    }
                }
                
                // Engagement Section
                item {
                    SettingsSection(
                        title = "Engagement",
                        icon = Icons.Default.Timer
                    ) {
                        SwitchPreference(
                            title = "Inactivity Alerts",
                            description = "Remind me if I haven't studied",
                            checked = preferences?.inactivityAlertsEnabled ?: true,
                            onCheckedChange = { enabled ->
                                viewModel.updateInactivityAlerts(
                                    enabled,
                                    preferences?.inactivityThresholdDays ?: 3,
                                    preferences?.inactivityAlertsTime ?: "19:00"
                                )
                            }
                        )
                        
                        if (preferences?.inactivityAlertsEnabled == true) {
                            TimePickerPreference(
                                title = "Reminder time",
                                currentTime = preferences?.inactivityAlertsTime ?: "19:00",
                                onTimeSelected = { time ->
                                    viewModel.updateInactivityAlerts(
                                        true,
                                        preferences?.inactivityThresholdDays ?: 3,
                                        time
                                    )
                                }
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Alert after",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                var expanded by remember { mutableStateOf(false) }
                                val options = listOf(3, 5, 7)
                                
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }
                                ) {
                                    OutlinedTextField(
                                        value = "${preferences?.inactivityThresholdDays ?: 3} days",
                                        onValueChange = {},
                                        readOnly = true,
                                        modifier = Modifier
                                            .menuAnchor()
                                            .width(120.dp),
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                        }
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        options.forEach { days ->
                                            DropdownMenuItem(
                                                text = { Text("$days days") },
                                                onClick = {
                                                    viewModel.updateInactivityAlerts(true, days)
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Do Not Disturb Section
                item {
                    SettingsSection(
                        title = "Do Not Disturb",
                        icon = Icons.Default.DoNotDisturb
                    ) {
                        SwitchPreference(
                            title = "Quiet Hours",
                            description = "Pause notifications during specific hours",
                            checked = preferences?.quietHoursEnabled ?: false,
                            onCheckedChange = { enabled ->
                                viewModel.updateQuietHours(
                                    enabled,
                                    preferences?.quietHoursStart ?: "22:00",
                                    preferences?.quietHoursEnd ?: "08:00"
                                )
                            }
                        )
                        
                        if (preferences?.quietHoursEnabled == true) {
                            Text(
                                text = "Quiet: ${preferences?.quietHoursStart} - ${preferences?.quietHoursEnd}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
                
                // Info card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Notifications help you stay consistent with your GATE preparation. You can customize them anytime!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            
            content()
        }
    }
}

@Composable
fun SwitchPreference(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
