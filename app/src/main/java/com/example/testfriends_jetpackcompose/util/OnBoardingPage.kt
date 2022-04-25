package com.example.testfriends_jetpackcompose.util

import androidx.annotation.DrawableRes
import com.example.testfriends_jetpackcompose.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.create,
        title = "Create",
        description = "Create your own quiz"
    )

    object Second : OnBoardingPage(
        image = R.drawable.network,
        title = "Share",
        description = "Share it with your friends"
    )

    object Third : OnBoardingPage(
        image = R.drawable.stats,
        title = "results",
        description = "See their results & discover your real best friends\n"
    )
}