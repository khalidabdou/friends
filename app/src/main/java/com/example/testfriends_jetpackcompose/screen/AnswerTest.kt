package com.example.testfriends_jetpackcompose.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerTestMain(navHostController: NavHostController, viewModel: AnswerTestViewModel) {
    var index = viewModel.index

    if (viewModel.sender.value?.data == null && SENDER != null) {
        viewModel.sender.value?.data = SENDER
    }
    val sender = viewModel.sender.value!!.data!!
    val questions = Utils.stringToQuestionArrayList(sender.myQuestions)
    if (viewModel.questions.isEmpty()) {
        viewModel.questions = questions.toMutableStateList()
    }

    //val username = ME!!.username
    //Toast.makeText(context, "reql", Toast.LENGTH_LONG).show()
    fun setRealAnswer(answer: AnswerElement) {
        viewModel.setAnswer(answer = answer)
        if (viewModel.incrementIndex()) {
            navHostController.navigate(Screen.FinalScreen.route)
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
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
                    Text(
                        text = questions[index].question.replace("****", sender.username),
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
                            //Log.d("Answer", questions[index].realAnswer.text + " index$index")
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
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .padding(20.dp)

    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    MaterialTheme.colorScheme.secondary
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_before),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSecondary,
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

        Text(
            text = stringResource(R.string.answer),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    MaterialTheme.colorScheme.secondary
                )
        ) {
            Text(
                textAlign = TextAlign.Justify,
                text = index,
                color = MaterialTheme.colorScheme.onSecondary,
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
    onClickAnswer: (AnswerElement) -> Unit
) {


    var imgUrl :Any = answer.img.toString()
    if (answer.img == "")
        imgUrl = "https://cdn-icons-png.flaticon.com/512/8239/8239138.png"

    Column(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    MaterialTheme.colorScheme.secondary
                )
                .clickable {
                    onClickAnswer(answer)
                }
        ) {
            AsyncImage(
                model = imgUrl, contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .align(Alignment.Center)
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                fontSize = 16.sp,
                text = answer.text,
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .wrapContentHeight(Alignment.Bottom)

            )
        }
    }

}