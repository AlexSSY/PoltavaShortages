package com.alex.ps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alex.ps.data.settings.SettingsDataStore
import com.alex.ps.ui.Screen
import com.alex.ps.ui.composables.CircularSegmentProgressBar
import com.alex.ps.ui.composables.ElectricityAvailableWidget
import com.alex.ps.ui.composables.SettingsDrawer
import com.alex.ps.ui.composables.SummaryWidget
import com.alex.ps.ui.composables.TimePeriodPresentation
import com.alex.ps.ui.composables.TimePeriodPresentationState
import com.alex.ps.ui.composables.TimerWidget
import com.alex.ps.ui.composables.ToastWidget
import com.alex.ps.ui.screens.HomeScreen
import com.alex.ps.ui.screens.PreferencesScreen
import com.alex.ps.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
//    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val navController = rememberNavController()

            AppTheme {
                Text("fdfdf")
//                Scaffold(
//                    topBar = {
//                        TopAppBar(
//                            title = {
//                                Text(
//                                    text = "Today Schedule",
//                                    style = AppTheme.typography.titleMedium
//                                )
//                            },
//                        )
//                    },
//                ) { innerPadding ->
//                    NavHost(
//                        modifier = Modifier.padding(innerPadding),
//                        navController = navController,
//                        startDestination = Screen.Main.route
//                    ) {
//                        composable(Screen.Main.route) {
//                            HomeScreen(
//                                onOpenPreferences = {
//                                    navController.navigate(Screen.Preferences.route)
//                                }
//                            )
//                        }
//
//                        composable(Screen.Preferences.route) {
//                            PreferencesScreen(
//                                onBack = {
//                                    navController.popBackStack()
//                                }
//                            )
//                        }
//                    }
//                }
            }
        }
    }
}