package com.example.testfriends_jetpackcompose.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.testfriends_jetpackcompose.R

enum class OnBoardingPage(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    First(
        image = R.drawable.ic_create,
        title = R.string.welcome_title1,
        description = R.string.welcome_desc1
    ),
    Second(
        image = R.drawable.ic_share,
        title = R.string.welcome_title2,
        description = R.string.welcome_desc2
    ),
    Third(
        image = R.drawable.ic_res,
        title = R.string.welcome_title3,
        description = R.string.welcome_desc3
    )
}