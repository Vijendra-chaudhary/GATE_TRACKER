package com.gate.tracker.ui.mocktest

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.MockTestEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Main screen for displaying mock test history and statistics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestsScreen(
    viewModel: MockTestViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTest by remember { mutableStateOf<MockTestEntity?>(null) }
    var showDetailSheet by remember { mutableStateOf(false) }
    var testToDelete by remember { mutableStateOf<MockTestEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when message changes
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mock Tests",
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
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Test"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.isEmpty) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "No mock tests yet",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Tap the + button to add your first test",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Button(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Test")
                    }
                }
            }
        } else {
            // Content with tests
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                
                // Statistics Card
                item {
                    StatisticsCard(uiState = uiState)
                }

                // Chart Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            MockTestChart(
                                dataPoints = viewModel.getChartDataPoints(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Test History Header
                item {
                    Text(
                        text = "Test History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Test List
                items(uiState.tests, key = { it.id }) { test ->
                    TestCard(
                        test = test,
                        onClick = {
                            selectedTest = test
                            showDetailSheet = true
                        },
                        onLongPress = {
                            testToDelete = test
                            showDeleteDialog = true
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }

    // Add Test Dialog
    if (showAddDialog) {
        AddMockTestDialog(
            branchSubjects = subjects,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, score, maxScore, date, testType, selectedSubjects ->
                viewModel.addMockTest(
                    testName = name,
                    score = score,
                    maxScore = maxScore,
                    testDate = date,
                    testType = testType,
                    selectedSubjects = selectedSubjects,
                    onSuccess = {
                        showAddDialog = false
                        snackbarMessage = "Test added successfully"
                    },
                    onError = { error ->
                        snackbarMessage = error
                    }
                )
            }
        )
    }
    
    // Test Detail Bottom Sheet
    if (showDetailSheet && selectedTest != null) {
        TestDetailBottomSheet(
            test = selectedTest!!,
            subjects = subjects,
            onDismiss = {
                showDetailSheet = false
                selectedTest = null
            },
            onDelete = {
                testToDelete = selectedTest
                showDetailSheet = false
                selectedTest = null
                showDeleteDialog = true
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && testToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                testToDelete = null
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Test?") },
            text = {
                Text("Are you sure you want to delete \"${testToDelete!!.testName}\"? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        testToDelete?.let { test ->
                            viewModel.deleteMockTest(
                                test = test,
                                onSuccess = {
                                    snackbarMessage = "Test deleted"
                                    showDeleteDialog = false
                                    testToDelete = null
                                },
                                onError = { error ->
                                    snackbarMessage = error
                                }
                            )
                        }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteDialog = false
                    testToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Statistics Summary Card
 */
@Composable
fun StatisticsCard(uiState: MockTestUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Tests Taken",
                    value = "${uiState.testCount}",
                    icon = Icons.Default.Assignment
                )
                StatItem(
                    label = "Average",
                    value = "%.1f%%".format(uiState.averagePercentage),
                    icon = Icons.Default.TrendingUp
                )
                StatItem(
                    label = "Highest",
                    value = "%.1f%%".format(uiState.highestPercentage),
                    icon = Icons.Default.EmojiEvents
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * Individual Test Card
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestCard(
    test: MockTestEntity,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val percentage = test.percentage
    val isGoodScore = percentage >= 60f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = test.testName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(Date(test.testDate)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "%.1f%%".format(percentage),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isGoodScore) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.error
                    )
                    if (isGoodScore) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${test.score.toInt()}/${test.maxScore.toInt()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
