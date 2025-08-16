package com.example.thecssapp.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecssapp.R
import com.example.thecssapp.constants.Routes
import com.example.thecssapp.data.StudentProfileDataStore
import com.example.thecssapp.model.StudentProfile
import com.example.thecssapp.ui.theme.TheCSSAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, dataStore: StudentProfileDataStore) {
    var menuExpanded by remember { mutableStateOf(false) }
    val profile by dataStore.getProfile().collectAsState(initial = StudentProfile())

    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.css_nit_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.padding(start = 8.dp).size(80.dp)
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Profile") },
                                onClick = {
                                    navController.navigate(Routes.PROFILE)
                                    menuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("About Us") },
                                onClick = {
                                    navController.navigate(Routes.ABOUT_US)
                                    menuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Credits") },
                                onClick = {
                                    navController.navigate(Routes.CREDITS)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            ) {
                Text("Welcome, ${profile.name}!", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Your gateway to CSE department resources", color = MaterialTheme.colorScheme.onPrimary)

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Next Event", fontWeight = FontWeight.SemiBold)
                                Text("Tech Talk: AI in Education", fontSize = 14.sp)
                                Text("Tomorrow, 4:00 PM", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }

            Text(
                "Quick Access",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                QuickAccessButton("Attendance", Icons.Default.AssignmentTurnedIn) {
                    navController.navigate(Routes.ATTENDANCE)
                }
                QuickAccessButton("Events", Icons.Default.Event) {
                    navController.navigate(Routes.EVENTS)
                }
                QuickAccessButton("Planner", Icons.Default.Checklist) {
                    navController.navigate(Routes.PLANNER)
                }
            }

            SectionHeader("Upcoming Events") {
                navController.navigate(Routes.EVENTS)
            }

            EventCard("24 Jun", "Tech Talk: AI in Education", "4:00 PM - 6:00 PM • Auditorium", "Workshop")
            EventCard("28 Jun", "Coding Competition", "10:00 AM - 2:00 PM • Lab 204", "Competition")

            SectionHeader("Faculty Spotlight")

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Faculty",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Dr. Robert Chen", fontWeight = FontWeight.Bold)
                        Text("Associate Professor, AI & Machine Learning", fontSize = 12.sp)
                        Text("Office: Mon & Wed 2–4 PM, Room 302", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}


@Composable
fun QuickAccessButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier
                .size(60.dp)
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun EventCard(date: String, title: String, timePlace: String, tag: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column(
                modifier = Modifier
                    .width(56.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(8.dp)).padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(date.split(" ")[0], fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                Text(date.split(" ")[1], fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(timePlace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(tag, fontSize = 10.sp, color = Color.White, modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onViewAllClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        if (onViewAllClick != null) {
            Text(
                text = "View All",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview(){
    TheCSSAppTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        // We pass a dummy DataStore for the preview to work
        HomeScreen(
            navController = rememberNavController(),
            dataStore = StudentProfileDataStore(LocalContext.current)
        )
    }
}
