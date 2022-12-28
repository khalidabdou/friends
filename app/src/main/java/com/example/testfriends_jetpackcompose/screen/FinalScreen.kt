package com.example.testfriends_jetpackcompose.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel


@Composable
fun FinalScreen(navHostController: NavHostController, viewModel: AnswerTestViewModel) {
    val scaffoldState = rememberScaffoldState()

    //val sender = SENDER!!
    val questions = viewModel.questions
    val sender = viewModel.sender.value!!.data!!
    val openDialog = remember { mutableStateOf(false) }
    val results = questions.filter { q -> q.realAnswer.text == q.answerSender }.size

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Avatar(sender.username)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sender.username,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "$results/${questions.size}",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(questions.size) {
                    val emoji = if (questions[it].realAnswer.text == questions[it].answerSender) {
                        "✅ "
                    } else {
                        "❌ "
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Text(
                            text = questions[it].question.replace(
                                "****",
                                sender.username
                            ) + "",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(4f)
                        )
                        Text(text = "$emoji")
                    }
                }
            }
        }
        if (openDialog.value) {
            when (viewModel.sender.value) {
                is NetworkResults.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.user_not_found),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    openDialog.value = false
                }
                is NetworkResults.Success -> {
                    ChallengeDialog(
                        user = viewModel.sender.value!!.data,
                        onClick = { openDialog.value = it },
                        onConfirm = {
                            if (it) navHostController.navigate(Screen.Answer.route)
                        }
                    )
                }
                is NetworkResults.Loading -> {
                    ChallengeDialog(user = null, onClick = {
                        openDialog.value = true
                    }, onConfirm = {
                    })
                }
            }
        }
    }

    BackHandler {
        SENDER = null
        navHostController.navigate(Screen.Home.route) {
            navHostController.popBackStack()
        }
    }
}

@Composable
fun Friend(user: String, textColor: Color = MaterialTheme.colorScheme.primary) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .clip(
                    CircleShape
                )
        )
        Text(text = user, color = textColor)
    }
}

