package com.example.testfriends_jetpackcompose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.OnBoardingPage
import com.google.accompanist.pager.*


@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun WelcomeScreen(
    navController: NavHostController,
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }
        Row(
            verticalAlignment=Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
            .weight(1f).padding(10.dp),
            ) {
            HorizontalPagerIndicator(
                modifier=Modifier.weight(5f),
                pagerState = pagerState
            )
            AnimatedVisibility(
                modifier = Modifier.size(50.dp).weight(1f).clip(CircleShape),
                visible = pagerState.currentPage == 2
            ) {
                Box(
                    contentAlignment= Alignment.Center,
                    modifier = Modifier
                    .size(50.dp)

                    .background(Black)
                    .clickable {
                        //welcomeViewModel.saveOnBoardingState(completed = true)
                        navController.popBackStack()
                        navController.navigate(Screen.LoginScreen.route)

                    }

                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_next), tint = White,contentDescription = "")
                }
            }
        }
    }
}


@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onBoardingPage.title,
            fontSize = MaterialTheme.typography.h1.fontSize,
            fontWeight = FontWeight.Bold,
            color= Color.Black.copy(0.8f),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = onBoardingPage.description,
            style = TextStyle()
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun FinishButton(
    pagerState: PagerState,
    onClick: () -> Unit
) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            visible = pagerState.currentPage == 2
        ) {
          Box(modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(Black)){
              Icon(painter = painterResource(id = R.drawable.ic_next), contentDescription = "")
          }
        }

}

@Composable
@Preview(showBackground = true)
fun FirstOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}

@Composable
@Preview(showBackground = true)
fun SecondOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Second)
    }
}