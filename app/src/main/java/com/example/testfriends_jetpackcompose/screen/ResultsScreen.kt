package com.example.testfriends_jetpackcompose.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun ResultsScreen(
    navHostController: NavHostController,
    viewModel: CreateTestViewModel,
    answerTestViewModel: AnswerTestViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val openDialog = remember { mutableStateOf(false) }
    val resultTest: ResultTest = viewModel.resultsByUser.value!!
    val questions = Utils.stringToQuestionArrayList(resultTest.answers)
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
                    .height(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Avatar(resultTest.ReceiverName)

                    Text(
                        text = resultTest.ReceiverName,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                        onClick = {
                            answerTestViewModel.challenge(resultTest.ReceiverName)
                            openDialog.value = true
                        }
                    ) {
                        Text(text = stringResource(R.string.challenge))
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .align(Alignment.BottomEnd), contentAlignment = Alignment.Center
                ) {
                    val results = questions.filter { q -> q.realAnswer.text == q.answerSender }.size
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
                            .height(90.dp)
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = questions[it].question.replace(
                                "****",
                                resultTest.ReceiverName
                            ) + "",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(4f)
                        )

                        Text(text = "$emoji")
                    }

                }
            }

            if (openDialog.value) {
                when (answerTestViewModel.sender.value) {
                    is NetworkResults.Error -> {
                        Toast.makeText(LocalContext.current,  stringResource(R.string.user_not_found), Toast.LENGTH_SHORT)
                            .show()
                        openDialog.value = false
                    }
                    is NetworkResults.Success -> {
                        ChallengeDialog(
                            user = answerTestViewModel.sender.value!!.data,
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
    }
    BackHandler {
        navHostController.navigate(Screen.Home.route) {
            navHostController.popBackStack()
        }
    }

}

