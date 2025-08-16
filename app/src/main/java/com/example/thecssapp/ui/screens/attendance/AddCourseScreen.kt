package com.example.thecssapp.ui.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thecssapp.model.CourseItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    onDismiss: () -> Unit,
    onAddCourse: (CourseItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var totalClasses by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String?>(null) }
    var totalClassesError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        titleError = if (title.isBlank()) "Title cannot be empty" else null
        totalClassesError = when {
            totalClasses.isBlank() -> "Total classes cannot be empty"
            totalClasses.toIntOrNull() == null -> "Please enter a valid number"
            else -> null
        }
        return titleError == null && totalClassesError == null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Course") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Course Title (e.g., CSE101)") },
                isError = titleError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (titleError != null) {
                Text(text = titleError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = totalClasses,
                onValueChange = { totalClasses = it },
                label = { Text("Initial Total Classes Held") },
                isError = totalClassesError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (totalClassesError != null) {
                Text(text = totalClassesError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (validate()) {
                        val newCourse = CourseItem(
                            title = title,
                            attended = 0, // Always starts with 0 attended
                            total = totalClasses.toInt()
                        )
                        onAddCourse(newCourse)
                        // The navigation back is handled in MainActivity
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Course")
            }
        }
    }
}
