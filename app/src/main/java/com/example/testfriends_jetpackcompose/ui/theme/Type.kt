package com.example.testfriends_jetpackcompose.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.testfriends_jetpackcompose.R


val QuickSand= FontFamily(
    Font(R.font.majalla),
    Font(R.font.majalla,FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 12.sp,
        color = Color.White,
        textAlign = TextAlign.Center
    ),

    body2 = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 12.sp,
        color = Color.White,
        textAlign = TextAlign.Center
    ),
    h1 = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),

    h2  = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Bold,
        lineHeight = 17.sp,
        fontSize = 30.sp
    ),
    h3 = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),

    h4  = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Bold,
        lineHeight = 17.sp,
        fontSize = 20.sp
    ),
    h5 = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Bold,
        lineHeight = 17.sp,
        fontSize = 18.sp
    ),
    h6 = TextStyle(
        fontFamily = QuickSand,
        fontWeight = FontWeight.Bold,
        lineHeight = 17.sp,
        fontSize = 16.sp
    ),


    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */



)