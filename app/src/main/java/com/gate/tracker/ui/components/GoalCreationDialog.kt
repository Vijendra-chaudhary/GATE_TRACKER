package com.gate.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCreationDialog(
    onDismiss: () -> Unit,
    onCreateWeekly: (targetChapters: Int) -> Unit,
    onCreateDaily: (targetChapters: Int) -> Unit
) {
    var targetChapters by remember { mutableStateOf("10") }
    var selectedType by remember { mutableStateOf("weekly") }
    
    // Update default value when type changes
    LaunchedEffect(selectedType) {
        targetChapters = if (selectedType == "daily") "3" else "10"
    }
    
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Text(
                text = "Create New Goal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Divider()
            
            // Goal type selection
            Text(
                text = "Goal Type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = selectedType == "weekly",
                    onClick = { selectedType = "weekly" },
                    label = { Text("Weekly") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedType == "daily",
                    onClick = { selectedType = "daily" },
                    label = { Text("Daily") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Target input
            Text(
                text = "Target Chapters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            OutlinedTextField(
                value = targetChapters,
                onValueChange = { 
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        targetChapters = it
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Number of chapters") },
                placeholder = { Text("e.g., 10") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            // Helper text
            Text(
                text = if (selectedType == "weekly") {
                    "Complete this many chapters in the next 7 days"
                } else {
                    "Complete this many chapters today"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }
                
                Button(
                    onClick = {
                        val target = targetChapters.toIntOrNull() ?: 0
                        if (target > 0) {
                            if (selectedType == "weekly") {
                                onCreateWeekly(target)
                            } else {
                                onCreateDaily(target)
                            }
                            onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    enabled = targetChapters.toIntOrNull()?.let { it > 0 } ?: false
                ) {
                    Text("Create Goal")
                }
            }
        }
    }
}
