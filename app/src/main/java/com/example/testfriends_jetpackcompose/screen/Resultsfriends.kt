package com.example.testfriends_jetpackcompose.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils

@Composable
fun ResultsFriends(resultTest: List<ResultTest>,paddingValues: PaddingValues,
                   onClick: (ResultTest) -> Unit) {
    if (resultTest.isNullOrEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding()
        ) {
            item {

                    Step(text = stringResource(R.string.step1))
                    Step(text = stringResource(R.string.step2))
                    Step(text = stringResource(R.string.step3))
                    Step(text = stringResource(R.string.step4))
                    Step(text = stringResource(R.string.step5))

            }
        }
    } else
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.results),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(7.dp))
            }

            items(resultTest.size) {
                ItemResults(resultTest[it], onClick = { onClick(it) })
            }
        }
}

@Composable
fun Step(text: String, color: Color = MaterialTheme.colorScheme.primary) {
    Text(
        fontSize = 18.sp,
        text = text,
        color = color,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(top=20.dp, start = 20.dp)
    )
}

@Composable
fun User(user: User) {
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
        Text(
            text = user.username,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun BoxButton(icon: Int, background: Color = White, height: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(background)
            .clickable {
                onClick()
            }
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

@Composable
fun ItemResults(item: ResultTest, onClick: (ResultTest) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.primary.copy(0.5f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick(item)
            }
    ) {

        Spacer(modifier = Modifier.width(7.dp))
        Avatar(name = ME!!.username)
        Spacer(modifier = Modifier.width(7.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(3f)
        ) {

            Text(
                text = "${item.ReceiverName}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .padding(5.dp)
        ) {
            val questions = Utils.stringToQuestionArrayList(item.answers)
            val results = questions.filter { q -> q.realAnswer.text == q.answerSender }.size
            Text(
                text = "$results/${questions.size}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



