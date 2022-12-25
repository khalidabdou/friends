package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel


@Composable
fun FinalScreen(navHostController: NavHostController, viewModel: AnswerTestViewModel) {
    val scaffoldState = rememberScaffoldState()

    val sender = SENDER!!
    val result = viewModel.result
    val questions = viewModel.questions
    //sender = SENDER!!

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
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
                    Avatar(sender.username, enableText = true)
                    Image(
                        painter = painterResource(id = R.drawable.love),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Avatar(ME!!.username, enableText = true)
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
                    val results = questions.filter { q -> q.realAnswer.text == q.answerSender }.size
                    Text(
                        text = "$results/${questions.size}",
                        color = darkGray,
                        style = MaterialTheme.typography.h1
                    )
                }
            }
            LazyColumn(modifier = Modifier.padding(6.dp)) {

                Log.d("ANSWER", "${questions[15]}")
                items(questions.size) {
                    var imgUrl = "${Constant.BASE_URL}english/$it/${questions[it].realAnswer.img}"
                    if (questions[it].realAnswer.img == "")
                        imgUrl = "${Constant.BASE_URL}english/3/4.png"
                    val emoji = if (questions[it].realAnswer.text == questions[it].answerSender) {
                        "❎ "
                    } else {
                        "❌ "
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .background(backgroundWhite),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = questions[it].question + " $emoji",
                            color = darkGray,
                            modifier = Modifier.weight(4f)
                        )

                        AsyncImage(
                            model = imgUrl, contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)

                        )
                    }

                }
            }
        }
    }

    BackHandler {
        navHostController.navigate(Screen.Home.route) {
            navHostController.popBackStack()
        }
    }

}

@Composable
fun Friend(user: String, textColor: Color = darkGray) {
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
        Text(text = user, color = textColor)
    }
}

