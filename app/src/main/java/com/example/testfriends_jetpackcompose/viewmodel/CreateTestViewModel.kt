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
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.HandleResponse
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class CreateTestViewModel @Inject constructor(
    val local: DataStoreRepository,
    application: Application,
    @ApplicationContext val context: Context,

    private val resultRepo: ResultsRepo,
) : AndroidViewModel(application) {

    //val questionFromJson = Constant.getJsonDataFromAsset(context = context, "question.json")
    val gson = Gson()
    //val listPersonType = object : TypeToken<List<Question>>() {}.type
    //var defaultQuestions: List<Question> = gson.fromJson(questionFromJson, listPersonType)

    //var user: User? = null
    var dynamicLink = ""

    var resultsList = mutableStateOf<NetworkResults<ListResults>?>(NetworkResults.Loading())
        private set

    var languageResponse = mutableStateOf<NetworkResults<Languages>?>(NetworkResults.Loading())
        private set

    var resultsByUser = mutableStateOf<ResultTest?>(null)
        private set

    var result by mutableStateOf(0)
        private set

    var index by mutableStateOf(0)
    var questions: ArrayList<Question> by mutableStateOf(ArrayList())
    var languages = mutableListOf<Language>()

    //add question
    var addedQ = mutableStateOf("")
    var realAnswer = mutableStateOf("")
    var wrong1 = mutableStateOf("")
    var wrong2 = mutableStateOf("")
    var wrong3 = mutableStateOf("")

    var addedQuestion = mutableStateOf(
        Question(
            id = 0,
            question = "",
            realAnswer = AnswerElement("", ""),
            answerSender = null,
            answer1 = AnswerElement("", ""),
            answer2 = AnswerElement("", ""),
            answer3 = AnswerElement("", ""),
            answer4 = AnswerElement("", ""),
        ),
    )

    var challenge = mutableStateOf<NetworkResults<User>>(NetworkResults.Loading())
        private set


    fun incrementIndex(): Boolean {
        if (index < questions.size - 1) {
            index++
            return false
        } else return true
    }

    fun decrementIndex() {
        if (index > 0) index--
    }


    fun setAnswerRealAnswer(answer: AnswerElement) {
        questions[index].realAnswer = answer
    }

    fun cleanAnswers() {
        index = 0
        questions.forEach { item ->
            item.realAnswer = AnswerElement("", "")
        }
    }

    fun addQuestion() {
        val question = Question(
            id = questions.size + 1,
            question = addedQ.value,
            realAnswer = AnswerElement(realAnswer.value, ""),
            answerSender = null,
            answer1 = AnswerElement(wrong1.value, ""),
            answer2 = AnswerElement(realAnswer.value, ""),
            answer3 = AnswerElement(wrong2.value, ""),
            answer4 = AnswerElement(wrong3.value, ""),
        )
        questions.add(question)

    }

    fun resetQuestion() {
        addedQ.value = ""
        realAnswer.value = ""
        wrong1.value = ""
        wrong2.value = ""
        wrong3.value = ""
    }

    fun updateMyQuestions() =
        viewModelScope.launch(Dispatchers.IO) {
            val dynamicLink = ME!!.dynamicLink
            if (ME?.inviteId == null || ME?.inviteId == "")
                ME?.inviteId = Utils.generateId(ME!!.username) + ME!!.id

            ME!!.myQuestions = gson.toJson(questions)

            //dataStoreRepository.saveUser(Utils.convertUserToJson(ME!!))
            val response = resultRepo.updateMyQuestions(ME!!)
            val success = HandleResponse(response)
            if (success.handleResult() is NetworkResults.Success) {
                val userObject = success.handleResult().data!!
                userObject.dynamicLink = dynamicLink
                val user = Utils.convertUserToJson(userObject)
                local.saveUser(user)
            }
        }


    @RequiresApi(Build.VERSION_CODES.M)
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

    fun setQuestion(language: Language) {
        questions =
            Utils.stringToQuestionArrayList(language.questions).toCollection(ArrayList<Question>())
    }

    fun setQuestion() {
        ME!!.myQuestions.let {
                questions = Utils.stringToQuestionArrayList(ME!!.myQuestions).toCollection(ArrayList())
        }


    }


    fun getLanguages() = viewModelScope.launch(Dispatchers.IO) {
        if (languageResponse.value is NetworkResults.Loading || languageResponse.value is NetworkResults.Error) {
            languageResponse.value = NetworkResults.Loading()
            val languagesList = resultRepo.getLanguage()
            languageResponse.value = handleResults(languagesList)
            languages = languageResponse.value!!.data!!.listLanguages.toMutableList()
            Log.d("lab", "sfdf")

            Log.d("lab", "${languageResponse?.value?.data?.listLanguages}")
        }
    }

    private fun <T> handleResults(response: Response<T?>?): NetworkResults<T> {
        return when {
            response == null -> NetworkResults.Error("No Data Found")
            response.body() == null -> NetworkResults.Error("No Data Found")
            response.message().toString()
                .contains("timeout") -> NetworkResults.Error("Timeout")
            response.code() == 402 -> NetworkResults.Error("Api Key Limited.")
            response.isSuccessful -> {
                val data = response.body()
                NetworkResults.Success(data!!)
            }
            else -> {
                Log.d("user", response.message())
                NetworkResults.Error(response.message())
            }
        }
    }


    fun challenge(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id == "")
                return@launch
            if (challenge.value is NetworkResults.Error || challenge.value is NetworkResults.Loading) {
                challenge.value = NetworkResults.Loading()
                val response = resultRepo.challenge(id)
                val handleUser = HandleResponse(response)
                challenge.value = handleUser.handleResult()
                var arra = Utils.stringToQuestionArrayList(challenge.value.data!!.myQuestions)
                Log.d("challenge", arra.toString())
            }
        }
    }


    fun saveDynamicLink(dynamicLink: String) = viewModelScope.launch {
        local.saveDynamicLink(dynamicLink)
    }

    fun shareDynamcLink() = viewModelScope.launch {
        local.getDynamicLink().collect { link ->
            dynamicLink = link
        }
    }

    fun deleteQuestiom(indexAt: Int) {
        questions.removeAt(indexAt)
        //index=questions.size
    }

    fun checkIsNotEmpty(): Boolean {
        return !(addedQ.value == "" || realAnswer.value == "" || wrong1.value == "" || wrong2.value == "" || wrong3.value == "")
    }


    @RequiresApi(Build.VERSION_CODES.M)
    val hasConnection = Utils.hasConnection(context as Application)
}