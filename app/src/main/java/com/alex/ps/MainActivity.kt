package com.alex.ps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alex.ps.ui.Screen
import com.alex.ps.ui.screens.HomeScreen
import com.alex.ps.ui.screens.PreferencesScreen
import com.alex.ps.ui.screens.TomorrowScheduleScreen
import com.alex.ps.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val currentScreen = listOf(
                Screen.Main,
                Screen.TomorrowSchedule,
                Screen.Preferences
            ).find { it.route == currentRoute }

            val title = currentScreen?.title.orEmpty()

            val canGoBack = navController.previousBackStackEntry != null

            AppTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = title,
                                    style = AppTheme.typography.titleMedium
                                )
                            },
                            navigationIcon = {
                                if (canGoBack) {
                                    IconButton(
                                        onClick = { navController.popBackStack() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        navController.navigate(Screen.Preferences.route)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Preferences"
                                    )
                                }
                            }
                        )
                    },
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Main.route
                    ) {
                        composable(Screen.Main.route) {
                            HomeScreen(
                                onClickTomorrowScheduleLink = {
                                    navController.navigate(Screen.TomorrowSchedule.route)
                                }
                            )
                        }

                        composable(Screen.Preferences.route) {
                            PreferencesScreen(
                                onBack = {
                                    navController.navigate(Screen.Main.route)
                                }
                            )
                        }

                        composable(Screen.TomorrowSchedule.route) {
                            TomorrowScheduleScreen(
                                onBack = {
                                    navController.navigate(Screen.Main.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}