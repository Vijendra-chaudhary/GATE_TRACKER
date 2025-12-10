package com.gate.tracker.ui.examdate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDateScreen(
    viewModel: ExamDateViewModel,
    onBackClick: () -> Unit
) {
    val examDate by viewModel.examDate.collectAsState()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = examDate ?: System.currentTimeMillis()
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Exam Date",
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            examDate?.let {
                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Current Exam Date",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = dateFormat.format(Date(it)),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            DatePicker(
                state = datePickerState,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate ->
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                        }
                        viewModel.updateExamDate(
                            year = calendar.get(Calendar.YEAR),
                            month = calendar.get(Calendar.MONTH),
                            day = calendar.get(Calendar.DAY_OF_MONTH),
                            onSuccess = onBackClick
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save Date",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
