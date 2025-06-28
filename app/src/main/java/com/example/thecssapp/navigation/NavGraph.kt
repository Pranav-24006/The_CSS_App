package com.example.thecssapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thecssapp.ui.screens.home.HomeScreen
import com.example.thecssapp.ui.screens.events.EventsScreen
import com.example.thecssapp.ui.screens.attendance.AttendanceScreen
import com.example.thecssapp.ui.screens.planner.PlannerScreen
import com.example.thecssapp.constants.Routes

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.EVENTS) { EventsScreen(navController) }
        composable(Routes.ATTENDANCE) { AttendanceScreen(navController) }
        composable(Routes.PLANNER) { PlannerScreen(navController) }
    }
}
