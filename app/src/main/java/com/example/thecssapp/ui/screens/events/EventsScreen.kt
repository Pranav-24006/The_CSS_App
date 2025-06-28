package com.example.thecssapp.ui.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.ui.theme.TheCSSAppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {
    val events = listOf(
        Event("Tech Talk: AI in Education","24 Jun", "4:00 PM - 6:00 PM • Auditorium", "Workshop"),
        Event("Tech Talk: AI in Education","24 Jun", "4:00 PM - 6:00 PM • Auditorium", "Workshop"),
        Event("Tech Talk: AI in Education","24 Jun", "4:00 PM - 6:00 PM • Auditorium", "Workshop")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CSS Events") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(events) { event ->
                    EventCard(event)
                }
            }
        }
    )
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Left: Date box
            Column(
                modifier = Modifier
                    .width(56.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val parts = event.date.split(" ")
                val day = parts.getOrNull(0) ?: "--"
                val month = parts.getOrNull(1) ?: "--"

                Text(
                    day,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    month,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right: Details
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.SemiBold)
                Text(
                    event.timePlace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tag as a styled label
                    Text(
                        event.tag,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    // Register Button
                    Button(
                        onClick = { /* TODO: Handle registration */ },
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Register", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}



data class Event(
    val title: String,
    val date: String,       // e.g., "01 Jul"
    val timePlace: String,  // e.g., "10 AM • Lab 3"
    val tag: String         // e.g., "Hackathon"
)


@Preview(showSystemUi=true)
@Composable
fun EventsPrev(){
    val navController = rememberNavController()
    TheCSSAppTheme(darkTheme = true, dynamicColor = false) {
        EventsScreen(navController)
    }
}
