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

        val colorList = arrayListOf<Color>(
            Color(0xFFAD5389),
            Color(0xFF3C1053),
            Color(0xFF3498db),
            Color(0xFFc0392b),
            Color(0xFFf39c12),
            Color(0xFF16a085),
            Color(0xFF38ada9),
            Color(0xFF5758BB),
            Color(0xFFAD5389),
            Color(0xFF6F1E51),
            Color(0xFF0a3d62),
            Color(0xFF006266),
        )
    }
}