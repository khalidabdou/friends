package com.example.testfriends_jetpackcompose.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun TestMain(navHostController: NavHostController, viewModel: CreateTestViewModel) {
    val context = LocalContext.current
    var index = viewModel.index
    var questions = viewModel.questions
    var username = ME!!.username

    if (!ME!!.myQuestions.isNullOrEmpty()) {
        questions = Utils.stringToQuestionArrayList(ME!!.myQuestions).toCollection(ArrayList())
    }

    fun setRealAnswer(answer: AnswerElement) {
        viewModel.setAnswerRealAnswer(answer)
        if (viewModel.incrementIndex()) {
            navHostController.navigate(Screen.ShareTest.route)
        }
    }
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            ActionBar("${index + 1}/${questions.size}")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it),
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = questions[index].question.replace("****", username),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                CardAnswer(
                    index = index,
                    answer = questions[index].answer1,
                ) { answer ->
                    setRealAnswer(answer)
                }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = questions[index].answer2,
                ) { answer ->
                    setRealAnswer(answer = answer)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardAnswer(
                    index = index,
                    answer = questions[index].answer3,
                ) { answer ->

                    setRealAnswer(answer = answer)
                }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = questions[index].answer4,

                    ) { answer ->
                    setRealAnswer(answer = answer)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.Gray.copy(
                                0.1f
                            )
                        )
                        .clickable {
                            viewModel.decrementIndex()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_before),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.Gray.copy(
                                0.1f
                            )
                        )
                        .clickable {
                            //Log.d("Answer", questions[index].realAnswer.text + " index$index")
                            if (questions[index].realAnswer.text != "")
                                if (viewModel.incrementIndex()) {
                                    navHostController.navigate(Screen.FinalScreen.route)
                                }
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                        )
                }
            }
        }
    }

    BackHandler {
        navHostController.navigate(Screen.Home.route) {
            popUpTo(Screen.FinalScreen.route) {
                inclusive = true
            }
        }
    }
}


