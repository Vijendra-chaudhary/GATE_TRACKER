package com.gate.tracker.ui.mocktest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.SubjectEntity
import com.gate.tracker.data.local.entity.TestType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Simplified dialog for adding a new mock test
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMockTestDialog(
    branchSubjects: List<SubjectEntity>,
    onDismiss: () -> Unit,
    onConfirm: (testName: String, score: Float, maxScore: Float, testDate: Long, testType: TestType, selectedSubjects: List<Int>) -> Unit
) {
    var testName by remember { mutableStateOf("") }
    var scoreText by remember { mutableStateOf("") }
    var maxScoreText by remember { mutableStateOf("100") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var testType by remember { mutableStateOf(TestType.FULL_SYLLABUS) }
    var selectedSubjects by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Mock Test",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Test Name
                OutlinedTextField(
                    value = testName,
                    onValueChange = { 
                        testName = it
                        errorMessage = null
                    },
                    label = { Text("Test Name") },
                    placeholder = { Text("e.g., GATE Mock Test 1") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage?.contains("name", ignoreCase = true) == true
                )

                // Score Fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = scoreText,
                        onValueChange = { 
                            if (it.isEmpty() || it.toFloatOrNull() != null) {
                                scoreText = it
                                errorMessage = null
                            }
                        },
                        label = { Text("Score") },
                        placeholder = { Text("67") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = errorMessage?.contains("score", ignoreCase = true) == true
                    )

                    OutlinedTextField(
                        value = maxScoreText,
                        onValueChange = { 
                            if (it.isEmpty() || it.toFloatOrNull() != null) {
                                maxScoreText = it
                                errorMessage = null
                            }
                        },
                        label = { Text("Max Score") },
                        placeholder = { Text("100") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = errorMessage?.contains("score", ignoreCase = true) == true
                    )
                }

                // Date Picker
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dateFormat.format(Date(selectedDate)))
                }

                Divider()

                // Test Type
                Text(
                    text = "Test Type",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = testType == TestType.FULL_SYLLABUS,
                        onClick = { 
                            testType = TestType.FULL_SYLLABUS
                            selectedSubjects = emptySet()
                            errorMessage = null
                        },
                        label = { Text("Full Syllabus") },
                        leadingIcon = if (testType == TestType.FULL_SYLLABUS) {
                            { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )
                    
                    FilterChip(
                        selected = testType == TestType.SELECTED_SUBJECT,
                        onClick = { 
                            testType = TestType.SELECTED_SUBJECT
                            errorMessage = null
                        },
                        label = { Text("Selected Subject") },
                        leadingIcon = if (testType == TestType.SELECTED_SUBJECT) {
                            { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Subject Selection
                if (testType == TestType.SELECTED_SUBJECT) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Select Subjects",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(branchSubjects) { subject ->
                                FilterChip(
                                    selected = selectedSubjects.contains(subject.id),
                                    onClick = {
                                        selectedSubjects = if (selectedSubjects.contains(subject.id)) {
                                            selectedSubjects - subject.id
                                        } else {
                                            selectedSubjects + subject.id
                                        }
                                        errorMessage = null
                                    },
                                    label = { Text(subject.name) }
                                )
                            }
                        }
                        
                        if (selectedSubjects.isNotEmpty()) {
                            Text(
                                text = "${selectedSubjects.size} selected",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Error Message
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validation
                    when {
                        testName.isBlank() -> {
                            errorMessage = "Test name is required"
                        }
                        scoreText.isBlank() -> {
                            errorMessage = "Score is required"
                        }
                        maxScoreText.isBlank() -> {
                            errorMessage = "Max score is required"
                        }
                        testType == TestType.SELECTED_SUBJECT && selectedSubjects.isEmpty() -> {
                            errorMessage = "Please select at least one subject"
                        }
                        else -> {
                            val score = scoreText.toFloatOrNull()
                            val maxScore = maxScoreText.toFloatOrNull()
                            
                            if (score == null || maxScore == null) {
                                errorMessage = "Invalid score values"
                            } else if (score < 0 || maxScore <= 0) {
                                errorMessage = "Score values must be positive"
                            } else if (score > maxScore) {
                                errorMessage = "Score cannot exceed max score"
                            } else if (selectedDate > System.currentTimeMillis()) {
                                errorMessage = "Test date cannot be in the future"
                            } else {
                                onConfirm(
                                    testName, 
                                    score, 
                                    maxScore, 
                                    selectedDate, 
                                    testType,
                                    selectedSubjects.toList()
                                )
                            }
                        }
                    }
                }
            ) {
                Text("Add Test")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = it
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
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
}
