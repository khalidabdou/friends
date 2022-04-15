package com.example.testfriends_jetpackcompose.data

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    val email: String,
    var token: String,
    val img: String
) {

    override fun toString(): String {
        return """{"id": $id , "username" : "$username" , "email" : "$email" , "token" : "$token" , "img" : "$img" }"""
    }
}

