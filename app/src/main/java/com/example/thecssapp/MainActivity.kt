package com.example.thecssapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.constants.Routes
import com.example.thecssapp.ui.components.BottomNavBar
import com.example.thecssapp.ui.screens.attendance.AttendanceScreen
import com.example.thecssapp.ui.screens.events.EventsScreen
import com.example.thecssapp.ui.screens.home.HomeScreen
import com.example.thecssapp.ui.screens.planner.PlannerScreen
import com.example.thecssapp.ui.theme.TheCSSAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            TheCSSAppTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {

                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute != Routes.HOME) {
                            BottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.HOME) { HomeScreen(navController) }
                        composable(Routes.EVENTS) { EventsScreen(navController) }
                        composable(Routes.ATTENDANCE) { AttendanceScreen(navController) }
                        composable(Routes.PLANNER) { PlannerScreen(navController) }
                    }
                }
            }
        }
    }
}
