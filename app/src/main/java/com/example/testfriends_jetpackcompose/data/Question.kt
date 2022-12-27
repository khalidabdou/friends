package com.example.testfriends_jetpackcompose.data

data class Question(
    val id: Int,
    var question: String,
    var realAnswer: AnswerElement,
    var answerSender: String?,
    val answer1: AnswerElement,
    val answer2: AnswerElement,
    val answer3: AnswerElement,
    val answer4: AnswerElement,
)

data class AnswerElement(
    var text: String,
    var img: String?
)

