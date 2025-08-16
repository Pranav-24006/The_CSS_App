package com.example.thecssapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thecssapp.model.ScheduleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert
    suspend fun addSchedule(item: ScheduleItem)

    @Query("SELECT * FROM schedules ORDER BY date ASC, time ASC")
    fun getSchedules(): Flow<List<ScheduleItem>>

    // You can easily add other functions later, like @Update or @Delete
}