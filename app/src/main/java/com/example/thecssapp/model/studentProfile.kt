package com.example.thecssapp.model

data class StudentProfile(
    val name: String,
    val rollNumber: String,
    val department: String,
    val year: Int,
    val interests: List<String>
)
