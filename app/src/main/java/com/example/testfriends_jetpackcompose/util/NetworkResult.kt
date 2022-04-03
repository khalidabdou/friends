package com.example.testfriends_jetpackcompose.util

sealed class NetworkResult <out T>{
    object Idle:NetworkResult<Nothing>()
    object Loading:NetworkResult<Nothing>()
    data class Success<T>(val data :T):NetworkResult<T>()
    data class Error (val error: Throwable): NetworkResult<Nothing>()
}