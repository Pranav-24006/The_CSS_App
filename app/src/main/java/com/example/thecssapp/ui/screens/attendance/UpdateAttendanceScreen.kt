package com.example.thecssapp.ui.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thecssapp.model.CourseItem
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAttendanceScreen(
    navController: NavController,
    courseFlow: Flow<CourseItem>, // Passed from the ViewModel
    onUpdate: (CourseItem) -> Unit
) {
    val course by courseFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Attendance") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        course?.let { currentCourse ->
            val percentage = if (currentCourse.total > 0) (currentCourse.attended.toFloat() / currentCourse.total.toFloat() * 100).toInt() else 0
            val attendanceColor = when {
                percentage >= 85 -> Color(0xFF4CAF50) // Green
                percentage >= 75 -> Color(0xFFFFC107) // Amber
                else -> Color(0xFFF44336)          // Red
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(currentCourse.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Current Attendance: ${currentCourse.attended} / ${currentCourse.total}",
                    fontSize = 20.sp
                )

                Text(
                    "$percentage%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = attendanceColor
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            val updatedCourse = currentCourse.copy(
                                attended = currentCourse.attended + 1,
                                total = currentCourse.total + 1
                            )
                            onUpdate(updatedCourse)
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Attended")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(
                        onClick = {
                            val updatedCourse = currentCourse.copy(
                                total = currentCourse.total + 1
                            )
                            onUpdate(updatedCourse)
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Missed")
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
