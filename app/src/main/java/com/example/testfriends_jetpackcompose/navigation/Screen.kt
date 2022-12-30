package com.example.testfriends_jetpackcompose.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen(route = "welcome_screen")
    object Home : Screen(route = "Home_screen")
    object Create : Screen(route = "Create_screen")
    object Test : Screen(route = "test")
    object ShareTest : Screen(route = "Share_screen")
    object LoginScreen : Screen(route = "Login_screen")
    object ResultsScreen : Screen(route = "results_screen")
    object FinalScreen : Screen(route = "Final_screen")
    object Answer : Screen(route = "Answer_screen")
}