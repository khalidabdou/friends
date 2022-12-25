package com.example.testfriends_jetpackcompose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.screen.CircularProgressIndicatorSample
import com.example.testfriends_jetpackcompose.screen.Friend
import com.example.testfriends_jetpackcompose.screen.MyButton
import com.example.testfriends_jetpackcompose.ui.theme.TestFriends_JetPackComposeTheme
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class HandleDynamicLink : ComponentActivity() {
    val TAG = "firebase_app"
    lateinit var context: Context
    var notNull = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestFriends_JetPackComposeTheme {
                context = LocalContext.current
                val viewModel: AnswerTestViewModel = hiltViewModel()
                Firebase.dynamicLinks
                    .getDynamicLink(intent)
                    .addOnSuccessListener(this) { pendingDynamicLinkData ->
                        var deepLink: Uri? = null
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.link
                            if (deepLink != null) {
                                val userId: String? = deepLink.lastPathSegment
                                if (userId != null) {
                                    viewModel.challenge(userId)
                                } else {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            MainActivity::class.java
                                        )
                                    )
                                    this.finish()
                                }
                            }
                        }
                    }
                    .addOnFailureListener(this) {
                        context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            )
                        )
                        this.finish()
                    }

                when (viewModel._questions.value) {
                    is NetworkResults.Error -> {
                        context.startActivity(Intent(context, MainActivity::class.java))
                        this.finish()
                    }
                    is NetworkResults.Success -> {
                        SENDER = viewModel._questions.value!!.data!!
                        Log.d("USER", viewModel._questions.value!!.data!!.myQuestions)
                        viewModel.questions =
                            Utils.stringToQuestionArrayList(viewModel._questions.value!!.data!!.myQuestions)
                                .toMutableStateList()
                        challenge(
                            viewModel._questions.value!!.data!!,
                            onStartClick = {
                                context.startActivity(Intent(context, MainActivity::class.java))
                                this.finish()
                            })
                    }
                    is NetworkResults.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    darkGray
                                )
                        ) {
                            CircularProgressIndicatorSample()
                        }
                    }
                }
            }
        }
    }
}


@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun challenge(user: User, onStartClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundWhite)
            .padding(20.dp)
    ) {
        Friend(user = user.username)
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            textAlign = TextAlign.Center,
            text = "${user.username} want to challenge you by answering his question ",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(1f)
        )
        MyButton(
            text = "Start Answering",
            icon = null,
            background = darkGray,
            contentColor = Color.White,
            onClickButton = { onStartClick() }
        )

    }
}

