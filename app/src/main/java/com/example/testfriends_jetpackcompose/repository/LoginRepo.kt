package com.example.testfriends_jetpackcompose.repository

import com.example.testfriends_jetpackcompose.data.Remote
import com.example.testfriends_jetpackcompose.data.User
import retrofit2.Response
import javax.inject.Inject

class LoginRepo @Inject constructor(private val remote: Remote) {

    suspend fun insetUser(user: User) = remote.insertUser(user)

    fun getUser(): Response<String> = remote.getUser()

}