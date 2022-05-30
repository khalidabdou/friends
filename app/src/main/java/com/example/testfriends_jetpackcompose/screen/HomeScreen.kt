package com.example.testfriends_jetpackcompose.screen

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.ResultsViewModel


@Composable
fun HomeScreen(navController: NavHostController, createTestViewModel: CreateTestViewModel) {

    val viewModelresults: ResultsViewModel = hiltViewModel()
    //var user = viewModelresults.userAuth
    val openDialog = remember { mutableStateOf(false) }
    createTestViewModel.getResults()
    createTestViewModel.shareDynamcLink()
    var scaffoldState = rememberScaffoldState()
    val activity = (LocalContext.current as? Activity)

    Surface(color = MaterialTheme.colors.background) {
        NavigationDrawer()
        var navigateClick by remember { mutableStateOf(false) }

        val offSetAnim by animateDpAsState(
            targetValue = if (navigateClick) 450.dp else 0.dp,
            tween(1000)
        )
        val clipDp by animateDpAsState(
            targetValue = if (navigateClick) 60.dp else 0.dp,
            tween(1000)
        )
        val scaleAnim by animateFloatAsState(
            targetValue = if (navigateClick) 0.5f else 1.0f,
            tween(1000)
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleAnim)
                .offset(x = offSetAnim)
                .clip(RoundedCornerShape(clipDp))
                .background(darkGray)
                .clickable {
                    navigateClick = false
                },
            scaffoldState = scaffoldState,
            backgroundColor = White,
            topBar = {
                AppBar(
                    ME!!,
                    viewModelresults.search.value,
                    context = LocalContext.current,
                    onShare = {
                        if (createTestViewModel.dynamicLink == "")
                            Toast.makeText(activity, "Create your test first", Toast.LENGTH_LONG)
                                .show()
                        else
                            Utils.shareChallenge(
                                context = activity as Context,
                                createTestViewModel.dynamicLink
                            )
                    },
                    onDrawer = {
                        navigateClick = !navigateClick
                    },
                    search = { viewModelresults.setSearchText(it) },
                    onSearchClick = {
                        if (it == "") {
                            return@AppBar
                        }
                        viewModelresults.challenge(it)
                        openDialog.value = true
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = White, onClick = {
                        SENDER = null
                        createTestViewModel.cleanAnswers()
                        navController.navigate("Create_screen")
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.create),
                        contentDescription = "",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        ) {
            when (createTestViewModel.resultsList.value) {
                is NetworkResults.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        (createTestViewModel.resultsList.value as NetworkResults.Error<ListResults>).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResults.Success -> {
                    ResultsFriends(
                        (createTestViewModel.resultsList.value as NetworkResults.Success<ListResults>).data!!.listResults,
                        onClick = {
                            SENDER = null
                            createTestViewModel.userResults = it
                            navController.navigate(Screen.FinalScreen.route)
                        })
                }
                is NetworkResults.Loading -> {
                    //Toast.makeText(LocalContext.current, "loading data ...", Toast.LENGTH_SHORT).show()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicatorSample(darkGray)
                    }
                }
            }
            if (openDialog.value) {
                when (viewModelresults.challenge.value) {
                    is NetworkResults.Error -> {
                        Toast.makeText(LocalContext.current, "user not found", Toast.LENGTH_SHORT)
                            .show()
                        openDialog.value = false
                    }
                    is NetworkResults.Success -> {
                        SENDER = viewModelresults.challenge.value.data
                        if (SENDER!!.myQuestions.isNullOrEmpty()) {
                            Toast.makeText(
                                LocalContext.current,
                                "${SENDER!!.username} have no test, tell he to create test ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            openDialog.value = false
                        } else
                            ChallengeDialog(
                                user = viewModelresults.challenge.value.data,
                                onClick = { openDialog.value = it },
                                onConfirm = {
                                    createTestViewModel.cleanAnswers()
                                    if (it) navController.navigate("Create_screen")
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
        SENDER = null
        activity?.finish()
    }
}

@Composable
fun AppBar(
    user: User,
    textSearch: String,
    context: Context,
    onDrawer: () -> Unit,
    onShare: () -> Unit,
    search: (String) -> Unit,
    onSearchClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                //horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconCircle(icon = Icons.Default.Info, onClick = { onDrawer() })
                Text(
                    text = "Profile",
                    color = darkGray,
                    fontSize = 22.sp,
                    modifier = Modifier.weight(1f)
                )
                IconCircle(icon = Icons.Default.Share, onClick = {
                    onShare()
                })

            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.avatar), contentDescription = "",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(text = "Welcome", color = darkGray.copy(0.7f))
                    Text(text = "${user.username}", fontSize = 20.sp, color = darkGray)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                MyTextField(
                    placeholder = "code, email or name ",
                    text = textSearch,
                    icon = Icons.Default.Search,
                    textStyle = MaterialTheme.typography.h6,
                    textColor = darkGray,
                    backgroundColor = backgroundWhite,
                    isPassword = false,
                    onChange = { search(it) },

                    modifier = Modifier
                        .weight(3f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .padding(vertical = 0.dp),
                    onSearch = {
                        try {
                            onSearchClick(textSearch)
                        } catch (ex: Exception) {
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun IconCircle(
    icon: ImageVector,
    onClick: () -> Unit
) {

    Icon(
        imageVector = icon,
        tint = darkGray.copy(0.5f),
        contentDescription = "",
        modifier = Modifier
            .size(27.dp)
            .border(width = 1.dp, color = darkGray.copy(0.7f), shape = CircleShape)
            .padding(4.dp)
            .clickable {
                onClick()
            }
    )
}


@Composable
fun NavigationDrawer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkGray)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

                .padding(20.dp)
        ) {
            item {
                Step(text = "✿ click the button below", color = backgroundWhite)
                Step(text = "✿ answer your questions", color = backgroundWhite)
                Step(
                    text = "✿ you get the link share it with your friends ",
                    color = backgroundWhite
                )
                Step(text = "✿  wait for their answers", color = backgroundWhite)
                Step(text = "✿  Come back to this app and see the results", color = backgroundWhite)
            }
        }
    }

}


@Preview
@Composable
fun prevs() {
    //var user =
    //User(id = 0, username = "abdellah", email = "@egample.com", token = "", image = "", "")
    //AppBar(user)
}