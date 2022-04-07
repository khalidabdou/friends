package com.example.testfriends_jetpackcompose.data


data class User(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val img: String
) {

    override fun toString(): String {
        return """{"id": $id , "username" : "$username" , "email" : "$email" , "token" : "$token" , "img" : "$img" }"""
    }
}

