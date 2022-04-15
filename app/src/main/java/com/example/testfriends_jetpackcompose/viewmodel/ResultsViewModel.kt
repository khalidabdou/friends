package com.example.testfriends_jetpackcompose.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.*
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import com.example.testfriends_jetpackcompose.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    val repository: DataStoreRepository,
    val resultsRepo: ResultsRepo
) :ViewModel() {

    val user1= User(id = 0, username = "", email = "jhon wils", img ="", token = "")
    val user2= User(id = 0, username = "", email = "jhon wils", img = "", token = "")
    val res1=ResultTest(0,user1,user2,"50%")
    private val listResult= listOf<ResultTest>(res1,res1,res1)
    var results: List<ResultTest> by mutableStateOf(listResult)
    var userAuth= mutableStateOf(user1)

    init {
        viewModelScope.launch {
            repository.readUserInfo().collect { user ->
                if (user == "")
                    return@collect
                Log.d("auth_user", Utils.convertToUser(user).toString())
                 userAuth.value = Utils.convertToUser(user)
            }
        }
    }


    fun updateMyQuestions(id:Int,questions:String)=viewModelScope.launch(Dispatchers.IO) {
        resultsRepo.updateMyQuestions(id,questions)
    }




}