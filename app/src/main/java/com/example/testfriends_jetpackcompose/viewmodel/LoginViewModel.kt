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
import com.example.testfriends_jetpackcompose.repository.LoginRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ALREADY_SIGN
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
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
    var userNetworkResult: MutableLiveData<NetworkResults<String>> = MutableLiveData()


    init {
        getUserSafe()
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

    fun saveUser(user: User) {
        //val user: User = User(0, "00000", email.value, 0)
        viewModelScope.launch {
            var rseponse = remoteRepo.insetUser(user = user)
            Log.d("Response",user.toString())
            //repository.saveUser(user = user)
        }
    }

    fun getUser() {
        var userInfo = repository.readUserInfo()
        Log.d("Auth_user", userInfo.toString())

    }



     fun getUserSafe() {


            try {
                val response = remoteRepo.getUser()
                userNetworkResult.value = handle(response)
                //Log.d("Tag_quote","handle quotes")
            } catch (ex: Exception) {
                userNetworkResult.value = NetworkResults.Error(ex.message)
            }

    }

    private fun handle(response: Response<String>): NetworkResults<String> {
        Log.d("resultsapi",response.toString())
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
                Log.d("Error",response.message(),)
                return NetworkResults.Error(response.message())
            }
        }
    }
}




enum class ApiStatus{
    SUCCESS,
    ERROR,
    LOADING
}

sealed class ApiResult <out T> (val status: ApiStatus, val data: T?, val message:String?) {

    data class Success<out R>(val _data: R?): ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String): ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception
    )

    data class Loading<out R>(val _data: R?, val isLoading: Boolean): ApiResult<R>(
        status = ApiStatus.LOADING,
        data = _data,
        message = null
    )
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object Already : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}

sealed class NetworkResults<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResults<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResults<T>(data, message)
    class Loading<T> : NetworkResults<T>()
    class Cached<T> : NetworkResults<T>()

}