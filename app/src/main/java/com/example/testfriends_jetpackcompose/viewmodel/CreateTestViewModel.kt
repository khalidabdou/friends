package com.example.testfriends_jetpackcompose.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateTestViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val resultRepo: ResultsRepo,
) :

    ViewModel() {
    val questionFromJson = Constant.getJsonDataFromAsset(context = context, "question.json")
    val gson = Gson()
    val listPersonType = object : TypeToken<List<Question>>() {}.type
    var questions: List<Question> = gson.fromJson(questionFromJson, listPersonType)

    var resultsList = mutableStateOf<ListResults?>(null)
    //private val questions = MutableStateFlow<question>

    var myAnswers by mutableStateOf("")
        private set


    var index by mutableStateOf(0)
    var question: List<Question> by mutableStateOf(questions)


    fun incrementIndex(): Boolean {
        if (index < 17) {
            index++
            return false
        } else return true
    }

    fun decrementIndex() {
        if (index > 0) index--
    }

    fun setAnswer(answer: String, img: Int) {
        question[index].realAnswer = answer
        question[index].realAnswerImg = img
    }

    fun updateMyQuestions(dataStoreRepository: DataStoreRepository) =
        viewModelScope.launch(Dispatchers.IO) {

            question.forEach {
                myAnswers += it.realAnswer + ","
            }
            val invateId = Utils.generateId(ME!!.username) + ME!!.id
            ME!!.myQuestions = myAnswers
            ME!!.inviteId = invateId
            dataStoreRepository.saveUser(Utils.convertUserToJson(ME!!))
            val response = resultRepo.updateMyQuestions(ME!!.id, invateId, myAnswers)
            //Log.d("updateMyQuestions", response.body().toString())
        }

    fun createResults() =
        viewModelScope.launch {
            var myAnswers = ""
            for (item in question) {
                myAnswers += item.realAnswer + ","
            }
            resultRepo.createResults(
                Constant.SENDER!!.id, ME!!.id, myAnswers, Constant.SENDER!!.token,
                ME!!.username
            )
        }


    fun getResults() = viewModelScope.launch(Dispatchers.IO) {
        val results = resultRepo.getResults(ME!!.id)
        if (results.isSuccessful) {
            Log.d("results", results.body().toString())


            resultsList.value = results.body()
        } else
            Log.d("results", results.message())

    }

}