package com.example.testfriends_jetpackcompose.util

import android.content.Context
import com.example.testfriends_jetpackcompose.admob.applovin
import com.example.testfriends_jetpackcompose.data.User
import java.io.IOException

class Constant {

    companion object {

        val PREFIX: String = "https://testfriends.page.link"
        val BASE_URL = "http://206.189.121.105:9000/"

        val ALREADY_SIGN = "address is already in use by another account"

        var ME: User? = null
        var SENDER: User? = null
        val applovinClass = applovin()

        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }
}