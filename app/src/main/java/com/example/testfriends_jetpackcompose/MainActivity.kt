package com.example.testfriends_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.testfriends_jetpackcompose.navigation.SetupNavGraph
import com.example.testfriends_jetpackcompose.ui.theme.TestFriends_JetPackComposeTheme
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.SplashViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }

        setContent {
            TestFriends_JetPackComposeTheme {

                //val screen by splashViewModel.startDestination
                val navController = rememberNavController()
                val viewModel: CreateTestViewModel = hiltViewModel()
                val viewModel2: AnswerTestViewModel =
                    ViewModelProvider(this).get(AnswerTestViewModel::class.java)
                //var startDestination = splashViewModel.startDestination.value
                SetupNavGraph(
                    navController = navController,
                    startDestination = splashViewModel.startDestination.value,
                    viewModel,
                    viewModel2
                )
            }
        }
    }
}
