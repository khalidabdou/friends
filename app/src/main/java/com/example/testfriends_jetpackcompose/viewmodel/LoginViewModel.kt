package com.example.testfriends_jetpackcompose.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfriends_jetpackcompose.data.DataStoreRepository
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.repository.LoginRepo
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ALREADY_SIGN
import com.example.testfriends_jetpackcompose.util.HandleResponse
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.util.Utils.Companion.isEmailValid
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: DataStoreRepository,
    val remoteRepo: LoginRepo
) : ViewModel() {
    val TAG = "login"
    var email = mutableStateOf("")
    var firstname = mutableStateOf("")
    var lastname = mutableStateOf("")
    var password = mutableStateOf("")
    var userState = mutableStateOf<User?>(null)
    var userNetworkResult = mutableStateOf<NetworkResults<User>>(NetworkResults.NoAction())

    //var updateNetworkResult: MutableLiveData<NetworkResults<User>> = MutableLiveData()
    private var auth: FirebaseAuth = Firebase.auth

    //private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    //val authState: LiveData<AuthState> = _authState

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

    @RequiresApi(Build.VERSION_CODES.M)
    fun handleSignUp() {

        if (firstname.value == "") {
            userNetworkResult.value = NetworkResults.Error("first name is empty")
            return
        }
        if (!isEmailValid(email.value)) {
            userNetworkResult.value = NetworkResults.Error("Invalid email")
            return
        }

        if (lastname.value == "") {
            userNetworkResult.value = NetworkResults.Error("last name is invalid")
            return
        }
        if (password.value == "") {
            userNetworkResult.value = NetworkResults.Error("Invalid password")
            return
        }

        userNetworkResult.value = NetworkResults.Loading()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email.value, password.value
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "Email signup is successful")
                val user = User(
                    id = 0,
                    inviteId = "create your test and get your id",
                    username = "${firstname.value} ${lastname.value}",
                    token = "",
                    email = email.value,
                    image = "",
                    myQuestions = ""
                )
                saveUser(user)
            } else {
                task.exception?.let {
                    if (it.localizedMessage.contains(ALREADY_SIGN)) {
                        //Log.i(TAG, "Email signup failed with error")
                        viewModelScope.launch {
                            val response = remoteRepo.getUser(email = email.value)
                            val success = HandleResponse(response)
                            userNetworkResult.value = success.handleResult()
                            if (userNetworkResult.value is NetworkResults.Success) {
                                updateUser((userNetworkResult.value as NetworkResults.Success<User>).data!!)
                            } else if (userNetworkResult.value is NetworkResults.Error) {
                                userNetworkResult.value = NetworkResults.Error("error")
                            }
                        }
                        return@let
                    }
                    Log.i(TAG, "${it.localizedMessage}")
                    userNetworkResult.value = NetworkResults.Error("Invalid")
                }
            }
        }
    }

    fun updateUser(user: User) {
        userNetworkResult.value = NetworkResults.Loading()
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Token", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                user.token = task.result
                viewModelScope.launch {
                    val response = remoteRepo.updateUser(user = user)
                    val success = HandleResponse(response)
                    userNetworkResult.value = success.handleResult()
                    if (userNetworkResult.value is NetworkResults.Success) {
                        repository.saveUser(user = Utils.convertUserToJson(user = user))

                    }
                }
            })
        } catch (ex: Exception) {
            Log.d("Tokenfirebase", ex.toString())
        }


    }

    fun saveUser(user: User) {
        userNetworkResult.value = NetworkResults.Loading()
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
                        Log.d("login", user.email)
                    } else {
                        Log.d("login", response.toString())
                    }
                }
            })
        } catch (ex: Exception) {
            Log.d("Tokenfirebase", ex.toString())
        }
    }
}


