package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.backgrounds.Companion.linearGradientBrush
import com.example.testfriends_jetpackcompose.viewmodel.ResultsViewModel


@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel: ResultsViewModel = hiltViewModel()
    var user = viewModel.userAuth
    Scaffold(

        topBar = { AppBar(user.value) },
        floatingActionButton = {
            FloatingActionButton(backgroundColor = White, onClick = {
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
        ResultsFriends()
    }
}

@Composable
fun AppBar(user: User) {

    Box(   modifier = Modifier
        .fillMaxWidth()
        ,) {
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
                    modifier = Modifier.fillMaxSize(),
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
                        CopyId()
                        Spacer(modifier = Modifier.height(10.dp))

                    }

                    Image(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(10.dp)
                            .clip(
                                CircleShape
                            )
                        ,
                        contentScale = ContentScale.Crop,
                        painter = rememberAsyncImagePainter(user.img),
                        contentDescription = ""
                    )
                }

            }
        }
        Box(modifier = Modifier
            .height(170.dp)
            .padding(10.dp)) {
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.align(Alignment.BottomStart)) {
                MyTextField(
                    placeholder = "friend code ",
                    textStyle = MaterialTheme.typography.h6,
                    isPassword = false,
                    onChange = {},
                    modifier = Modifier
                        .weight(3f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .padding(vertical = 0.dp),
                )
            }
        }

    }

}

@Composable
fun MyCard(icon: Int, title: String, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .blur(5.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .height(100.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(linearGradientBrush)
            .clickable {
                onClick()
                Log.d("changeIndex", "000")
            },


        ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .padding(start = 5.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(White.copy(0.5f))

            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)

                )
            }
            Box() {
                Column() {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp),
                        text = title, style = TextStyle(fontSize = 18.sp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = description,
                        style = TextStyle(fontSize = 14.sp, color = Color.White.copy(0.5f))
                    )
                }

            }

        }
    }
}


@Composable
fun CopyId() {
    Row() {
        Text(
            text = "JHN2D5",
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
    var user = User(id = 0, username = "abdellah", email = "@egample.com", token = "", img = "")
    AppBar(user)
}