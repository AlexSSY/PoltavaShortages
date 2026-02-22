package com.alex.ps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alex.ps.domain.Settings
import com.alex.ps.data.SettingsRepositoryImpl
import com.alex.ps.domain.SettingsRepository
import com.alex.ps.domain.ThemeSetting
import com.alex.ps.ui.Screen
import com.alex.ps.ui.HomeScreen
import com.alex.ps.ui.LanguageScreen
import com.alex.ps.ui.PreferencesScreen
import com.alex.ps.ui.QueueScreen
import com.alex.ps.ui.ThemeScreen
import com.alex.ps.ui.TomorrowScheduleScreen
import com.alex.ps.ui.theme.AppTheme
import org.koin.android.ext.android.get

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

            val settingsDataStore: SettingsRepository = get()
            val settings by settingsDataStore.settingsFlow.collectAsState(initial = Settings.default())

            val isDarkTheme = when(settings.theme) {
                ThemeSetting.DARK -> true
                ThemeSetting.LIGHT -> false
                ThemeSetting.SYSTEM -> isSystemInDarkTheme()
            }

            AppTheme(isDarkTheme) {
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
                                onThemeClick = {
                                    navController.navigate(Screen.ThemeScreen.route)
                                },
                                onQueueClick = {
                                    navController.navigate(Screen.QueueScreen.route)
                                },
                                onLanguageClick = {
                                    navController.navigate(Screen.LanguageScreen.route)
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

                        composable(Screen.ThemeScreen.route) {
                            ThemeScreen(
                                onThemeSelected = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Screen.QueueScreen.route) {
                            QueueScreen(
                                onLanguageSelected = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Screen.LanguageScreen.route) {
                            LanguageScreen(
                                onLanguageSelected = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}