package com.example.testfriends_jetpackcompose.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch



@Composable
fun LoginScreen(navController: NavController) {

    val authState: LoginViewModel = hiltViewModel()

    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)

    // Equivalent of onActivityResult
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            //viewModel.signWithCredential(credential)
            Log.d("USERLOG",account.displayName)
            var user:User= User( 0,  account.displayName!!,  account.idToken!!,  account.email!!, account.photoUrl!!.toString())
            authState.saveUser(user.toString())
        } catch (e: ApiException) {
            Log.w("TAG", "Google sign in failed", e)
        }
    }



    var email = authState.email
    var password = authState.password

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
            .background(backgroundWhite)
            .padding(20.dp)
            .alpha(alphaAnim.value)
    ) {

        //Spacer(modifier = Modifier.height(60.dp))
        MyText(text = "Create an account", style = MaterialTheme.typography.h1)
        //Spacer(modifier = Modifier.height(60.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            MyTextField(
                placeholder = "Email address",
                isPassword = false,
                text = email.value,
                onChange = {
                    email.value = it
                })
            Spacer(modifier = Modifier.height(22.dp))
            MyTextField(
                placeholder = "password",
                isPassword = true,
                text = password.value,
                onChange = {
                    password.value = it
                })
        }

        //Spacer(modifier = Modifier.height(22.dp))
        MyButton(
            text = "Create an account",
            icon = null,
            background = Color.Black,
            contentColor = Color.White,
            onClickButton = {
                authState.handleSignUp()
                //authState.handleSignIn()
                //navController.navigate("Home_screen")
            }
        )
        //Spacer(modifier = Modifier.height(30.dp))
        MyText(text = "Already have an account", style = MaterialTheme.typography.h4)
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
                text = "Continue with Google",
                icon = R.drawable.google,
                background = Color.White,
                contentColor = Color.Black,
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
            MyButton(
                text = "Continue with facebook",
                icon = R.drawable.facebook,
                background = Color.White,
                contentColor = Color.Black,
                onClickButton = {}
            )
            Spacer(modifier = Modifier.height(22.dp))
            MyButton(
                text = "Continue with Apple",
                icon = R.drawable.apple,
                background = Color.White,
                contentColor = Color.Black,
                onClickButton = {}
            )
        }
    }
}

@Composable
fun MyTextField(
    text: String? = "",
    placeholder: String,
    isPassword: Boolean,
    onChange: (String) -> Unit,
) {
    //var textContent by rememberSaveable { mutableStateOf("") }
    TextField(
        textStyle = MaterialTheme.typography.h4,
        value = text!!,
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(color = Color.Black.copy(0.5f))
            )
        },
        maxLines = 1,
        singleLine = true,
        onValueChange = {
            onChange(it)
            //textContent = it

        },
        visualTransformation = if (!isPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = Color.Black, backgroundColor = Color.White
        ),
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email),
    )
}


@Composable
fun MyButton(
    text: String,
    icon: Int?,
    background: Color,
    contentColor: Color,
    onClickButton: () -> Unit
) {
    Button(
        onClick = { onClickButton() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            backgroundColor = background
        ),

        ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }
        Text(text, style = MaterialTheme.typography.h4, color = contentColor)
    }
}

@Composable
fun MyText(text: String, style: TextStyle) {

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth(),
        style = style,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
}


@Composable
@Preview
fun perv() {
    //LoginScreen()

}