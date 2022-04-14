package com.example.testfriends_jetpackcompose.di

import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.data.User
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("insertUser")
    suspend fun insertUser(
        @Body user: User
    )

    @GET("getUser")
    suspend fun getUser():User

    @GET("getResults")
    suspend fun getResults():List<ResultTest>


}