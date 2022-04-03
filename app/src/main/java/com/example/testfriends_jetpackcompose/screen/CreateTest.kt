package com.example.testfriends_jetpackcompose.screen

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.util.backgrounds.Companion.linearGradientBrush
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun TestMain(navHostController: NavHostController, viewModel: CreateTestViewModel) {

    var index = viewModel.index
    var question = viewModel.question
    var visible by remember { mutableStateOf(true) }

    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "", modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(linearGradientBrush),
            contentAlignment = Alignment.Center
        ) {
            Text(style = MaterialTheme.typography.body1, text = viewModel.questions[index].question)  
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
                visible = visible,
                onClickAnswer = { answer ->
                    viewModel.setAnswer(answer = answer)
                    viewModel.incrementIndex()

                })
            Spacer(modifier = Modifier.width(20.dp))
            CardAnswer(
                R.drawable.colors,
                answer = viewModel.questions[index].answer2,
                realAnswer = question[index].realAnswer,
                visible = visible,
                onClickAnswer = { answer ->
                    viewModel.setAnswer(answer = answer)
                    viewModel.incrementIndex()
                })
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
                visible = visible,
                onClickAnswer = { answer ->
                    viewModel.setAnswer(answer = answer)
                    viewModel.incrementIndex()

                })
            Spacer(modifier = Modifier.width(20.dp))
            CardAnswer(
                R.drawable.maths,
                answer = viewModel.questions[index].answer4,
                realAnswer = question[index].realAnswer,
                visible = visible,
                onClickAnswer = { answer ->
                    viewModel.setAnswer(answer = answer)
                    viewModel.incrementIndex()
                })
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
                        if (viewModel.incrementIndex()) {
                            navHostController.navigate("Share_screen")
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
    onClickAnswer: (String) -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
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
                        onClickAnswer(answer)
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
                        .background(linearGradientBrush)
                )

            }
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