package com.example.testfriends_jetpackcompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.Constant.Companion.SENDER
import com.example.testfriends_jetpackcompose.util.Utils
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Screen.Welcome.route)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            repository.readUserInfo().collect { user ->
                if (user == "") {
                    _startDestination.value = Screen.Welcome.route
                } else {
                    ME = Utils.convertToUser(user)
                    if (SENDER!=null)
                        _startDestination.value = Screen.Answer.route
                    else
                        _startDestination.value = Screen.Home.route
                }
            }
            _isLoading.value = false
        }
    }

}