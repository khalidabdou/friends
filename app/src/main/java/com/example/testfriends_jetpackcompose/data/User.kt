package com.example.testfriends_jetpackcompose.data

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    var id: Int,
    @SerializedName("inviteId")
    var inviteId: String? = "",
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("myQuestions", alternate = ["myQuetions"])
    var myQuestions: String,
    @SerializedName("dynamicLink")
    var dynamicLink: String?
)

