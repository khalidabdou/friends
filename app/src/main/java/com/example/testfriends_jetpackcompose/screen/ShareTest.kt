package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.copyTextToClipboard
import com.example.testfriends_jetpackcompose.util.Utils.Companion.shareChallenge
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun ShareTest(viewModel: CreateTestViewModel) {
    val context = LocalContext.current
    //val dataStoreRepository = DataStoreRepository(context = LocalContext.current)
    var scaffoldState = rememberScaffoldState()

    var username = ME!!.username
    if (Constant.SENDER != null)
        username = Constant.SENDER!!.username

    var shortLink by remember { mutableStateOf("") }
    Utils.generateSharingLink(
        deepLink = "${Constant.PREFIX}/${ME!!.inviteId}".toUri()
    ) { generatedLink ->
        shortLink =
             generatedLink
       viewModel.saveDynamicLink(shortLink)
    }

    LaunchedEffect(key1 = scaffoldState) {
        viewModel.updateMyQuestions()
    }

    Scaffold(backgroundColor = Color.White) {
        Column {
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(viewModel.questions.size) {
                    ItemAnswer(index = it, question = viewModel.question[it], username = username)
                }
            }
            ShareBox(
                text = shortLink,
                onShare = {
                    shareChallenge(context = context, shortLink)
                },
                onCopyText = {
                    ME!!.dynamicLink = shortLink
                    viewModel.updateMyQuestions()
                    copyTextToClipboard(shortLink, context = context)
                }
            )
        }
    }
}

@Composable
fun ItemAnswer(index: Int, question: Question, username: String) {
    var imgUrl = "${Constant.BASE_URL}english/${question.id}/${question.realAnswer.img}"
    if (question.realAnswer.img == "")
        imgUrl = "${Constant.BASE_URL}english/3/4.png"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                BorderStroke(0.3.dp, darkGray.copy(0.3f)),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                style = MaterialTheme.typography.body1,
                text = question.question.replace("****", username),
                color = darkGray,
                modifier = Modifier
                    .weight(4f)

            )

            CardAnswer(
                index = index,
                answer = AnswerElement(question.realAnswer.text, question.realAnswer.img),
                realAnswer = AnswerElement("", ""),
                width = 100.dp,
                height = 100.dp,
                imageSize = 40.dp,
                onClickAnswer = {})

        }

    }
}

@Preview
@Composable
fun item() {
    //BoxImage(R.drawable.knowledge,"sjfgkdsajfhjdesf")

//    ItemAnswer(index = 0,)
}

@Composable
fun ShareBox(text: String, onShare: () -> Unit, onCopyText: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(Color.White)
            .padding(20.dp)

    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Column {
                Text(text = text, color = darkGray)
//                TextField(
//                    value = text,
//                    maxLines = 5,
//                    singleLine = false,
//                    onValueChange = {},
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(5.dp)),
//                    textStyle = MaterialTheme.typography.body1,
//                    colors = TextFieldDefaults.textFieldColors(
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        textColor = Color.Black
//                    ),
//
//                    )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
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
                        colors = ButtonDefaults.buttonColors(
                            contentColor = darkGray,
                            backgroundColor = backgroundWhite
                        )

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Share", color = darkGray, style = MaterialTheme.typography.h6)
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    BoxButton(
                        icon = R.drawable.link, background = backgroundWhite, height = 50,
                        onClick = {
                            onCopyText()
                        })
                }
            }
        }
    }

}

@Preview
@Composable
fun preview() {
    ShareBox(
        text = "jsfhjk adfjhdkjf jhafskjahfk fsjhkjabf sldfkuaidwf ",
        onCopyText = {},
        onShare = {})
}