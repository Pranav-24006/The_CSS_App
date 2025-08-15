package com.example.thecssapp.ui.screens.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecssapp.data.StudentProfileDataStore
import com.example.thecssapp.model.StudentProfile
import kotlinx.coroutines.launch

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = StudentProfileDataStore(application)

    fun saveProfile(profile: StudentProfile, onComplete: () -> Unit) {
        viewModelScope.launch {
            dataStore.saveProfile(profile)
            onComplete()
        }
    }
}
