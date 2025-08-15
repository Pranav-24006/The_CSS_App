package com.example.thecssapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.thecssapp.model.StudentProfile
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("student_profile")

class StudentProfileDataStore(private val context: Context) {

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val ROLL_KEY = stringPreferencesKey("roll_number")
        private val DEPT_KEY = stringPreferencesKey("department")
        private val YEAR_KEY = intPreferencesKey("year")
        private val INTERESTS_KEY = stringPreferencesKey("interests") // comma-separated
    }

    suspend fun saveProfile(profile: StudentProfile) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = profile.name
            prefs[ROLL_KEY] = profile.rollNumber
            prefs[DEPT_KEY] = profile.department
            prefs[YEAR_KEY] = profile.year
            prefs[INTERESTS_KEY] = profile.interests.joinToString(",")
        }
    }

    fun getProfile() = context.dataStore.data.map { prefs ->
        StudentProfile(
            name = prefs[NAME_KEY] ?: "",
            rollNumber = prefs[ROLL_KEY] ?: "",
            department = prefs[DEPT_KEY] ?: "",
            year = prefs[YEAR_KEY] ?: 0,
            interests = prefs[INTERESTS_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
        )
    }
}
