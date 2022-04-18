package com.example.testfriends_jetpackcompose.di

import com.example.testfriends_jetpackcompose.data.ListResults
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
    suspend fun getUser(@Query("id") id: Int): Response<User>

    @POST("updateMyQuestions")
    suspend fun updateMyQuestions(
        @Query("id") id: Int,
        @Query("questions") questions: String
    ): Response<Any>

    @POST("createResult")
    suspend fun createResult(
        @Query("sender") sender: Int,
        @Query("receiver") receiver: Int,
        @Query("answers") answers: String,
        @Query("token") token: String
    )

    @GET("getResults")
    suspend fun getMyResults(
        @Query("id") id: Int,
    ): Response<ListResults>


}