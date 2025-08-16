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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thecssapp.constants.Routes
import com.example.thecssapp.data.AppDatabase
import com.example.thecssapp.data.StudentProfileDataStore
import com.example.thecssapp.model.StudentProfile
import com.example.thecssapp.ui.components.BottomNavBar
import com.example.thecssapp.ui.screens.about.AboutUsScreen
import com.example.thecssapp.ui.screens.attendance.AddCourseScreen
import com.example.thecssapp.ui.screens.attendance.AttendanceScreen
import com.example.thecssapp.ui.screens.attendance.UpdateAttendanceScreen
import com.example.thecssapp.ui.screens.credits.CreditsScreen
import com.example.thecssapp.ui.screens.events.EventsScreen
import com.example.thecssapp.ui.screens.events.eventdetails.EventDetailScreen
import com.example.thecssapp.ui.screens.home.HomeScreen
import com.example.thecssapp.ui.screens.onboarding.OnboardingScreen
import com.example.thecssapp.ui.screens.planner.PlannerScreen
import com.example.thecssapp.ui.screens.planner.addschedule.AddScheduleScreen
import com.example.thecssapp.ui.screens.profile.ProfileScreen
import com.example.thecssapp.ui.screens.splash.SplashScreen
import com.example.thecssapp.ui.theme.TheCSSAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getDatabase(this)
        val scheduleDao = db.scheduleDao()
        val courseDao = db.courseDao()
        val profileDataStore = StudentProfileDataStore(applicationContext)

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val scope = rememberCoroutineScope()

            TheCSSAppTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                Scaffold(
                    bottomBar = {
                        val updateAttendanceBaseRoute = Routes.UPDATE_ATTENDANCE.substringBefore("/{")
                        val noBottomBarRoutes = listOf(
                            Routes.HOME, Routes.SPLASH, Routes.ONBOARDING, Routes.ADD_COURSE,
                            Routes.PROFILE, Routes.ABOUT_US, Routes.CREDITS
                        )
                        if (currentRoute !in noBottomBarRoutes &&
                            currentRoute?.startsWith(updateAttendanceBaseRoute) != true
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
                            composable(Routes.SPLASH) { SplashScreen(navController = navController, dataStore = profileDataStore) }
                            composable(Routes.ONBOARDING) { OnboardingScreen(navController = navController) }
                            composable(Routes.HOME) {
                                HomeScreen(navController, profileDataStore)
                            }
                            composable(Routes.EVENTS) { EventsScreen(navController) }
                            composable(Routes.PLANNER) { PlannerScreen(navController, scheduleDao) }
                            composable(Routes.EVENT_DETAILS) { backStackEntry ->
                                val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull() ?: -1
                                EventDetailScreen(eventId, navController)
                            }
                            composable(Routes.ADD_SCHEDULE) { AddScheduleScreen(navController, scheduleDao) }
                            composable(Routes.ATTENDANCE) {
                                val profile by profileDataStore.getProfile().collectAsState(initial = StudentProfile())
                                val courses by courseDao.getCourses().collectAsState(initial = emptyList())
                                AttendanceScreen(
                                    navController = navController,
                                    userName = profile.name,
                                    courses = courses,
                                    onAddClick = { navController.navigate(Routes.ADD_COURSE) },
                                    onDeleteCourse = { course -> scope.launch { courseDao.deleteCourse(course) } },
                                    onCourseClick = { course ->
                                        navController.navigate(Routes.UPDATE_ATTENDANCE.replace("{courseId}", course.id.toString()))
                                    }
                                )
                            }
                            composable(Routes.ADD_COURSE) {
                                AddCourseScreen(
                                    onDismiss = { navController.popBackStack() },
                                    onAddCourse = { course ->
                                        scope.launch {
                                            courseDao.addCourse(course)
                                            navController.popBackStack()
                                        }
                                    }
                                )
                            }
                            composable(
                                route = Routes.UPDATE_ATTENDANCE,
                                arguments = listOf(navArgument("courseId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val courseId = backStackEntry.arguments?.getLong("courseId") ?: -1L
                                if (courseId != -1L) {
                                    UpdateAttendanceScreen(
                                        navController = navController,
                                        courseFlow = courseDao.getCourseById(courseId),
                                        onUpdate = { course -> scope.launch { courseDao.updateCourse(course) } }
                                    )
                                }
                            }

                            composable(Routes.PROFILE) {
                                ProfileScreen(navController, profileDataStore)
                            }
                            composable(Routes.ABOUT_US) { AboutUsScreen(navController) }
                            composable(Routes.CREDITS) { CreditsScreen(navController) }
                        }
                    }
                }
            }
        }
    }
}
