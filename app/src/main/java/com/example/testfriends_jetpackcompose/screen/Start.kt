package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray


@Composable
fun ChallengeDialog(user: User?, onConfirm: (Boolean) -> Unit?, onClick: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = {}, title = {
            if (user != null) {
                Text(
                    text = "Do you know ${user.username}",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        backgroundColor = darkGray,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (user == null)
                    CircularProgressIndicatorSample()
            }

        },

        confirmButton = {
            if (user != null)
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = backgroundWhite),
                    onClick = {
                        onConfirm(true)
                        onClick(false)
                    }) {
                    Text("start", color = darkGray)
                }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = darkGray),
                onClick = {
                    onClick(false)
                }) {
                Text("cancel")
            }
        })

}

@Composable
fun CircularProgressIndicatorSample(color: androidx.compose.ui.graphics.Color = backgroundWhite) {
    var progress by remember { mutableStateOf(0.1f) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = color)
    }
}

