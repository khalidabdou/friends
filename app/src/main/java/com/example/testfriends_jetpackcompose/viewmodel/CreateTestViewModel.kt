package com.example.testfriends_jetpackcompose.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant
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
    private val resultRepo: ResultsRepo
) :

    ViewModel() {
    val questionFromJson = Constant.getJsonDataFromAsset(context = context, "question.json")
    val gson = Gson()
    val listPersonType = object : TypeToken<List<Question>>() {}.type
    var questions: List<Question> = gson.fromJson(questionFromJson, listPersonType)
    //private val questions = MutableStateFlow<question>

    //var questions by mutableStateOf(_questions)


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

    fun updateMyQuestions(id: Int, questions: String) = viewModelScope.launch(Dispatchers.IO) {
        //Log.d("updateMyQuestions","begin")
        val response=resultRepo.updateMyQuestions(id, questions)
        Log.d("updateMyQuestions",response.body().toString())
    }

}