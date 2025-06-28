package com.example.thecssapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.ui.screens.attendance.AttendanceScreen
import com.example.thecssapp.ui.screens.home.HomeScreen
import com.example.thecssapp.ui.theme.TheCSSAppTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                val navController = rememberNavController()
                TheCSSAppTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController) }
                        composable("attendance") { AttendanceScreen(navController) }
                        // Add more screens here
                    }
                }
        }
    }
}
