package com.example.testfriends_jetpackcompose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.screen.CircularProgressIndicatorSample
import com.example.testfriends_jetpackcompose.screen.Friend
import com.example.testfriends_jetpackcompose.screen.MyButton
import com.example.testfriends_jetpackcompose.ui.theme.TestFriends_JetPackComposeTheme
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class HandleDynamicLink : ComponentActivity() {

    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestFriends_JetPackComposeTheme {
                context = LocalContext.current
                val viewModel: AnswerTestViewModel =
                    ViewModelProvider(this).get(AnswerTestViewModel::class.java)

                FirebaseDynamicLinks.getInstance()
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

                when (viewModel.sender.value) {
                    is NetworkResults.Error -> {
                        context.startActivity(Intent(context, MainActivity::class.java))
                        this.finish()
                    }
                    is NetworkResults.Success -> {
                        SENDER = viewModel.sender.value!!.data!!
                        //Log.d("USER", viewModel.sender.value!!.data!!.myQuestions)

                        viewModel.questions =
                            Utils.stringToQuestionArrayList(viewModel.sender.value!!.data!!.myQuestions)
                                .toMutableStateList()
                        challenge(
                            viewModel.sender.value!!.data!!,
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
                                    MaterialTheme.colorScheme.background
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
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Friend(user = user.username)
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.want_challenge).replace("****", user.username),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        )
        MyButton(
            text = stringResource(R.string.start_answer),
            icon = null,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClickButton = { onStartClick() }
        )

    }
}

