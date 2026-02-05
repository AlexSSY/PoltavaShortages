package com.alex.ps.ui

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Preferences : Screen("preferences")
}