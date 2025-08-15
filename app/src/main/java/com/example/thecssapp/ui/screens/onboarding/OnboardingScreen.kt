package com.example.thecssapp.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.thecssapp.model.StudentProfile

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tell us about yourself", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = rollNumber, onValueChange = { rollNumber = it }, label = { Text("Roll Number") })
        OutlinedTextField(value = department, onValueChange = { department = it }, label = { Text("Department") })

        OutlinedTextField(
            value = year,
            onValueChange = { year = it.filter { ch -> ch.isDigit() } },
            label = { Text("Year") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(value = interests, onValueChange = { interests = it }, label = { Text("Interests (comma-separated)") })

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val profile = StudentProfile(
                    name = name,
                    rollNumber = rollNumber,
                    department = department,
                    year = year.toIntOrNull() ?: 0,
                    interests = interests.split(",").map { it.trim() }
                )
                viewModel.saveProfile(profile) {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save & Continue")
        }
    }
}
