package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.questionList
import com.example.testfriends_jetpackcompose.util.backgrounds.Companion.linearGradientBrush
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun ShareTest(viewModel: CreateTestViewModel) {

    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "", modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Column {
        LazyColumn(modifier = Modifier.weight(5f)) {
            items(viewModel.questions.size) {
                ItemAnswer(viewModel.question[it])
            }
        }

        ShareBox(onShare = {
            Log.d("updateMyQuestions", "begin")
            viewModel.updateMyQuestions(id = ME!!.id)
        })
    }
}

@Composable
fun ItemAnswer(question: Question) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(2.dp, linearGradientBrush), shape = RoundedCornerShape(10.dp))

            .background(Color.White)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                style = MaterialTheme.typography.h4,
                text = question.question,

                modifier = Modifier
                    .weight(4f)
                    .padding(top = 5.dp, start = 5.dp), lineHeight = 15.sp
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = question.realAnswerImg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    alignment = Alignment.Center,
                    contentDescription = "",
                )
                Text(
                    text = question.realAnswer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
            }

        }

    }
}

@Composable
fun BoxImage(img: Int, answer: String) {
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
                    Color.Gray.copy(
                        0.2f
                    )
                )

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

@Preview
@Composable
fun item() {
    //BoxImage(R.drawable.knowledge,"sjfgkdsajfhjdesf")
    ItemAnswer(question = questionList[0])
}

@Preview
@Composable
fun ShareBoxPrv() {
    //ShareBox()
}

@Composable
fun ShareBox(onShare:()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(linearGradientBrush)

    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(20.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
            ) {
                Image(painter = painterResource(id = R.drawable.avatar), contentDescription = "")
            }
            Column {
                TextField(
                    value = "A Verizon is the perfect device for taking Wi-Fi access with you. More robust than your smartphone's mobile hotspot, a Jetpack can connect multiple",
                    maxLines = 5,
                    singleLine = false,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                                  onShare()
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        ),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)

                    ) {
                        // Inner content including an icon and a text label
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Share")
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    BoxButton(icon = R.drawable.link, 42,
                        onClick = {
                            onShare()
                        })
                }


            }

        }
    }

}