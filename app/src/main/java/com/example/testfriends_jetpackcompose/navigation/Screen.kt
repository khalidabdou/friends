package com.example.testfriends_jetpackcompose.navigation

sealed class Screen (val route :String) {
    object Welcome : Screen(route = "welcome_screen")
    object Home :Screen(route = "Home_screen")
    object Create : Screen(route = "Create_screen")
    object Results :Screen(route = "Results_screen")
    object ShareTest :Screen(route = "Share_screen")
    object LoginScreen :Screen(route = "Login_screen")

}