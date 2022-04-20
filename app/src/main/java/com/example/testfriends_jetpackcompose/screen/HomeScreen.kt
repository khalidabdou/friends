package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.NetworkResults
import com.example.testfriends_jetpackcompose.viewmodel.ResultsViewModel


@Composable
fun HomeScreen(navController: NavHostController, createTestViewModel: CreateTestViewModel) {

    val viewModelresults: ResultsViewModel = hiltViewModel()
    var user = viewModelresults.userAuth
    val openDialog = remember { mutableStateOf(false) }
    ME = user.value
    Log.d("userme", user.toString())

    createTestViewModel.getResults()
    var scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                user.value,
                viewModelresults.search.value,
                search = { viewModelresults.setSearchText(it) },
                onSearchClick = {
                    viewModelresults.challenge(it)
                    openDialog.value = true
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(backgroundColor = White, onClick = {
                SENDER = null
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
        if (createTestViewModel.resultsList.value != null)
            ResultsFriends(createTestViewModel.resultsList.value!!)


        if (openDialog.value) {
            when (viewModelresults.challenge.value) {
                is NetworkResults.Error -> {
                    ChallengeDialog(
                        user = null,
                        onClick = { openDialog.value = it },
                        onConfirm = {})
                }
                is NetworkResults.Success -> {
                    SENDER = viewModelresults.challenge.value.data
                    ChallengeDialog(
                        user = viewModelresults.challenge.value.data,
                        onClick = { openDialog.value = it },
                        onConfirm = {
                            if (it) navController.navigate("Create_screen")
                        }
                    )
                }
                is NetworkResults.Loading -> {
                    ChallengeDialog(user = null, onClick = {
                        if (it == null)
                            openDialog.value = false
                    }, onConfirm = {})
                }
            }


        }
    }
}

@Composable
fun AppBar(
    user: User,
    textSearch: String,
    search: (String) -> Unit,
    onSearchClick: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                    .background(darkGray)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .padding(start = 10.dp),
                    ) {
                        Text(
                            text = user.username,
                            textAlign = TextAlign.Start, color = White.copy(0.6f),
                            style = MaterialTheme.typography.h2
                        )
                        CopyId(Utils.generateId(user.username) + user.id.toString())
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                    Avatar(null, textColor = darkGray)
                }

            }
        }
        Box(
            modifier = Modifier
                .height(170.dp)
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                MyTextField(
                    placeholder = "friend code ",
                    text = textSearch,
                    textStyle = MaterialTheme.typography.h6,
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
fun CopyId(id: String) {
    Row {
        Text(
            text = id,
            textAlign = TextAlign.Start, color = Gray,
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_copy),
            contentDescription = "",
            tint = Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}


@Preview
@Composable
fun prevs() {
    //var user =
    //User(id = 0, username = "abdellah", email = "@egample.com", token = "", image = "", "")
    //AppBar(user)
}