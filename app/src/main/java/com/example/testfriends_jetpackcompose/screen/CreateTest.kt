package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun TestMain(navHostController: NavHostController, viewModel: CreateTestViewModel) {


    var index = viewModel.index
    var question = viewModel.question
    var visible by remember { mutableStateOf(true) }

    viewModel.getResults()

    fun setRealAnswer(answer: String, img: Int) {
        viewModel.setAnswer(answer = answer, img)
        if (viewModel.incrementIndex()) {
            if (SENDER == null)
                navHostController.navigate("Share_screen")
            else navHostController.navigate("Final_screen")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundWhite),
        verticalArrangement = Arrangement.Center
    ) {
        if (SENDER != null)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Color.White
                    )
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(
                            CircleShape
                        ),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(SENDER!!.image),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "answer for ${SENDER!!.username} questions ",
                    style = MaterialTheme.typography.body1,
                    color = darkGray
                )

            }
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
                    text = viewModel.questions[index].question,
                    style = MaterialTheme.typography.body1,
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .align(Alignment.TopEnd),
                Alignment.Center

            ) {
                Text(
                    text = "${index + 1}/${question.size}",
                    color = Color.Black,
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
                R.drawable.kno,
                answer = viewModel.questions[index].answer1,
                realAnswer = question[index].realAnswer,
                visible = visible
            ) { answer, realAnswerImg -> setRealAnswer(answer, realAnswerImg) }
            Spacer(modifier = Modifier.width(20.dp))
            CardAnswer(
                R.drawable.colors,
                answer = viewModel.questions[index].answer2,
                realAnswer = question[index].realAnswer,
                visible = visible
            ) { answer, img ->
                setRealAnswer(answer = answer, img = img)
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
                R.drawable.knowledge,
                answer = viewModel.questions[index].answer3,
                realAnswer = question[index].realAnswer,
                visible = visible
            ) { answer, img ->
                setRealAnswer(answer = answer, img = img)
            }
            Spacer(modifier = Modifier.width(20.dp))
            CardAnswer(
                R.drawable.maths,
                answer = viewModel.questions[index].answer4,
                realAnswer = question[index].realAnswer,
                visible = visible
            ) { answer, img ->
                setRealAnswer(answer = answer, img = img)
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
                        Log.d("Answer", question[index].realAnswer + " index$index")
                        if (question[index].realAnswer != "")
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

@Composable
fun CardAnswer(
    img: Int,
    answer: String,
    realAnswer: String,
    visible: Boolean,
    onClickAnswer: (String, Int) -> Unit
) {

    Column(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(5.dp))
                //.background(Color.Gray.copy(0.2f))
                .background(
                    if (answer == realAnswer) MaterialTheme.colors.primary else Color.Gray.copy(
                        0.2f
                    )
                )
                .clickable {
                    onClickAnswer(answer, img)
                }
        ) {

            Image(
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp, bottom = 60.dp),
                painter = painterResource(id = img),
                contentDescription = "",
            )
            Text(
                style = MaterialTheme.typography.body1,
                text = answer,
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