package com.example.testfriends_jetpackcompose.repository

import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.Remote
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResultsRepo @Inject constructor(private var remote: Remote) {

    suspend fun updateMyQuestions(id: Int, invate: String, questions: String) =
        remote.updateMyQuestions(id = id, invate = invate, questions = questions)

    suspend fun challenge(id: String) = remote.getUser(id, null)

    suspend fun createResults(
        sender: Int,
        receiver: Int,
        answers: String,
        token: String,
        receiverName: String
    ) =
        remote.createResults(sender, receiver, answers, token, receiverName)

    suspend fun getResults(id: Int): Response<ListResults?> = remote.getResults(id)
}