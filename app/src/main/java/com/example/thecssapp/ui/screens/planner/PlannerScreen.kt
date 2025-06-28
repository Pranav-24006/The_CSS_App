package com.example.thecssapp.ui.screens.planner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.ui.theme.TheCSSAppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlannerScreen(navController: NavController) {
    val today = LocalDate.now()

    var selectedDate by remember { mutableStateOf(today) }
    var currentMonthYear by remember { mutableStateOf(YearMonth.from(today)) }

    val scheduleItems = listOf(
        ScheduleItem("CSE101: Intro to Programming", "9:00 AM - 10:30 AM", "Room 104", "Class", "Chapter 5: Arrays & Pointers"),
        ScheduleItem("Assignment Submission", "2:00 PM", "CSE202", "Deadline", "Lab 3: Linked Lists"),
        ScheduleItem("Tech Talk: AI in Education", "4:00 PM - 6:00 PM", "Auditorium", "Event", "Speaker: Dr. Robert Chen")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planner", fontWeight = FontWeight.Bold) },
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
                onClick = { /* TODO */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            CalendarHeader(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                currentMonth = currentMonthYear,
                onPrevMonth = { currentMonthYear = currentMonthYear.minusMonths(1) },
                onNextMonth = { currentMonthYear = currentMonthYear.plusMonths(1) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Today's Schedule", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = { /* Add schedule */ }) {
                    Text("+ Add")
                }
            }

            scheduleItems.forEach { item -> ScheduleCard(item) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    currentMonth: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)
    val offset = firstDayOfMonth.dayOfWeek.value % 7
    val days = (1..daysInMonth).map { currentMonth.atDay(it) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPrevMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Text(
            "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Day-of-week headers
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.height(235.dp)) {
        items(offset) { Box(modifier = Modifier.size(40.dp)) }

        items(days.size) { i ->
            val date = days[i]
            val isSelected = date == selectedDate
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onDateSelected(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${date.dayOfMonth}",
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}



@Composable
fun ScheduleCard(item: ScheduleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                TagChip(item.tag)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("${item.time} • ${item.location}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.extra, fontSize = 12.sp)
        }
    }
}

@Composable
fun TagChip(tag: String) {
    val tagColor = when (tag) {
        "Class" -> Color(0xFF2196F3)
        "Deadline" -> Color(0xFFFFC107)
        "Event" -> Color(0xFF673AB7)
        else -> Color.Gray
    }

    Button(
        onClick = { /* Future: show tag info */ },
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = tagColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.height(26.dp)
    ) {
        Text(tag, fontSize = 11.sp)
    }
}

data class ScheduleItem(
    val title: String,
    val time: String,
    val location: String,
    val tag: String,
    val extra: String
)

@Preview(showSystemUi = true)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun PlannerPreview(){
    val navController = rememberNavController()
    TheCSSAppTheme(darkTheme = false, dynamicColor = false) {
        PlannerScreen(navController)
    }
}