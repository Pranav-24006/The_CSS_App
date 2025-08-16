package com.example.thecssapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.thecssapp.model.ScheduleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.scheduleDataStore by preferencesDataStore("schedule_prefs")

class ScheduleDataStore(private val context: Context) {
    companion object {
        private val SCHEDULES_KEY = stringPreferencesKey("schedules")
    }

    fun getSchedules(): Flow<List<ScheduleItem>> {
        return context.scheduleDataStore.data.map { prefs ->
            prefs[SCHEDULES_KEY]?.let {
                Json.decodeFromString<List<ScheduleItem>>(it)
            } ?: emptyList()
        }
    }

    /**
     * FIX: Renamed from saveSchedule to addSchedule for clarity.
     * This function now correctly describes its action: adding a new item to the list.
     */
    suspend fun addSchedule(item: ScheduleItem) {
        context.scheduleDataStore.edit { prefs ->
            val current = prefs[SCHEDULES_KEY]?.let { Json.decodeFromString<List<ScheduleItem>>(it) } ?: emptyList()
            prefs[SCHEDULES_KEY] = Json.encodeToString(current + item)
        }
    }
}