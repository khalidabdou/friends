package com.example.testfriends_jetpackcompose.repository

import com.example.testfriends_jetpackcompose.data.Remote
import javax.inject.Inject

class ResultsRepo @Inject constructor(private var remote: Remote) {

    suspend fun updateMyQuestions(id:Int,questions:String) =remote.updateMyQuestions(id,questions)
}