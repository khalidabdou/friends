package com.example.testfriends_jetpackcompose.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.AnswerElement
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.di.NetworkModule.gson
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.HandleResponse
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnswerTestViewModel @Inject constructor(
    application: Application,
    private val resultRepo: ResultsRepo,
) : AndroidViewModel(application) {

    var sender = mutableStateOf<NetworkResults<User>?>(NetworkResults.Loading())
        private set

    var index by mutableStateOf(0)
    var questions = mutableListOf<Question>()
    var result = mutableStateOf(0)


    fun incrementIndex(): Boolean {
        if (index < questions.size - 1) {
            index++
            return false
        } else {
            createResults()
            return true
        }
    }

    fun decrementIndex() {
        if (index > 0) index--
    }

    fun setAnswer(answer: AnswerElement) {
        questions[index].answerSender = answer.text
    }

    fun challenge(id: String) {
        index = 0
        viewModelScope.launch(Dispatchers.IO) {
            if (id == "")
                return@launch
            if (sender.value is NetworkResults.Error || sender.value is NetworkResults.Loading) {
                sender.value = NetworkResults.Loading()
                val response = resultRepo.challenge(id)
                val handleUser = HandleResponse(response)
                sender.value = handleUser.handleResult()
                //SENDER=sender.value!!.data
                Log.d("SENDERID", sender.value!!.data.toString())
                if (!sender.value!!.data?.myQuestions.isNullOrEmpty())
                    questions =
                        Utils.stringToQuestionArrayList(sender.value!!.data!!.myQuestions)
                            .toMutableStateList()

            }
        }
    }

    private fun createResults() =
        viewModelScope.launch(Dispatchers.IO) {
            //Log.d("SENDERID", sender.value!!.data.toString())
            val sender = sender.value!!.data
            //result = Utils.compareResults(SENDER!!.myQuestions, myAnswers)
            val res = gson.toJson(questions.toList())
            resultRepo.createResults(
                sender!!.id, ME!!.id, res, sender.token,
                ME!!.username
            )
        }
}