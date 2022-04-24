package com.example.testfriends_jetpackcompose.di

import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.User
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("insertUser")
    suspend fun insertUser(
        @Body user: User
    ): Response<Int>

    @PUT("updateUser")
    suspend fun updateUser(
        @Body user: User
    ): Response<User?>

    @GET("getUser")
    suspend fun getUser(
        @Query("id") id: String?,
        @Query("email") email: String?,
    ): Response<User?>


    @POST("updateMyQuestions")
    suspend fun updateMyQuestions(
        @Query("id") id: Int,
        @Query("inviteId") anviteId: String,
        @Query("questions") questions: String
    ): Response<Any>

    @POST("createResult")
    suspend fun createResult(
        @Query("sender") sender: Int,
        @Query("receiver") receiver: Int,
        @Query("answers") answers: String,
        @Query("token") token: String,
        @Query("ReceiverName") ReceiverName: String
    )

    @GET("getResults")
    suspend fun getMyResults(
        @Query("id") id: Int,
    ): Response<ListResults?>


}