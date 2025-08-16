package com.example.thecssapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thecssapp.model.CourseItem // <-- Assuming this is your course entity
import com.example.thecssapp.model.ScheduleItem

// Add CourseItem to the list of entities
@Database(entities = [ScheduleItem::class, CourseItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Declare all your DAOs here
    abstract fun scheduleDao(): ScheduleDao
    abstract fun courseDao(): CourseDao

    // A class can only have ONE companion object
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Use the existing INSTANCE or create a new one inside a synchronized block
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Give the database a single, unified name
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}