package com.example.testfriends_jetpackcompose.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

class backgrounds {

    companion object{
        val linearGradientBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFAD5389),
                Color(0xFF3C1053),

                ),
            start = Offset(Float.POSITIVE_INFINITY, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY),
        )
    }
}