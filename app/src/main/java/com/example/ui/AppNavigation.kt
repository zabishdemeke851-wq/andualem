package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*

data class NavTabItem(
    val screen: AppScreen,
    val titleEn: String,
    val titleAm: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isAmharic by viewModel.isAmharic.collectAsState()

    val navTabs = remember {
        listOf(
            NavTabItem(AppScreen.DASHBOARD, "Home", "መነሻ", Icons.Default.Home),
            NavTabItem(AppScreen.CALENDAR, "Calendar", "ቀን", Icons.Default.CalendarMonth),
            NavTabItem(AppScreen.ABUSHAKIR, "Abushakir", "ባሕረ ሐሳብ", Icons.Default.Calculate),
            NavTabItem(AppScreen.LIBRARY, "Library", "ቤተ-መጽሐፍት", Icons.Default.MenuBook),
            NavTabItem(AppScreen.AI_RESEARCH, "Andualem AI", "AI", Icons.Default.AutoAwesome),
            NavTabItem(AppScreen.SAINTS_FEASTS, "Feasts", "በዓላት", Icons.Default.Church)
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isAmharic) "አንዱዓለም" else "ANDUALEM",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    TextButton(onClick = { viewModel.toggleLanguage() }) {
                        Text(
                            text = if (isAmharic) "EN" else "አማ",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navTabs.forEach { tab ->
                    val isSelected = currentScreen == tab.screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateTo(tab.screen) },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.titleEn
                            )
                        },
                        label = {
                            Text(
                                text = if (isAmharic) tab.titleAm else tab.titleEn,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                AppScreen.CALENDAR -> CalendarScreen(viewModel = viewModel)
                AppScreen.CONVERTER -> ConverterScreen(viewModel = viewModel)
                AppScreen.ABUSHAKIR -> AbushakirScreen(viewModel = viewModel)
                AppScreen.TIME -> TimeScreen(viewModel = viewModel)
                AppScreen.LIBRARY -> LibraryScreen(viewModel = viewModel)
                AppScreen.READER -> ReaderScreen(viewModel = viewModel)
                AppScreen.AI_RESEARCH -> AiResearchScreen(viewModel = viewModel)
                AppScreen.SAINTS_FEASTS -> SaintsAndFeastsScreen(viewModel = viewModel)
                AppScreen.FAVORITES -> FavoritesScreen(viewModel = viewModel)
                AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
