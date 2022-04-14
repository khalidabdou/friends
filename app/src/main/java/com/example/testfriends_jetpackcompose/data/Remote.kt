package com.example.testfriends_jetpackcompose.data

import com.example.testfriends_jetpackcompose.di.Api
import retrofit2.Response
import javax.inject.Inject

class Remote @Inject constructor(
    private val api: Api
){
   suspend fun insertUser(user: User)=api.insertUser(user)

    fun getUser():Response<String> {
        return api.getUser()
    }

}