package com.example.testfriends_jetpackcompose.screen

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun TestMain(navHostController: NavHostController, viewModel: CreateTestViewModel) {


    var index = viewModel.index
    var question = viewModel.question
    var username = ME!!.username
    if (SENDER != null)
        username = SENDER!!.username


    fun setRealAnswer(answer: AnswerElement) {
        viewModel.setAnswer(answer = answer)
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
                .background(backgroundWhite),
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(darkGray),
                    Alignment.Center

                ) {
                    Text(
                        text = viewModel.questions[index].question.replace("****", username),
                        style = MaterialTheme.typography.body1,
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
                    answer = viewModel.questions[index].answer1,
                    realAnswer = question[index].realAnswer,

                    ) { answer -> setRealAnswer(answer) }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = viewModel.questions[index].answer2,
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
                    answer = viewModel.questions[index].answer3,
                    realAnswer = question[index].realAnswer,
                ) { answer ->
                    setRealAnswer(answer = answer)
                }
                Spacer(modifier = Modifier.width(20.dp))
                CardAnswer(
                    index = index,
                    answer = viewModel.questions[index].answer4,
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
                        .background(MaterialTheme.colors.primary)
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
                        .background(MaterialTheme.colors.primary)
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


@Composable
fun ActionBar(index: String) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .background(darkGray)
            .padding(10.dp)
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
                color = backgroundWhite
            )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.White),
        ) {
            Text(
                textAlign = TextAlign.Justify,
                text = index,
                color = darkGray,

                )
        }
    }
}

@Composable
fun CardAnswer(
    index: Int,
    answer: AnswerElement,
    realAnswer: AnswerElement,
    onClickAnswer: (AnswerElement) -> Unit
) {
    var imgUrl = "${Constant.BASE_URL}english/$index/${answer.img}"
    if (answer.img == "")
        imgUrl = "${Constant.BASE_URL}english/3/4.png"
    Column(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    if (answer == realAnswer) MaterialTheme.colors.primary else Color.Gray.copy(
                        0.2f
                    )
                )
                .clickable {
                    onClickAnswer(answer)
                }
        ) {

            AsyncImage(
                model = imgUrl, contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.Center)

            )
            Text(
                style = MaterialTheme.typography.body1,
                text = answer.text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .wrapContentHeight(Alignment.Bottom)
                    .background(darkGray)
            )

        }
    }

}


@Composable
fun MyTextField(
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}

@Composable
@Preview
fun prev() {
    // MyCard(img = R.drawable.knowledge, answer = "crau", realAnswer = "", onClick = {})
}