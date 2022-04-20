package com.example.testfriends_jetpackcompose.data

import com.example.testfriends_jetpackcompose.di.Api
import retrofit2.Response
import javax.inject.Inject

class Remote @Inject constructor(
    private val api: Api
) {
    suspend fun insertUser(user: User): Response<Int> {
        return api.insertUser(user)
    }

    suspend fun getUser(id: String): Response<User> {
        return api.getUser(id = id)
    }

    suspend fun updateMyQuestions(id: Int, invate: String, questions: String) =
        api.updateMyQuestions(id = id, questions = questions, anviteId = invate)

    suspend fun createResults(
        sender: Int,
        receiver: Int,
        answers: String,
        token: String,
        receiverName: String
    ) =
        api.createResult(sender, receiver, answers, token, receiverName)

    suspend fun getResults(id: Int): Response<ListResults> = api.getMyResults(id)


}