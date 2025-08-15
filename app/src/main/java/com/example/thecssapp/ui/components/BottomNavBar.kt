package com.example.thecssapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.thecssapp.constants.Routes

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Routes.EVENTS to "Events",
        Routes.ATTENDANCE to "Attendance",
        Routes.PLANNER to "Planner"
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { (route, label) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { /* Add icons here if needed */ },
                label = { Text(label) }
            )
        }
    }
}
