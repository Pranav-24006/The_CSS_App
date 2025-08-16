package com.example.thecssapp.model

data class Event(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val category: String,
    val description: String = "",
    val location: String = "",
    val date: String = "",
    val price: Double = 0.0
)
