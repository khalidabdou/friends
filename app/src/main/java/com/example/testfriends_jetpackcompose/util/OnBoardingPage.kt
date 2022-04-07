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
        title = "Create Test",
        description = "Create test by answering real answers for your self and share link with friends and wait Fo"
    )

    object Second : OnBoardingPage(
        image = R.drawable.ic_launcher_background,
        title = "Coordination",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object Third : OnBoardingPage(
        image = R.drawable.ic_launcher_background,
        title = "Dialogue",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
}