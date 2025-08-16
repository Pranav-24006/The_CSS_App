package com.example.thecssapp.ui.screens.attendance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thecssapp.model.CourseItem
import com.example.thecssapp.ui.theme.TheCSSAppTheme
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    userName: String,
    courses: List<CourseItem>,
    onAddClick: () -> Unit, // Changed from onAddCourse
    onCourseClick: (CourseItem) -> Unit,
    onDeleteCourse: (CourseItem) -> Unit
) {
    val listState = rememberLazyListState()
    var courseToDelete by remember { mutableStateOf<CourseItem?>(null) }

    // Show Delete Confirmation Dialog
    courseToDelete?.let { course ->
        DeleteConfirmationDialog(
            courseName = course.title,
            onConfirm = {
                onDeleteCourse(course)
                courseToDelete = null // Hide dialog
            },
            onDismiss = {
                courseToDelete = null // Hide dialog
            }
        )
    }

    TheCSSAppTheme(darkTheme = true, dynamicColor = false) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Attendance Tracker", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddClick, // Use the new navigation callback
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Course")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        modifier = Modifier.size(50.dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    GreetingHeader(userName)
                }
                Text(
                    text = "Attendance Overview",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    if (courses.isEmpty()) {
                        item {
                            Text(
                                "No courses yet. Tap the '+' button to add one!",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        item {
                            BarChart(courses = courses)
                        }

                        items(courses, key = { it.id }) { course ->
                            val percentage = if (course.total > 0) {
                                (course.attended.toFloat() / course.total.toFloat() * 100)
                            } else { 0f }

                            val bunkInfoText = if (percentage >= 75) {
                                val bunks = ((4 * course.attended - 3 * course.total) / 3.0).toInt()
                                if (bunks > 0) "Bunks Available: $bunks" else "Bunks Available: 0"
                            } else {
                                val classesNeeded = ceil(3 * course.total - 4 * course.attended.toDouble()).toInt()
                                if (classesNeeded > 0) "Must attend $classesNeeded classes" else "On track"
                            }

                            SubjectCard(
                                subject = course.title,
                                percent = percentage.toInt(),
                                bunkInfo = bunkInfoText,
                                onClick = { onCourseClick(course) },
                                onDeleteClick = { courseToDelete = course }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    subject: String,
    percent: Int,
    bunkInfo: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val attendanceColor = when {
        percent >= 85 -> Color(0xFF4CAF50)
        percent >= 75 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 25.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(subject, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Attendance: $percent%", fontSize = 14.sp, color = attendanceColor)
                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { percent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = attendanceColor,
                    trackColor = MaterialTheme.colorScheme.outlineVariant,
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(bunkInfo, fontSize = 14.sp)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Course",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    courseName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Course") },
        text = { Text("Are you sure you want to delete the course \"$courseName\"? This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun GreetingHeader(userName: String) {
    val greeting = remember {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
    Text(
        text = "$greeting,\n$userName",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    )
}

@Composable
fun BarChart(courses: List<CourseItem>) {
    val data = courses.map {
        if (it.total > 0) (it.attended.toFloat() / it.total.toFloat() * 100).toInt() else 0
    }
    val subjects = courses.map { it.title }

    val barWidthDp = 16.dp
    val spacingDp = 48.dp

    val density = LocalDensity.current
    val barWidthPx = with(density) { barWidthDp.toPx() }
    val spacingPx = with(density) { spacingDp.toPx() }

    val colorScheme = MaterialTheme.colorScheme
    val colors = data.map {
        when {
            it >= 85 -> Color(0xFF4CAF50)
            it >= 75 -> Color(0xFFFFC107)
            else -> Color(0xFFF44336)
        }
    }

    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tap on a bar to view details",
                fontSize = 12.sp,
                color = colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val totalWidth = data.size * barWidthPx + (data.size - 1) * spacingPx
                    val startX = (size.width - totalWidth) / 2

                    val scaleLines = listOf(1f, 0.75f, 0.5f, 0.25f, 0.00f)
                    scaleLines.forEach { level ->
                        val y = size.height * (1 - level)
                        drawLine(
                            color = colorScheme.outlineVariant,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawContext.canvas.nativeCanvas.drawText(
                            "${(level * 100).toInt()}%",
                            8f,
                            y - 4.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.GRAY
                                textSize = 24f
                            }
                        )
                    }

                    data.forEachIndexed { index, value ->
                        val barHeight = size.height * (value / 100f)
                        val x = startX + index * (barWidthPx + spacingPx)
                        drawRoundRect(
                            color = colors[index],
                            topLeft = Offset(x, size.height - barHeight),
                            size = androidx.compose.ui.geometry.Size(barWidthPx, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                subjects.forEachIndexed { index, subject ->
                    Box(
                        modifier = Modifier
                            .width(barWidthDp + spacingDp)
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subject,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
