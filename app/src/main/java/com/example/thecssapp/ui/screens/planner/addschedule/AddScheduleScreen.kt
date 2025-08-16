package com.example.thecssapp.ui.screens.planner.addschedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.thecssapp.data.ScheduleDataStore
import com.example.thecssapp.model.ScheduleItem
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(navController: NavController, dataStore: ScheduleDataStore) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("Class") } // Default to a valid tag
    var extra by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    /**
     * FIX 1: STATE MANAGEMENT
     * Moved `rememberDatePickerState` to the top level of the composable.
     * State should never be declared inside conditional blocks like `if`.
     */
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
                // FIX 2: UX - Added a back navigation button
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Changed to LazyColumn to prevent overflow on small screens
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
                /**
                 * FIX 3: UX - Replaced the free-text tag field with a dropdown
                 * for consistency and to prevent typos.
                 */
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
                        val newItem = ScheduleItem(
                            id = System.currentTimeMillis(),
                            title = title.trim(),
                            date = date.toString(),
                            time = time.trim(),
                            location = location.trim(),
                            tag = tag,
                            extra = extra.trim()
                        )
                        // Launch a coroutine to save the data and navigate back
                        coroutineScope.launch {
                            dataStore.addSchedule(newItem) // Use the renamed function
                            navController.popBackStack()
                        }
                    },
                    // Disable button if the title is blank
                    enabled = title.isNotBlank(),
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