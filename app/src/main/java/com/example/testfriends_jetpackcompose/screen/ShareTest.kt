package com.example.testfriends_jetpackcompose.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.copyTextToClipboard
import com.example.testfriends_jetpackcompose.util.Utils.Companion.shareChallenge
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareTest(viewModel: CreateTestViewModel) {
    val context = LocalContext.current
    //val dataStoreRepository = DataStoreRepository(context = LocalContext.current)
    var scaffoldState = rememberScaffoldState()
    val openDialog = remember { mutableStateOf(false) }
    val done = remember { mutableStateOf(false) }

    var username = ME!!.username


    var shortLink by remember { mutableStateOf("") }


    Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "", modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        openDialog.value = true
                    },
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),

                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_create),
                        contentDescription = "",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        stringResource(R.string.add_question),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(viewModel.questions.size) {
                    ItemAnswer(
                        index = it,
                        question = viewModel.questions[it],
                        username = username
                    ) {
                        viewModel.deleteQuestiom(it)
                        viewModel.questions = viewModel.questions
                    }
                }
            }
            if (done.value)
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
                ) else {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            Utils.generateSharingLink(
                                deepLink = "${Constant.PREFIX}/${ME!!.inviteId}".toUri()
                            ) { generatedLink ->
                                shortLink =
                                    generatedLink
                                viewModel.saveDynamicLink(shortLink)
                                viewModel.updateMyQuestions()
                            }
                            done.value = true
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        ),

                        ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            stringResource(R.string.done),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {}, title = {

            },
            text = {
                AddQuestion(viewModel)
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        if (!viewModel.checkIsNotEmpty()) {
                            Toast.makeText(context, context.resources.getString(R.string.empty), Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.addQuestion()
                        viewModel.resetQuestion()
                        openDialog.value = false
                    }) {
                    Text(
                        stringResource(R.string.add),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = {
                        openDialog.value = false
                    }) {
                    androidx.compose.material.Text(
                        stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            })
    }
}

@Composable
fun AddQuestion(viewModel: CreateTestViewModel) {
    val q = viewModel.addedQ
    val realAnswer = viewModel.realAnswer
    val wrong1 = viewModel.wrong1
    val wrong2 = viewModel.wrong2
    val wrong3 = viewModel.wrong3
    Column(modifier = Modifier.fillMaxWidth()) {
        MyTextFieldQuestion(
            placeholder = stringResource(R.string.your_question),
            text = q.value,
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 0.dp),
            onChange = {
                q.value = it
            },
        )
        Spacer(modifier = Modifier.height(22.dp))

        MyTextFieldQuestion(
            placeholder = stringResource(R.string.real_answer),
            text = realAnswer.value,
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 0.dp),
            onChange = {
                realAnswer.value = it
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        MyTextFieldQuestion(
            placeholder = stringResource(R.string.chose1),
            text = wrong1.value,
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 0.dp),
            onChange = {
                wrong1.value = it
            },
        )

        Spacer(modifier = Modifier.height(10.dp))
        MyTextFieldQuestion(
            placeholder = stringResource(R.string.chose2),
            text = wrong2.value,
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 0.dp),
            onChange = {
                wrong2.value = it
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        MyTextFieldQuestion(
            placeholder = stringResource(R.string.chose3),
            text = wrong3.value,
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(5.dp))
                .padding(vertical = 0.dp),
            onChange = {
                wrong3.value = it
            },
        )
    }
    Spacer(modifier = Modifier.height(22.dp))

}

@Composable
fun ItemAnswer(index: Int, question: Question, username: String, onDelete: (Int) -> Unit) {
    var imgUrl = "${Constant.BASE_URL}english/${question.id}/${question.realAnswer.img}"
    if (question.realAnswer.img == "")
        imgUrl = "${Constant.BASE_URL}english/3/4.png"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                BorderStroke(0.3.dp, MaterialTheme.colorScheme.primary.copy(0.3f)),
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
                style = MaterialTheme.typography.bodyMedium,
                text = question.question.replace("****", username),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(4f)
            )
            CardAnswer(
                index = index,
                answer = AnswerElement(question.realAnswer.text, question.realAnswer.img),
                width = 60.dp,
                height = 60.dp,
                imageSize = 20.dp,
                onClickAnswer = {})
//            Icon(
//                imageVector = Icons.Default.Delete,
//                tint = MaterialTheme.colorScheme.error,
//                contentDescription = "Delete",
//                modifier = Modifier.clickable {
//                    onDelete(index)
//                })
        }

    }
}


@Composable
fun ShareBox(text: String, onShare: () -> Unit, onCopyText: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))


    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Column {
                Text(text = text, color = MaterialTheme.colorScheme.primary)
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

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            stringResource(R.string.share),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Button(
                        onClick = {
                            onCopyText()
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        ),


                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_copy),
                            contentDescription = "",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            stringResource(R.string.copy),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }
        }
    }
}
