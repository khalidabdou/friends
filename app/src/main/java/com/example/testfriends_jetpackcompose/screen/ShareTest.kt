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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.copyTextToClipboard
import com.example.testfriends_jetpackcompose.util.Utils.Companion.shareChallenge
import com.example.testfriends_jetpackcompose.util.backgrounds.Companion.linearGradientBrush
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun ShareTest(viewModel: CreateTestViewModel) {

    val context = LocalContext.current
    val dataStoreRepository = DataStoreRepository(context = LocalContext.current)
    var scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = scaffoldState) {
        viewModel.updateMyQuestions(dataStoreRepository)
    }
    var shortLink by remember { mutableStateOf("") }
    Utils.generateSharingLink(
        deepLink = "${Constant.PREFIX}/${ME!!.inviteId}".toUri()
    ) { generatedLink ->
        shortLink =
            context.getString(R.string.share_text) + " " + generatedLink + " Or Use my invitation code " + ME!!.inviteId
        Log.d("dynamic_link", generatedLink)
    }
    Scaffold {
        Column {
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(viewModel.questions.size) {
                    ItemAnswer(viewModel.question[it])
                }
            }
            ShareBox(
                text = shortLink,
                onShare = {
                    shareChallenge(context = context, shortLink)
                },
                onCopyText = {
                    copyTextToClipboard(shortLink, context = context)
                }

            )
        }
    }
}

@Composable
fun ItemAnswer(question: Question) {
    var imgUrl = "${Constant.BASE_URL}english/${question.id}/${question.realAnswer.img}"
    if (question.realAnswer.img == "")
        imgUrl = "${Constant.BASE_URL}english/3/4.png"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(2.dp, darkGray), shape = RoundedCornerShape(10.dp))
            .background(Color.White)
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
                text = question.question,
                color = darkGray,
                modifier = Modifier
                    .weight(4f)

            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = imgUrl, contentDescription = null, modifier = Modifier.size(100.dp)
                )
                Text(
                    text = question.realAnswer.text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    color = darkGray,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
            }

        }

    }
}

@Preview
@Composable
fun item() {
    //BoxImage(R.drawable.knowledge,"sjfgkdsajfhjdesf")
    //ItemAnswer(question = questionList[0])
}

@Composable
fun ShareBox(text: String, onShare: () -> Unit, onCopyText: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(darkGray)

    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Spacer(modifier = Modifier.width(10.dp).height(10.dp))
            Avatar(ME!!.username, )
            Spacer(modifier = Modifier.width(10.dp).height(10.dp))
            Column {
                TextField(
                    value = text,
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
                    textStyle = MaterialTheme.typography.body1
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
                        colors = ButtonDefaults.buttonColors(contentColor = darkGray, backgroundColor = backgroundWhite)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Share",color= darkGray, style = MaterialTheme.typography.h6)
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    BoxButton(icon = R.drawable.link, 42,
                        onClick = {
                            onCopyText()
                        })
                }


            }

        }
    }

}