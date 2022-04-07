package com.example.testfriends_jetpackcompose.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.util.backgrounds.Companion.linearGradientBrush
import com.example.testfriends_jetpackcompose.viewmodel.ResultsViewModel

@Composable
fun ResultsFriends() {

    val viewModelResult: ResultsViewModel = viewModel()
    LazyColumn(
        Modifier
            .background(White)
            .fillMaxSize()
    ) {
        items(5) {
            ItemResult()
        }
    }
}

@Composable
fun ItemResult(item: ResultTest) {
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
                .padding(top = 50.dp, start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(linearGradientBrush)

        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    User(item.Sender)
                    User(item.Receiver)
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
                        BoxButton(icon = R.drawable.share, 30)
                        Spacer(modifier = Modifier.width(4.dp))
                        BoxButton(icon = R.drawable.download, 30)

                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .width(70.dp)
                .align(Alignment.TopCenter),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.love),
                contentDescription = "",
                tint = backgroundWhite
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 16.sp),
                text = item.result,
                color = Black.copy(0.7f)
            )
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
fun BoxButton(icon: Int, height: Int) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .clickable { }
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
fun ItemResult() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundWhite)
    ) {

        Image(
            modifier = Modifier
                .size(70.dp)
                .padding(10.dp)
                .clip(
                    CircleShape
                )
                .weight(1f),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "",
        )
        Column(modifier = Modifier.weight(3f)) {

        }
        Box(
            modifier = Modifier
                .weight(2f)
        ) {
            CustomComponent(
                canvasSize = 80.dp,
                indicatorValue = 50,
                backgroundIndicatorStrokeWidth = 30f,
                foregroundIndicatorStrokeWidth = 20f, smallText = ""
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
    ResultsFriends()
}


@Preview
@Composable
fun BoxButtonPrev() {
    BoxButton(icon = R.drawable.share, 30)
}


@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = White,//MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = MaterialTheme.colors.primary,
    foregroundIndicatorStrokeWidth: Float = 100f,
//    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    bigTextColor: Color = MaterialTheme.colors.onSurface,
    bigTextSuffix: String = "GB",
    smallText: String = "Remaining",
    smallTextFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    smallTextColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
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


