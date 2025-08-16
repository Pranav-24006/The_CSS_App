package com.example.thecssapp.model

// This is an example based on your DataStore.
// Ensure the properties match your actual class.
data class StudentProfile(
    val name: String = "",
    val rollNumber: String = "",
    val department: String = "",
    val year: Int = 0,
    val interests: List<String> = emptyList()
)

