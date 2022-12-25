package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun TestMain(navHostController: NavHostController, viewModel: CreateTestViewModel) {
    val context= LocalContext.current
    var index = viewModel.index
    var question = viewModel.question
    var username = ME!!.username
    if (SENDER != null){
        username = SENDER!!.username
        viewModel.question = Utils.stringToQuestionArrayList(SENDER!!.myQuestions)
        //Log.d("CHAAA",SENDER!!.myQuestions)
        //Toast.makeText(context,question.size.toString(),Toast.LENGTH_LONG).show()
    }


    fun setRealAnswer(answer: String) {
        if (SENDER == null){
            viewModel.setAnswer(answer = answer)
        }else{
            viewModel.setAnswerSender(answer)
        }
        if (viewModel.incrementIndex()) {
            if (SENDER == null) {
                navHostController.navigate("Share_screen")
            } else {
                navHostController.navigate(Screen.FinalScreen.route) {
                    popUpTo(Screen.Create.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            ActionBar("${index + 1}/${question.size}")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
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
                    Text(
                        text = viewModel.questions[index].question.replace("****", username),
                        style = MaterialTheme.typography.body1,
                        color = darkGray,
                        fontSize = 22.sp,
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
                    answer = question[index].answer1,
                    realAnswer = question[index].realAnswer,

                    ) { answer -> setRealAnswer(answer) }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = question[index].answer2,
                    realAnswer = question[index].realAnswer,
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
                    answer = question[index].answer3,
                    realAnswer = question[index].realAnswer,
                ) { answer ->
                    setRealAnswer(answer = answer)
                }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = question[index].answer4,
                    realAnswer = question[index].realAnswer,
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
                        .background(Color.Gray.copy(
                            0.1f
                        ))
                        .clickable {
                            viewModel.decrementIndex()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_before),
                        contentDescription = ""
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
                        ))
                        .clickable {
                            Log.d("Answer", question[index].realAnswer.text + " index$index")
                            if (question[index].realAnswer.text != "")
                                if (viewModel.incrementIndex()) {
                                    if (SENDER == null)
                                        navHostController.navigate("Share_screen")
                                    else navHostController.navigate("Final_screen")
                                }
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = ""
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


