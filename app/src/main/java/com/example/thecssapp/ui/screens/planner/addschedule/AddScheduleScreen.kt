package com.example.thecssapp.ui.screens.planner.addschedule

import android.content.Context
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
import com.example.thecssapp.data.ScheduleDataStore
import com.example.thecssapp.model.ScheduleItem
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * ## VALIDATION ADDED
 * A helper function to check if the time string matches a common format like "10:00 AM".
 */
private fun isValidTimeFormat(time: String): Boolean {
    // This regex checks for formats like H:MM AM/PM, HH:MM AM/PM (case-insensitive for am/pm)
    val timeRegex = "^(0?[1-9]|1[0-2]):[0-5][0-9]\\s(?i)(AM|PM)$".toRegex()
    return time.matches(timeRegex)
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(navController: NavController, dataStore: ScheduleDataStore) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("Class") }
    var extra by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // ## VALIDATION ADDED: Get context to show Toast messages
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
                            date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
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
                    label = { Text("Time (e.g., 10:00 AM)") },
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
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTagMenuExpanded) },
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
                        /**
                         * ## VALIDATION ADDED
                         * Before saving, check if the required fields are filled and formatted correctly.
                         * If not, show a message and stop the save process.
                         */
                        when {
                            title.isBlank() -> {
                                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            time.isBlank() -> {
                                Toast.makeText(context, "Time cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            !isValidTimeFormat(time.trim()) -> {
                                Toast.makeText(context, "Invalid time format. Use HH:MM AM/PM", Toast.LENGTH_SHORT).show()
                            }
                            location.isBlank() -> {
                                Toast.makeText(context, "Location cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                // All checks passed, proceed to save
                                val newItem = ScheduleItem(
                                    id = System.currentTimeMillis(),
                                    title = title.trim(),
                                    date = date.toString(),
                                    time = time.trim(),
                                    location = location.trim(),
                                    tag = tag,
                                    extra = extra.trim()
                                )
                                coroutineScope.launch {
                                    dataStore.addSchedule(newItem)
                                    navController.popBackStack()
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