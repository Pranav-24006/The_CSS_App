package com.example.thecssapp.ui.screens.events.eventdetails
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.thecssapp.model.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    navController: NavController
) {
    // For now, fetch from static list. Later replace with repository or DB.
    val allEvents = listOf(
        Event(1, "Tech Talk 2025", "https://picsum.photos/300", "Technical"),
        Event(2, "CSS Fest", "https://picsum.photos/301", "Fest"),
        Event(3, "Design Workshop", "https://picsum.photos/302", "Workshop"),
        Event(4, "Startup Meetup", "https://picsum.photos/303", "Networking")
    )
    val event = allEvents.firstOrNull { it.id == eventId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event?.title ?: "Event Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        event?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = it.imageUrl,
                    contentDescription = it.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Tabs
                var selectedTab by remember { mutableStateOf(0) }
                val tabs = listOf("About", "Participants", "Location")

                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(tab) }
                        )
                    }
                }

                when (selectedTab) {
                    0 -> Text(
                        "This week, ${it.title} will be happening! Don't miss out.",
                        modifier = Modifier.padding(16.dp)
                    )
                    1 -> Text("List of participants (dummy)", modifier = Modifier.padding(16.dp))
                    2 -> Text("Location details (dummy)", modifier = Modifier.padding(16.dp))
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { /* buy ticket */ },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Buy tickets for $79")
                }

                OutlinedButton(
                    onClick = { /* save for later */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Save for later")
                }
            }
        }
    }
}
