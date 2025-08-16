package com.example.thecssapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItem(
    val id: Long,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val tag: String,
    val extra: String
)