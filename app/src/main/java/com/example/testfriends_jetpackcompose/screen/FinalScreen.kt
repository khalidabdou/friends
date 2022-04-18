package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun FinalScreen(navHostController: NavHostController, viewModel: CreateTestViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(backgroundColor = Color.White, onClick = {
            }) {
                Image(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    ) {
        var myAnswers = ""
        for (item in viewModel.question) {
            myAnswers += item.realAnswer + ","
        }
        val results = Utils.compareResults(SENDER!!.myQuestions, myAnswers)
        viewModel.createResults(SENDER!!.id, ME!!.id, myAnswers, SENDER!!.token)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundWhite)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                        .background(darkGray),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Friend("abdellah khalid")
                    Image(
                        painter = painterResource(id = R.drawable.love),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Friend("mohamed mod")
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .align(Alignment.BottomEnd), contentAlignment = Alignment.Center
                ) {
                    CustomComponent(
                        canvasSize = 80.dp,
                        indicatorValue = results,
                        maxIndicatorValue = 100,
                        backgroundIndicatorStrokeWidth = 25f,
                        foregroundIndicatorStrokeWidth = 25f, smallText = ""
                    )
                }

            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Image(
                    painter = painterResource(id = R.drawable.qr),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            Color.White
                        )
                        .padding(10.dp)
                )
            }

        }
    }


}

@Composable
fun Friend(user: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .clip(
                    CircleShape
                )
        )
        Text(text = user, style = MaterialTheme.typography.h3, color = Color.White)
        CopyId()
    }
}