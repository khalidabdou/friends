package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.util.backgrounds.Companion.linearGradientBrush


@Composable
fun HomeScreen(navController: NavHostController) {

    Scaffold() {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            MyCard(
                R.drawable.ic_send,
                "Create",
                "Create new and share with your friends or Lovers",
                onClick = {
                    navController.navigate("Create_screen")
                })
            MyCard(
                R.drawable.ic_tested,
                "Results",
                "Check your test answered by your friends",
                onClick = { navController.navigate("Results_screen") })
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
                    .background(Color.White.copy(0.5f))

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

