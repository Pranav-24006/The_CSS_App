package com.example.thecssapp.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.R
import com.example.thecssapp.data.StudentProfileDataStore
import com.example.thecssapp.ui.theme.TheCSSAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(navController: NavController, dataStore: StudentProfileDataStore) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fade-in animation
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )

        delay(2000) // keep splash visible for a bit

        val profile = dataStore.getProfile().first()
        if (profile.name.isNotBlank()) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.css_nit_logo), // Replace with your logo file name
            contentDescription = "App Logo",
            modifier = Modifier.alpha(alpha.value)
        )
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // In preview, we'll just create a temporary data store.
    // This won't actually persist anything in preview mode.
    val previewDataStore = remember {
        StudentProfileDataStore(context)
    }

    TheCSSAppTheme(darkTheme = true) {
        SplashScreen(
            navController = navController,
            dataStore = previewDataStore
        )
    }
}
