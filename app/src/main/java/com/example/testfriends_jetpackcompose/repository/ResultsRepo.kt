package com.example.testfriends_jetpackcompose.repository

import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.Remote
import retrofit2.Response
import javax.inject.Inject

class ResultsRepo @Inject constructor(private var remote: Remote) {

    suspend fun updateMyQuestions(id: Int, questions: String) =
        remote.updateMyQuestions(id, questions)

    suspend fun challenge(id: Int) = remote.getUser(id)

    suspend fun createResults(sender: Int, receiver: Int, answers: String, token: String) =
        remote.createResults(sender, receiver, answers, token)

    suspend fun getResults(id: Int): Response<ListResults> = remote.getResults(id)
}