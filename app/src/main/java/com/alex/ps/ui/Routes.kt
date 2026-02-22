package com.alex.ps.ui

import com.alex.ps.R

sealed class Screen(
    val route: String,
    val titleStringId: Int
) {
    data object Main : Screen("main", R.string.today_schedule)
    data object Preferences : Screen("preferences", R.string.today_schedule)
    data object TomorrowSchedule : Screen("tomorrowSchedule", R.string.today_schedule)
    data object ThemeScreen : Screen("theme", R.string.today_schedule)
    data object QueueScreen : Screen("queue", R.string.today_schedule)
    data object LanguageScreen : Screen("language", R.string.today_schedule)
}