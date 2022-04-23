package com.example.testfriends_jetpackcompose.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.*
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.NetworkResults
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
            val success=HandleResponse(response)
            challenge.value=success.handleResult()
            //challenge.value = handleUser(response)

        }
    }

}