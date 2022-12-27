package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.testfriends_jetpackcompose.data.User


@Composable
fun ChallengeDialog(user: User?, onConfirm: (Boolean) -> Unit?, onClick: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = {}, title = {
            if (user != null) {
                Text(
                    text = "Do you know ${user.username}?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
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
                    onClick = {
                        onConfirm(true)
                        onClick(false)
                    }) {
                    Text("Yes start Test",color=MaterialTheme.colorScheme.background)
                }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                onClick = {
                    onClick(false)
                }) {
                Text("cancel",color=MaterialTheme.colorScheme.onErrorContainer)
            }
        })

}



