package com.example.thecssapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.constants.Routes
import com.example.thecssapp.data.StudentProfileDataStore
import com.example.thecssapp.data.ScheduleDataStore
import com.example.thecssapp.ui.components.BottomNavBar
import com.example.thecssapp.ui.screens.attendance.AttendanceScreen
import com.example.thecssapp.ui.screens.events.EventsScreen
import com.example.thecssapp.ui.screens.events.eventdetails.EventDetailScreen
import com.example.thecssapp.ui.screens.home.HomeScreen
import com.example.thecssapp.ui.screens.onboarding.OnboardingScreen
import com.example.thecssapp.ui.screens.splash.SplashScreen
import com.example.thecssapp.ui.screens.planner.PlannerScreen
import com.example.thecssapp.ui.screens.planner.addschedule.AddScheduleScreen
import com.example.thecssapp.ui.theme.TheCSSAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create DataStores once and reuse across screens
        val profileDataStore = StudentProfileDataStore(applicationContext)
        val scheduleDataStore = ScheduleDataStore(applicationContext)

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            TheCSSAppTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                Scaffold(
                    bottomBar = {
                        // Hide bottom nav on splash, onboarding, and home
                        if (currentRoute != Routes.HOME &&
                            currentRoute != Routes.SPLASH &&
                            currentRoute != Routes.ONBOARDING
                        ) {
                            BottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.SPLASH
                        ) {
                            // Splash decides next route (onboarding or home)
                            composable(Routes.SPLASH) {
                                SplashScreen(
                                    navController = navController,
                                    dataStore = profileDataStore
                                )
                            }

                            // Onboarding (saves profile, then navigates to HOME)
                            composable(Routes.ONBOARDING) {
                                OnboardingScreen(navController = navController)
                            }

                            // Main app screens
                            composable(Routes.HOME) { HomeScreen(navController) }
                            composable(Routes.EVENTS) { EventsScreen(navController) }
                            composable(Routes.ATTENDANCE) { AttendanceScreen(navController) }
                            composable(Routes.PLANNER) {
                                PlannerScreen(navController, scheduleDataStore)
                            }
                            composable(Routes.EVENT_DETAILS) { backStackEntry ->
                                val eventId =
                                    backStackEntry.arguments?.getString("eventId")?.toIntOrNull()
                                        ?: -1
                                EventDetailScreen(eventId, navController)
                            }
                            composable(Routes.ADD_SCHEDULE) {
                                AddScheduleScreen(navController, scheduleDataStore)
                            }
                        }
                    }
                }
            }
        }
    }
}
