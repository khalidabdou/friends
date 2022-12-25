package com.example.testfriends_jetpackcompose.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.*
import com.example.testfriends_jetpackcompose.di.NetworkModule.gson
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.HandleResponse
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class AnswerTestViewModel @Inject constructor(
    application: Application,
    private val resultRepo: ResultsRepo,
) : AndroidViewModel(application) {


    var _questions = mutableStateOf<NetworkResults<User>?>(NetworkResults.Loading())
        private set

    var index by mutableStateOf(0)
    var questions = mutableListOf<Question>()
    var result= mutableStateOf(0)


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

    fun setAnswer(answer: String) {
        questions[index].answerSender =  answer
    }

    fun challenge(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id == "")
                return@launch
            if (_questions.value is NetworkResults.Error || _questions.value is NetworkResults.Loading) {
                _questions.value = NetworkResults.Loading()
                val response = resultRepo.challenge(id)
                val handleUser = HandleResponse(response)
                _questions.value = handleUser.handleResult()
                questions = Utils.stringToQuestionArrayList(_questions.value!!.data!!.myQuestions).toMutableStateList()
                //var arra=Utils.stringToQuestionArrayList(_questions.value.data!!.myQuestions)
                Log.d("challenge",questions.toString())
            }
        }
    }

    fun createResults() =
        viewModelScope.launch(Dispatchers.IO) {
            //result = Utils.compareResults(SENDER!!.myQuestions, myAnswers)
            val res=gson.toJson(questions.toList())
            Log.d("res",res!!)
            resultRepo.createResults(
                SENDER!!.id, ME!!.id, res, SENDER!!.token,
                ME!!.username
            )
        }
}