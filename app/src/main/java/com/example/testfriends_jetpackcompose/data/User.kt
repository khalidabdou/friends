package com.example.testfriends_jetpackcompose.data

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("myQuetions")
    val myQuestions: String
) {

    override fun toString(): String {
        return """{"id": $id , "username" : "$username" , "email" : "$email" , "token" : "$token" , "img" : "$image" }"""
    }
}

