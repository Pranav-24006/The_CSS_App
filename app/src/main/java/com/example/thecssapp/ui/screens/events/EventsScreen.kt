package com.example.thecssapp.ui.screens.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.thecssapp.ui.theme.TheCSSAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {
    val events = listOf(
        Event(
            1, "The Weeknd", "Nov 21", "6:00 PM", "Auditorium", "Concert",
            "https://link-to-weeknd-poster.jpg",
            "This week, Abel comes back to California to perform his newest studio album..."
        ),
        Event(
            2, "Ariana Grande", "Dec 3", "8:00 PM", "Stadium", "Concert",
            "https://link-to-ariana-poster.jpg",
            "Experience Ariana's magical voice live on stage..."
        )
    )

    val galleryImages = listOf(
        "https://link-to-past-event1.jpg",
        "https://link-to-past-event2.jpg",
        "https://link-to-past-event3.jpg",
        "https://link-to-past-event4.jpg"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore events", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search events") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            // Popular section
            Text(
                "Popular",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(events) { event ->
                    PopularEventCard(event) {
                        navController.navigate("eventDetail/${event.id}")
                    }
                }
            }

            // Gallery section
            Text(
                "Gallery",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(galleryImages) { imageUrl ->
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = "Past event photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Categories section
            Text(
                "Categories",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryChip("Concerts")
                CategoryChip("Movies")
                CategoryChip("Exhibitions")
            }
        }
    }
}


@Composable
fun PopularEventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp)
            .padding(end = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(event.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
            Text(event.date, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun CategoryChip(name: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        modifier = Modifier.clickable { }
    ) {
        Text(name, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = MaterialTheme.colorScheme.primary)
    }
}
@Composable
fun GallerySection(galleryImages: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Gallery",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(galleryImages) { imageUrl ->
                Card(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = "Past event photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}




data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val category: String,
    val imageUrl: String,
    val description: String
)



@Preview(showSystemUi=true)
@Composable
fun EventsPrev(){
    val navController = rememberNavController()
    TheCSSAppTheme(darkTheme = true, dynamicColor = false) {
        EventsScreen(navController)
    }
}
