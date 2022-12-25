package com.example.testfriends_jetpackcompose.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel

@Composable
fun AnswerTestMain(navHostController: NavHostController, viewModel: AnswerTestViewModel) {
    var index = viewModel.index
    val context = LocalContext.current
    val questions = Utils.stringToQuestionArrayList(SENDER!!.myQuestions)
    if (viewModel.questions.isNullOrEmpty())
        viewModel.questions = questions.toMutableStateList()
    val username = ME!!.username
    //Toast.makeText(context, "reql", Toast.LENGTH_LONG).show()
    fun setRealAnswer(answer: String) {
        viewModel.setAnswer(answer = answer)
        questions[index].answerSender = answer
        //viewModel.questions[index].answerSender = "kkkkkk"
        Log.d("ANSWER", "${questions[index].answerSender} + $answer")
        if (index > 0)
            Log.d("ANSWER", "${questions[index - 1].answerSender} + $answer")
        if (viewModel.incrementIndex()) {
            Log.d("ANSWER", "${questions[index]}")
            navHostController.navigate(Screen.FinalScreen.route)
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
                        text = questions[index].question.replace("****", username),
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
                    answer = questions[index].answer1,
                    realAnswer = questions[index].realAnswer,

                    ) { answer ->
                    setRealAnswer(answer)
                }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = questions[index].answer2,
                    realAnswer = questions[index].realAnswer,
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
                    realAnswer = questions[index].realAnswer,
                ) { answer ->

                    setRealAnswer(answer = answer)
                }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = questions[index].answer4,
                    realAnswer = questions[index].realAnswer,
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
                            )
                        )
                        .clickable {
                            Log.d("Answer", questions[index].realAnswer.text + " index$index")
                            if (questions[index].realAnswer.text != "")
                                if (viewModel.incrementIndex()) {

                                    navHostController.navigate("Final_screen")
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


@Composable
fun ActionBar(index: String) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .padding(20.dp)

    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    backgroundWhite
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_before),
                contentDescription = "",
                tint = darkGray,
                modifier = Modifier
                    .size(30.dp)
                    .align(
                        Alignment.Center
                    )
                    .clickable {
                        onBackPressedDispatcher?.onBackPressed()
                    }
            )
        }
        if (SENDER != null)
            Text(
                text = "answer for ${SENDER!!.username} questions ",
                style = MaterialTheme.typography.body1,
                color = darkGray
            )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    backgroundWhite
                )
        ) {
            Text(
                textAlign = TextAlign.Justify,
                text = index,
                color = darkGray,
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CardAnswer(
    index: Int,
    answer: AnswerElement,
    width: Dp = 150.dp,
    height: Dp = 150.dp,
    imageSize: Dp = 70.dp,
    realAnswer: AnswerElement,
    onClickAnswer: (String) -> Unit
) {


    var imgUrl = "${Constant.BASE_URL}english/$index/${answer.img}"
    if (answer.img == "")
        imgUrl = "${Constant.BASE_URL}english/3/4.png"
    Column(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    backgroundWhite
                )
                .clickable {


                    onClickAnswer(answer.text)


                }
        ) {

            AsyncImage(
                model = imgUrl, contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .align(Alignment.Center)
            )
            Text(
                style = MaterialTheme.typography.body1,
                fontSize = 16.sp,
                text = answer.text,
                color = darkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .wrapContentHeight(Alignment.Bottom)

            )
        }
    }

}