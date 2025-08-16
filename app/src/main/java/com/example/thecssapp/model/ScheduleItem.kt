package com.example.thecssapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules") // Define the table name for Room
data class ScheduleItem(
    @PrimaryKey(autoGenerate = true) // Let Room handle creating a unique ID
    val id: Long = 0,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val tag: String,
    val extra: String
)