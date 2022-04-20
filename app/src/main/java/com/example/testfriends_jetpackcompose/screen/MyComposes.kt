package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.util.backgrounds
import kotlin.random.Random

@Composable
fun Avatar(item: String?, textColor: Color) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(
                CircleShape
            )
            .background(
                if (item == null) backgroundWhite else backgrounds.colorList[Random.nextInt(
                    0,
                    backgrounds.colorList.size
                )]
            ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "AB",
            color = textColor,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
        )

//            Image(
//                modifier = Modifier
//                    .size(70.dp)
//                    .padding(10.dp)
//                    .clip(
//                        CircleShape
//                    ),
//                contentScale = ContentScale.Crop,
//                painter = rememberAsyncImagePainter(item),
//                contentDescription = ""
//            )
    }
}