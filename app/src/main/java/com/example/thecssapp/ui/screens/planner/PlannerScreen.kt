package com.example.thecssapp.ui.screens.planner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thecssapp.data.ScheduleDataStore
import com.example.thecssapp.model.ScheduleItem
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlannerScreen(
    navController: NavController,
    dataStore: ScheduleDataStore
) {
    val today = LocalDate.now()

    var selectedDate by remember { mutableStateOf(today) }
    var currentMonthYear by remember { mutableStateOf(YearMonth.from(today)) }

    val schedules: List<ScheduleItem> by dataStore.getSchedules()
        .collectAsState(initial = emptyList())

    /**
     * FIX 1: PERFORMANCE
     * The schedule list is now wrapped in `remember`. This prevents the expensive
     * filtering operation from running on every recomposition, boosting performance.
     * It will only re-filter when the `schedules` list or `selectedDate` changes.
     */
    val todaysSchedules = remember(schedules, selectedDate) {
        schedules.filter { it.date == selectedDate.toString() }
    }

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
                onClick = {
                    navController.navigate("addSchedule")
                },
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

            Text(
                "Schedule for ${selectedDate.dayOfMonth} ${selectedDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (todaysSchedules.isEmpty()) {
                // Keep this centered within the remaining space
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No schedules for this day.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                /**
                 * FIX 2: LAYOUT
                 * Changed `Modifier.fillMaxSize()` to `Modifier.weight(1f)`.
                 * This makes the LazyColumn fill only the remaining available space
                 * instead of the whole screen, preventing it from overlapping the calendar.
                 */
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    // Using a key improves performance and state preservation for list items
                    items(items = todaysSchedules, key = { it.id }) { item ->
                        ScheduleCard(item)
                    }
                }
            }
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
    // Adjusted offset to correctly handle Sunday as the first day of the week (value=7)
    val offset = (firstDayOfMonth.dayOfWeek.value % 7)
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

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
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

    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.height(250.dp)) {
        items(offset) { Box(modifier = Modifier.size(40.dp)) }

        items(days.size) { i ->
            val date = days[i]
            val isSelected = date == selectedDate
            Box(
                modifier = Modifier
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

    // Using a Box with background is simpler than a Button if it's not clickable
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(26.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(tagColor)
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Text(tag, fontSize = 11.sp, color = Color.White)
    }
}