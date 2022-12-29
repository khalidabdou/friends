package com.example.testfriends_jetpackcompose.data

import com.example.testfriends_jetpackcompose.di.Api
import retrofit2.Response
import javax.inject.Inject

class Remote @Inject constructor(
    private val api: Api
) {
    suspend fun insertUser(user: User): Response<User?> {
        return api.insertUser(user)
    }

    suspend fun getUser(id: String?, email: String?): Response<User?> {
        return api.getUser(id = id, email = email)
    }

    suspend fun updateMyQuestions(user: User) =
        api.updateMyQuestions(user)

    suspend fun createResults(
        sender: Int,
        receiver: Int,
        answers: String,
        token: String,
        receiverName: String
    ) =
        api.createResult(sender, receiver, answers, token, receiverName)

    suspend fun getResults(id: Int): Response<ListResults?> = api.getMyResults(id)
    suspend fun updateUser(user: User): Response<User?> = api.updateUser(user)
    suspend fun getLanguages(): Response<Languages?> = api.getLanguages()
    suspend fun deleteResult(id:Int):Response<Int?> = api.deleteResult(id)

}