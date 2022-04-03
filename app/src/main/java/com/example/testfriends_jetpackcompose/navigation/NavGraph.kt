package com.example.testfriends_jetpackcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable

import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testfriends_jetpackcompose.screen.*
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    viewModel: CreateTestViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Create.route) {
            TestMain(navHostController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Results.route) {
            ResultsFriends()
        }
        composable(route = Screen.ShareTest.route) {
            ShareTest(viewModel=viewModel)
        }
    }
}