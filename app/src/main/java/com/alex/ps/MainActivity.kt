package com.alex.ps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

            AppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Today Schedule",
                                    style = AppTheme.typography.titleMedium
                                )
                            },
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