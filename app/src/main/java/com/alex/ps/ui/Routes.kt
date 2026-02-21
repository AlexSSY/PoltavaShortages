package com.alex.ps.ui

sealed class Screen(
    val route: String,
    val title: String
) {
    data object Main : Screen("main", "Today Schedule")
    data object Preferences : Screen("preferences", "Preferences")
    data object TomorrowSchedule : Screen("tomorrowSchedule", "Tomorrow Schedule")
    data object ThemeScreen : Screen("theme", "Select Theme")
    data object QueueScreen : Screen("queue", "Select Queue")
}