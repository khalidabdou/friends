package com.example.testfriends_jetpackcompose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.util.backgrounds

@Composable
fun ResultsFriends() {
    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "", modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    LazyColumn() {
        items(5) {
            AnimatedVisibility(
                visible = true,
                exit = fadeOut(
                    animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)
                )
            ) {
                ItemResult()
            }

        }
    }


}

@Composable
fun ItemResult() {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .animateContentSize(
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
            .clickable {
                expanded = !expanded
            }

    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(backgrounds.linearGradientBrush)

        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    User()
                    User()
                }
                if (expanded) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.White)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Image(
                            modifier = Modifier.padding(5.dp),
                            painter = painterResource(id = R.drawable.qr),
                            contentDescription = "",
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.End,

                        ) {
                        BoxButton(icon = R.drawable.share,30)
                        Spacer(modifier = Modifier.width(4.dp))
                        BoxButton(icon = R.drawable.download,30)

                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .width(70.dp)
                .align(Alignment.TopCenter),
        ) {
            Image(
                painter = painterResource(id = R.drawable.love),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 16.sp),
                text = "90%"
            )
        }


    }

}


@Composable
fun User() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .padding(top = 5.dp)
                .clip(CircleShape)
        ) {
            Image(painter = painterResource(id = R.drawable.avatar), contentDescription = "")
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Abdellah khalid")
    }
}

@Composable
fun ButtonCard(iconButton: ImageVector, color: Color, text: String) {
    Button(
        onClick = {},
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
    ) {
        // Inner content including an icon and a text label
        Icon(
            iconButton,
            contentDescription = "",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

@Composable
fun BoxButton(icon: Int,height:Int) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .clickable {   }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier
                .padding(7.dp)
                .align(Alignment.Center)
        )
    }
}


@Preview
@Composable
fun ItemPrev() {
    ItemResult()
}

@Preview
@Composable
fun ListPrev() {
    ResultsFriends()
}


@Preview
@Composable
fun BoxButtonPrev() {
    BoxButton(icon = R.drawable.share,30)
}

