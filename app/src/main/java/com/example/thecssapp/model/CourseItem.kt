package com.example.thecssapp.model;
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses") // Define the table name for Room
data class CourseItem(
    @PrimaryKey(autoGenerate = true) // Let Room handle creating a unique ID
    val id: Long = 0,
    val title: String,
    val attended: Int,
    val total: Int
)