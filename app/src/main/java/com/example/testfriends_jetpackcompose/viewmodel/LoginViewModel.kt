package com.example.testfriends_jetpackcompose.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ALREADY_SIGN
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: DataStoreRepository
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var isAuth = mutableStateOf(false)
    var userState = mutableStateOf<User?>(null)

    init {
        viewModelScope.launch {
            repository.readUserInfo().collect { user ->
                if (user == "")
                    return@collect
                Log.d("auth_user", user)
                val userAuth = Utils.convertToUser(user)
                //email.value = userAuth.username
                userState.value=userAuth
                isAuth.value = true
            }
        }
    }

    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    fun handleSignIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success $task")
                    //val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun handleSignUp() {
        if (!isEmailValid(email.value)) {
            _authState.value = AuthState.AuthError("Invalid email")
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email.value, password.value
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "Email signup is successful")
                _authState.value = AuthState.Success
                //saveUser()
            } else {
                task.exception?.let {
                    Log.i(TAG, "Email signup failed with error ${it.localizedMessage}")
                    if (it.localizedMessage.contains(ALREADY_SIGN)) {
                    } else
                        _authState.value = AuthState.AuthError(it.localizedMessage)
                }
            }
        }
    }

    fun saveUser(user: String) {
        //val user: User = User(0, "00000", email.value, 0)
        viewModelScope.launch {
            repository.saveUser(user = user)
        }
    }

    fun getUser() {
        var userInfo = repository.readUserInfo()
        Log.d("Auth_user", userInfo.toString())

    }
}


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object Already : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}