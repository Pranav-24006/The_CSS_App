package com.example.thecssapp.ui.screens.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.thecssapp.constants.Routes
import com.example.thecssapp.ui.screens.attendance.AttendanceScreen
import com.example.thecssapp.ui.screens.planner.PlannerScreen
import com.example.thecssapp.ui.theme.TheCSSAppTheme

data class Event(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {
    // Sample Data
    val allEvents = remember {
        listOf(
            Event(1, "Tech Talk 2025", "https://picsum.photos/300", "Technical"),
            Event(2, "CSS Fest", "https://picsum.photos/301", "Fest"),
            Event(3, "Design Workshop", "https://picsum.photos/302", "Workshop"),
            Event(4, "Startup Meetup", "https://picsum.photos/303", "Networking")
        )
    }
    val galleryImages = remember {
        listOf(
            "https://picsum.photos/400",
            "https://picsum.photos/401",
            "https://picsum.photos/402"
        )
    }
    val categories = listOf("All", "Technical", "Workshop", "Fest", "Networking")

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredEvents = allEvents.filter { event ->
        (selectedCategory == "All" || event.category == selectedCategory) &&
                event.title.contains(searchQuery.text, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search events") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gallery
            Text("Gallery", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            GallerySection(galleryImages)

            Spacer(modifier = Modifier.height(16.dp))

            // Categories
            Text("Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Popular Events
            Text("Popular Events", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredEvents) { event ->
                    PopularEventCard(event) {
                        navController.navigate("eventDetail/${event.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun GallerySection(galleryImages: List<String>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(galleryImages) { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Gallery Image",
                modifier = Modifier
                    .size(120.dp)
                    .clickable { /* open gallery detail */ }
            )
        }
    }
}

@Composable
fun CategoryChip(category: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(category) }
    )
}

@Composable
fun PopularEventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventPrev() {
    val navController = rememberNavController()
    // Pass a safe static dataset for preview mode
    CompositionLocalProvider {
        TheCSSAppTheme(darkTheme = true, dynamicColor = false) {
            EventsScreen(navController)
        }
    }
}
