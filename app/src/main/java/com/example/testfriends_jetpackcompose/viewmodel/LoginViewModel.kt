package com.example.testfriends_jetpackcompose.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.repository.LoginRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ALREADY_SIGN
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.isEmailValid
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: DataStoreRepository,
    val remoteRepo: LoginRepo
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var isAuth = mutableStateOf(false)
    var userState = mutableStateOf<User?>(null)
    var userNetworkResult: MutableLiveData<NetworkResults<User>> = MutableLiveData()

    private var auth: FirebaseAuth= Firebase.auth

    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    fun handleSignIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success $task")
                } else {
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

    fun saveUser(user: User) {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Token", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                user.token = task.result
                viewModelScope.launch(Dispatchers.IO) {
                    val response = remoteRepo.insetUser(user = user)
                    if (response.isSuccessful) {
                        user.id = response.body()!!
                        repository.saveUser(user = Utils.convertUserToJson(user = user))
                    }

                }
            })
        } catch (ex: Exception) {
            Log.d("Tokenfirebase", ex.toString())
        }
    }

    fun getUserSafe() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
            }
            //userNetworkResult.value = handle(response)

        } catch (ex: Exception) {
            userNetworkResult.value = NetworkResults.Error(ex.message)
        }

    }

    private fun handle(response: Response<String>): NetworkResults<String> {
        Log.d("resultsapi", response.toString())
        when {
            response.message().toString()
                .contains("Timeout") -> return NetworkResults.Error("Timeout")
            response.code() == 402 -> return NetworkResults.Error("Api Key Limited.")
            response.body()!!.isNullOrEmpty() -> return NetworkResults.Error("No Data Found")
            response.isSuccessful -> {

                val quotes = response.body()
                //CacheQuotes(quotes!!.results)
                return NetworkResults.Success(quotes!!)
            }
            else -> {
                Log.d("Error", response.message())
                return NetworkResults.Error(response.message())
            }
        }
    }
}



sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object Already : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}

