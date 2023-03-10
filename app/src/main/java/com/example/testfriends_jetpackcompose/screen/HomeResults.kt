package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils

@Composable
fun ResultsFriends(
    resultTest: List<ResultTest>, paddingValues: PaddingValues,
    onClick: (ResultTest) -> Unit
) {
    if (resultTest.isEmpty()) {
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
                .padding(15.dp)
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
                ResultsList(resultTest[it], onClick = { onClick(it) })
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
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
        modifier = Modifier.padding(top = 20.dp, start = 20.dp)
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
fun ResultsList(item: ResultTest, onClick: (ResultTest) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
                color = MaterialTheme.colorScheme.onSurface,
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
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



