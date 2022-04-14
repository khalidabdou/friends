package com.example.testfriends_jetpackcompose.di

import com.example.testfriends_jetpackcompose.data.User
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("/insertUser")
    suspend fun insertUser(
        @Body user: User
    ):Response<String>

    @GET("getUser")
    fun getUser():Response<String>

}