package com.example.testfriends_jetpackcompose.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.*
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ResultsViewModel @Inject constructor(
    val resultsRepo: ResultsRepo
) : ViewModel() {

    var challenge = mutableStateOf<NetworkResults<User>>(NetworkResults.Loading())
        private set
    var search = mutableStateOf("")
    fun setSearchText(text: String) {
        search.value = text
    }
    fun challenge(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("user", "begin")
            if (id == "")
                return@launch
            challenge.value = NetworkResults.Loading()
            val response = resultsRepo.challenge(id)
            challenge.value = handleUser(response)

        }
    }

    private fun handleUser(response: Response<User?>?): NetworkResults<User> {
        Log.d("user", response.toString())
        when {
            response == null -> return NetworkResults.Error("No Data Found")
            response.body() == null -> return NetworkResults.Error("No Data Found")
            response.message().toString()
                .contains("timeout") -> return NetworkResults.Error("Timeout")
            response.code() == 402 -> return NetworkResults.Error("Api Key Limited.")
            response.isSuccessful -> {
                val user = response.body()
                Log.d("user", response.body()!!.username)
                SENDER = user
                return NetworkResults.Success(user!!)
            }
            else -> {
                Log.d("user", response.message())
                return NetworkResults.Error(response.message())
            }
        }
    }


}