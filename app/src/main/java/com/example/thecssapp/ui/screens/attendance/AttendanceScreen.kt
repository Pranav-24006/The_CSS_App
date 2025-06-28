package com.example.thecssapp.ui.screens.attendance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.ui.theme.TheCSSAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController) {
    val listState = rememberLazyListState()

    TheCSSAppTheme(darkTheme = true, dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Anchored Top Section
                Column {
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(horizontal = 4.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        GreetingHeader("Pranav Verma")
                    }

                    Text(
                        text = "Attendance Overview",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }

                // Scrollable Content
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            BarChart()
                        }

                        items(4) { index ->
                            val subjects = listOf(
                                Triple("CSE101: Intro to Programming", 92, 1),
                                Triple("CSE202: Data Structures", 78, 2),
                                Triple("CSE305: Database Systems", 68, 4),
                                Triple("CSE401: AI Fundamentals", 0, 1)
                            )
                            val (subject, percent, bunks) = subjects[index]
                            SubjectCard(subject, percent, bunks)
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    // Floating Action Button
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        FloatingActionButton(
                            onClick = { /* TODO */ },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
            }
        }
    }
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
         text = "$greeting\n$userName",
         fontWeight = FontWeight.Bold,
         fontSize = 20.sp,
         color = MaterialTheme.colorScheme.onSurface,
         modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    )
}
@Composable
fun BarChart() {
    val data = listOf(92, 78, 68, 0)
    val subjects = listOf("CSE101", "CSE202", "CSE305", "CSE401")

    val barWidthDp = 16.dp
    val spacingDp = 48.dp

    val density = LocalDensity.current
    val barWidthPx = with(density) { barWidthDp.toPx() }
    val spacingPx = with(density) { spacingDp.toPx() }

    val colorScheme = MaterialTheme.colorScheme
    val colors = data.map {
        when {
            it >= 85 -> Color(0xFF4CAF50) // Green
            it >= 75 -> Color(0xFFFFC107) // Amber
            else -> Color(0xFFF44336)     // Red
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

                    // Draw scale lines
                    val scaleLines = listOf(1f, 0.75f, 0.5f, 0.25f,0.00f)
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

                    // Draw bars
                    data.forEachIndexed { index, value ->
                        val barHeight = size.height * (value / 100f)
                        val x = startX + index * (barWidthPx + spacingPx)
                        drawRoundRect(
                            color = colors[index],
                            topLeft = Offset(x, size.height - barHeight),
                            size = androidx.compose.ui.geometry.Size(barWidthPx, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(32f, 32f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
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
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}




@Composable
fun SubjectCard(subject: String, percent: Int, bunks: Int) {
    val attendanceColor = when {
        percent >= 85 -> Color(0xFF4CAF50) // Green
        percent >= 75 -> Color(0xFFFFC107) // Amber
        else -> Color(0xFFF44336)          // Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 25.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(subject, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Attendance: $percent%", fontSize = 14.sp, color = attendanceColor)
            Spacer(modifier = Modifier.height(4.dp))

            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { percent / 100f },
                modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                color = attendanceColor,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text("Bunks Available: $bunks", fontSize = 14.sp)
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AttendancePreview() {
    val navController = rememberNavController()
    AttendanceScreen(navController)
}