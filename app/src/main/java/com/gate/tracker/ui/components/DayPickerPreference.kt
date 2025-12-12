package com.gate.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayPickerPreference(
    title: String,
    selectedDays: String, // Comma-separated: "0,1,2" (0=Sun, 1=Mon, etc.)
    onDaysSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dayNames = listOf("S", "M", "T", "W", "T", "F", "S")
    val dayFull = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val selectedIndices = remember(selectedDays) {
        selectedDays.split(",").mapNotNull { it.toIntOrNull() }.toMutableStateList()
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dayNames.forEachIndexed { index, day ->
                val isSelected = selectedIndices.contains(index)
                
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            selectedIndices.remove(index)
                        } else {
                            selectedIndices.add(index)
                        }
                        onDaysSelected(selectedIndices.sorted().joinToString(","))
                    },
                    label = {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
        
        // Show selected days summary
        if (selectedIndices.isNotEmpty()) {
            Text(
                text = "Selected: ${selectedIndices.sorted().map { dayFull[it] }.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
