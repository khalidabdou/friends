package com.example.testfriends_jetpackcompose.di

import com.example.testfriends_jetpackcompose.data.ResultTest
import com.example.testfriends_jetpackcompose.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @POST("insertUser")
    suspend fun insertUser(
        @Body user: User
    ): Response<Any>

    @GET("getUser")
    suspend fun getUser(): User

    @GET("getResults")
    suspend fun getResults(): List<ResultTest>

    @POST("updateMyQuestions")
    suspend fun updateMyQuestions(@Query("id") id: Int, @Query("questions")questions : String) :Response<Any>


}