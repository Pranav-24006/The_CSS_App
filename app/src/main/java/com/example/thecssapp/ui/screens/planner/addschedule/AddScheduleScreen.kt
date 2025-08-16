package com.example.thecssapp.ui.screens.planner.addschedule

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.thecssapp.data.ScheduleDao
import com.example.thecssapp.model.ScheduleItem
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

/**
 * ## FIX: A robust time parser that understands multiple formats.
 * This builder creates a single DateTimeFormatter that can parse both 12-hour (h:mm a)
 * and 24-hour (H:mm) time formats. This is much safer than nested try-catch blocks.
 */
@RequiresApi(Build.VERSION_CODES.O)
private val timeParser = DateTimeFormatterBuilder()
    .appendOptional(DateTimeFormatter.ofPattern("h:mm a"))  // For formats like "2:30 PM"
    .appendOptional(DateTimeFormatter.ofPattern("H:mm"))    // For formats like "14:30"
    .toFormatter(Locale.US) // Use a specific Locale for AM/PM consistency

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(navController: NavController, dao: ScheduleDao) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("Class") }
    var extra by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            date = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Schedule") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )
            }

            item {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g., 14:30 or 2:30 PM)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                var isTagMenuExpanded by remember { mutableStateOf(false) }
                val tagOptions = listOf("Class", "Deadline", "Event")

                ExposedDropdownMenuBox(
                    expanded = isTagMenuExpanded,
                    onExpandedChange = { isTagMenuExpanded = !isTagMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = tag,
                        onValueChange = {},
                        label = { Text("Tag") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTagMenuExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isTagMenuExpanded,
                        onDismissRequest = { isTagMenuExpanded = false }
                    ) {
                        tagOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    tag = option
                                    isTagMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = extra,
                    onValueChange = { extra = it },
                    label = { Text("Extra Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            }

            item {
                Button(
                    onClick = {
                        when {
                            title.isBlank() -> {
                                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            time.isBlank() -> {
                                Toast.makeText(context, "Please enter a time", Toast.LENGTH_SHORT).show()
                            }
                            location.isBlank() -> {
                                Toast.makeText(context, "Location cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                val parseResult = runCatching {
                                    LocalTime.parse(time.trim().uppercase(), timeParser)
                                }

                                if (parseResult.isSuccess) {
                                    val parsedTime = parseResult.getOrThrow()
                                    val formattedTime = parsedTime.format(DateTimeFormatter.ofPattern("h:mm a"))

                                    // CHANGE: Create the item without an ID. Room generates it.
                                    val newItem = ScheduleItem(
                                        title = title.trim(),
                                        date = date.toString(),
                                        time = formattedTime,
                                        location = location.trim(),
                                        tag = tag,
                                        extra = extra.trim()
                                    )
                                    coroutineScope.launch {
                                        // CHANGE: Use the dao to save the new item
                                        dao.addSchedule(newItem)
                                        navController.popBackStack()
                                    }
                                } else {
                                    Toast.makeText(context, "Invalid time format", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}