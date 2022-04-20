package com.example.testfriends_jetpackcompose.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils

@Composable
fun ResultsFriends(resultTest: ListResults) {


    LazyColumn(
        Modifier
            .background(backgroundWhite)
            .fillMaxSize()
    ) {

        items(resultTest.listResults.size) {


            ItemResults(resultTest.listResults[it])
        }
    }
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
        Text(text = user.username, color = Black.copy(0.8f))
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
fun BoxButton(icon: Int, height: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .clickable {
                onClick
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
fun ItemResults(item: ResultTest) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(White)
    ) {

        Spacer(modifier = Modifier.width(5.dp))
        Avatar(item = "AB", backgroundWhite)

        Spacer(modifier = Modifier.width(3.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .height(70.dp)
                .weight(3f)
        ) {

            Text(
                text = "${item.ReceiverName}",
                color = Black.copy(0.8f),
                style = MaterialTheme.typography.h3
            )
            Text(
                text = Utils.score(item.answers, ME!!.myQuestions),
                color = Color.Gray,
                style = MaterialTheme.typography.body1
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .padding(5.dp)
        ) {
            CustomComponent(
                canvasSize = 80.dp,
                indicatorValue = if (ME!!.myQuestions != null) Utils.compareResults(
                    item.answers,
                    ME!!.myQuestions
                ) else 0,
                backgroundIndicatorStrokeWidth = 25f,
                foregroundIndicatorStrokeWidth = 25f, smallText = ""
            )
        }
    }
}

@Preview
@Composable
fun ItemPrev() {
    //ItemResult()
}

@Preview
@Composable
fun ListPrev() {
    //ResultsFriends()
}


@Preview
@Composable
fun BoxButtonPrev() {
    BoxButton(icon = R.drawable.share, 30, onClick = {})
}


@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = backgroundWhite,//MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = darkGray,
    foregroundIndicatorStrokeWidth: Float = 100f,
//    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    bigTextColor: Color = darkGray,
    bigTextSuffix: String = "%",
    smallText: String = "Remaining",
    smallTextFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    smallTextColor: Color = darkGray.copy(alpha = 0.3f)
) {
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }
    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage =
        (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000)
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0)
            MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            bigText = receivedValue,
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
//    indicatorStokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
//    indicatorStokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    bigText: Int,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit
) {
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )
    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}


