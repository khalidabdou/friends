package com.example.testfriends_jetpackcompose.viewmodel

import android.util.Log
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.NetworkResults
import retrofit2.Response

class HandleResponse<T>(private val response: Response<T?>?) {

    fun handleResult(): NetworkResults<T> {
        Log.d("user", response.toString())
        when {
            response == null -> return NetworkResults.Error("No Data Found")
            response.body() == null -> return NetworkResults.Error("No Data Found")
            response.message().toString()
                .contains("timeout") -> return NetworkResults.Error("Timeout")
            response.code() == 402 -> return NetworkResults.Error("Api Key Limited.")
            response.isSuccessful -> {
                val responseBody = response.body()
                return NetworkResults.Success(responseBody!!)
            }
            else -> {
                Log.d("user", response.message())
                return NetworkResults.Error(response.message())
            }
        }
    }

}

