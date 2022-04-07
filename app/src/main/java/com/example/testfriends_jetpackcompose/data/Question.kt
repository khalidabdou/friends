package com.example.testfriends_jetpackcompose.data

data class Question(
    val id: Int,
    val question: String,
    var realAnswer: String,
    var realAnswerImg: Int,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val img1: Int,
    val img2: Int,
    val img3: Int,
    val img4: Int,
    var answered: Boolean = false,
)
