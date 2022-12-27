package com.example.testfriends_jetpackcompose.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun LoginScreen(navController: NavController) {

    val authState: LoginViewModel = hiltViewModel()

    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)




    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account = task.getResult(ApiException::class.java)!!
                //val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

                val user = User(
                    id = 0,
                    inviteId = "",
                    username = account.displayName!!,
                    token = "",
                    email = account.email!!,
                    image = account.photoUrl.toString(),
                    myQuestions = "",
                    dynamicLink = ""
                )
                authState.saveUser(user)
                //Toast.makeText(context, account.displayName, Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                //Log.w("TAG", "Google sign in failed", e)
            }
        }

    val email = authState.email
    val password = authState.password
    val firstName = authState.firstname
    val lastName = authState.lastname

    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .alpha(alphaAnim.value)
    ) {
        //Spacer(modifier = Modifier.height(60.dp))
        MyText(text = stringResource(R.string.create_an_acount), style = MaterialTheme.typography.titleLarge)
        //Spacer(modifier = Modifier.height(60.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                MyTextField(
                    placeholder = "First name",
                    isPassword = false,
                    text = firstName.value,
                    onChange = {
                        firstName.value = it
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .weight(1f)
                        .background(Color.White),
                     onSearch = {}
                )
                Spacer(modifier = Modifier.width(5.dp))
                MyTextField(
                    placeholder = "Last name",
                    isPassword = false,
                    text = lastName.value,
                    onChange = {
                        lastName.value = it
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .weight(1f),
                     onSearch = {}
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            MyTextField(
                placeholder = "Email address",
                isPassword = false,
                text = email.value,
                onChange = {
                    email.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
               onSearch = {}
            )
            Spacer(modifier = Modifier.height(22.dp))
            MyTextField(
                placeholder = "password",
                isPassword = true,
                text = password.value,
                onChange = {
                    password.value = it
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White), onSearch = {}
            )
        }

        //Spacer(modifier = Modifier.height(22.dp))
        MyButton(
            text = stringResource(R.string.create_an_acount),
            icon = null,
            progressBar= authState.userNetworkResult.value is NetworkResults.Loading,
            contentColor = MaterialTheme.colorScheme.primaryContainer,
            onClickButton = {
                authState.handleSignUp()
                if (authState.userNetworkResult.value is NetworkResults.Error) {
                    Toast.makeText(
                        context,
                        (authState.userNetworkResult.value as NetworkResults.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //authState.handleSignIn()
                //navController.navigate("Home_screen")
            }
        )
        //Spacer(modifier = Modifier.height(30.dp))
        MyText(text = stringResource(R.string.already_have_an_ccount), style = MaterialTheme.typography.bodyLarge)
        //Spacer(modifier = Modifier.height(30.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )
        //Spacer(modifier = Modifier.height(22.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            MyButton(
                text = stringResource(R.string.with_google),
                icon = R.drawable.google,
                contentColor = MaterialTheme.colorScheme.primaryContainer,
                onClickButton = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}
