package com.example.testfriends_jetpackcompose.data

import com.example.testfriends_jetpackcompose.di.Api
import retrofit2.Response
import javax.inject.Inject

class Remote @Inject constructor(
    private val api: Api
){
   suspend fun insertUser(user: User):Response<Any> {
       return api.insertUser(user)
   }

    suspend fun getUser():User {
        return api.getUser()
    }

    suspend fun updateMyQuestions(id:Int,questions:String)=api.updateMyQuestions(id, questions)


}