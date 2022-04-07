package com.example.testfriends_jetpackcompose.util

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import com.example.testfriends_jetpackcompose.data.User

import com.google.gson.Gson
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class Utils {

    companion object{
        fun isEmailValid(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun convertToUser(userString: String): User {
            //val json = """{"id": "0", "token": "123445556", "username" : "abdellah","img" : "000" }"""
            val gson = Gson()
            return gson.fromJson(userString, User::class.java)
        }

    }

}