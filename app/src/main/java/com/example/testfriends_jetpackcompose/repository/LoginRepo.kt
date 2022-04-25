package com.example.testfriends_jetpackcompose.repository

import com.example.testfriends_jetpackcompose.data.Remote
import com.example.testfriends_jetpackcompose.data.User
import retrofit2.Response
import javax.inject.Inject

class LoginRepo @Inject constructor(private val remote: Remote) {


    suspend fun insetUser(user: User): Response<User?> {
        return remote.insertUser(user)
    }

    suspend fun updateUser(user: User): Response<User?> {
        return remote.updateUser(user)
    }

    suspend fun getUser(email: String): Response<User?> = remote.getUser(id = null, email = email)


}