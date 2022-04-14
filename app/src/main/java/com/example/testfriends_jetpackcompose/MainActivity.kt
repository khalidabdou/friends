package com.example.testfriends_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.navigation.SetupNavGraph
import com.example.testfriends_jetpackcompose.ui.theme.TestFriends_JetPackComposeTheme
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.ResultsViewModel
import com.example.testfriends_jetpackcompose.viewmodel.SplashViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    //var splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen().setKeepOnScreenCondition {
//            !splashViewModel.isLoading.value
//        }

        setContent {
            TestFriends_JetPackComposeTheme {
                //val screen by splashViewModel.startDestination
                val navController = rememberNavController()
                val viewModel: CreateTestViewModel = CreateTestViewModel(LocalContext.current)

                SetupNavGraph(navController = navController, startDestination = Screen.Welcome.route, viewModel)
            }
        }
    }
}
