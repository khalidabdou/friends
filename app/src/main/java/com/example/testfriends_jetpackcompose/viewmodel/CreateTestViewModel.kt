package com.example.testfriends_jetpackcompose.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.*
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant
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


@HiltViewModel
class CreateTestViewModel @Inject constructor(
    application: Application,
    @ApplicationContext val context: Context,
    private val resultRepo: ResultsRepo,
) : AndroidViewModel(application) {
    val questionFromJson = Constant.getJsonDataFromAsset(context = context, "question.json")
    val gson = Gson()
    val listPersonType = object : TypeToken<List<Question>>() {}.type
    var questions: List<Question> = gson.fromJson(questionFromJson, listPersonType)

    var userResults: User? = null


    var resultsList = mutableStateOf<NetworkResults<ListResults>?>(NetworkResults.Loading())
        private set
    //private val questions = MutableStateFlow<question>

    var myAnswers by mutableStateOf("")
        private set

    var result by mutableStateOf(0)
        private set

    var index by mutableStateOf(0)
    var question: List<Question> by mutableStateOf(questions)

    var challenge = mutableStateOf<NetworkResults<User>>(NetworkResults.Loading())
        private set

    fun incrementIndex(): Boolean {
        if (index < question.size - 1) {
            index++
            return false
        } else return true
    }

    fun decrementIndex() {
        if (index > 0) index--
    }

    fun setAnswer(answer: AnswerElement) {
        question[index].realAnswer = answer
    }

    fun cleanAnswers() {
        index = 0
        question.forEach { item ->
            item.realAnswer = AnswerElement("", "")
        }
    }

    fun updateMyQuestions(dataStoreRepository: DataStoreRepository) =
        viewModelScope.launch(Dispatchers.IO) {
            question.forEach {
                myAnswers += it.realAnswer.text + "*"
            }

            if (ME?.inviteId == null || ME?.inviteId == "")
                ME?.inviteId = Utils.generateId(ME!!.username) + ME!!.id
            ME!!.myQuestions = myAnswers
            Log.d("update", ME!!.toString())
            //dataStoreRepository.saveUser(Utils.convertUserToJson(ME!!))
            val response = resultRepo.updateMyQuestions(ME!!)
            val success = HandleResponse(response)
            if (success.handleResult() is NetworkResults.Success) {
                dataStoreRepository.saveUser(Utils.convertUserToJson(success.handleResult().data!!))
                Log.d("update", success.handleResult().data.toString())
            }


        }

    fun createResults() =
        viewModelScope.launch {

            var myAnswers = ""
            for (item in question) {
                myAnswers += item.realAnswer.text + "*"
            }
            Log.d("create", myAnswers)
            result = Utils.compareResults(SENDER!!.myQuestions, myAnswers)
            resultRepo.createResults(
                SENDER!!.id, ME!!.id, myAnswers, SENDER!!.token,
                ME!!.username
            )
        }


    fun getResults() = viewModelScope.launch(Dispatchers.IO) {
        if (!hasConnection) {
            resultsList.value = NetworkResults.Error("No Internet Connection")
            return@launch
        }
        if (resultsList.value is NetworkResults.Loading || resultsList.value is NetworkResults.Error) {
            resultsList.value = NetworkResults.Loading()
            val results = resultRepo.getResults(ME!!.id)
            resultsList.value = handleResults(results)
        }
    }

    private fun handleResults(response: Response<ListResults?>?): NetworkResults<ListResults> {
        when {
            response == null -> return NetworkResults.Error("No Data Found")
            response.body() == null -> return NetworkResults.Error("No Data Found")
            response.message().toString()
                .contains("timeout") -> return NetworkResults.Error("Timeout")
            response.code() == 402 -> return NetworkResults.Error("Api Key Limited.")
            response.isSuccessful -> {
                val list = response.body()
                return NetworkResults.Success(list!!)
            }
            else -> {
                Log.d("user", response.message())
                return NetworkResults.Error(response.message())
            }
        }
    }

    fun challenge(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("user", "begin")
            if (id == "")
                return@launch
            if (challenge.value is NetworkResults.Error || challenge.value is NetworkResults.Loading) {
                challenge.value = NetworkResults.Loading()
                val response = resultRepo.challenge(id)
                val handleUser = HandleResponse(response)
                challenge.value = handleUser.handleResult()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    val hasConnection = Utils.hasConnection(context as Application)
}