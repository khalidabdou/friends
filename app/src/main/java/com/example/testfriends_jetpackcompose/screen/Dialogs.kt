package com.example.testfriends_jetpackcompose.screen

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.Language
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils.Companion.openStore
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel


@Composable
fun RateDialog(activity: Activity, onDismiss: () -> Unit) {
    AlertDialog(
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        title = {
            Row() {
                Text(
                    "Thank You \uD83D\uDC96",
                    modifier = Modifier.weight(1f)
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = "cancel",
                    tint = MaterialTheme.colorScheme.primary.copy(0.6f),
                    modifier = Modifier.clickable {
                        onDismiss()
                    }
                )
            }

        },
        text = {
            Text(text = "\uD83D\uDC96 Love the app? \uD83C\uDF1F Leave us a rating on the Play Store! \uD83D\uDE4F Your support means a lot to us. \uD83D\uDE4C Thanks for using our app!")
        },
        confirmButton = {
            Button(onClick = {
                openStore(activity)
            }) {
                Text(text = "Give 5 \uD83C\uDF1F")
            }

        },
        dismissButton = {
            Button(onClick = {
                activity.finish()
            }) {
                Text(text = "Exit")
            }
        },
        onDismissRequest = {}
    )
}


@Composable
fun ChallengeDialog(user: User?, onConfirm: (Boolean) -> Unit?, onClick: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = {}, title = {
            if (user != null) {
                androidx.compose.material.Text(
                    text = "${stringResource(id = R.string.do_you_know)} ${user.username}?",
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
                    androidx.compose.material.Text(
                        stringResource(R.string.start),
                        color = MaterialTheme.colorScheme.background
                    )
                }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                onClick = {
                    onClick(false)
                }) {
                androidx.compose.material.Text(
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        })

}

@Composable
fun LanguagesDialog(
    createTestViewModel: CreateTestViewModel,
    onSelect: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        title = {
            Text("Select a language")
        },
        text = {
            when (createTestViewModel.languageResponse.value) {
                is NetworkResults.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.user_not_found),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    onDismiss()
                    //openDialogLanguage.value = false
                }
                is NetworkResults.Success -> {
                    LanguageRadioGroup(createTestViewModel.languages) {
                        createTestViewModel.isLanguageSelected.value = true
                        onSelect(it)
                        onDismiss()
                    }
                }
                is NetworkResults.Loading -> {
                    createTestViewModel.getLanguages()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(315.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicatorSample(color = MaterialTheme.colorScheme.primary)
                    }
                }
                else -> {}
            }

        },
        confirmButton = {
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        },
        onDismissRequest = {}
    )
}


@Composable
fun AddQuestionDialog(
    viewModel: CreateTestViewModel,
    context: Context,
    onAdd: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, title = {

        },
        text = {
            AddQuestion(viewModel)
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    if (!viewModel.checkIsNotEmpty()) {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.empty),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    viewModel.addQuestion()
                    viewModel.resetQuestion()
                    onAdd()
                    //openDialog.value = false
                }) {
                Text(
                    stringResource(R.string.add),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                onClick = {
                    onDismiss()
                }) {
                androidx.compose.material.Text(
                    stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        })
}

@Composable
fun ShareTestDialog(link: String, onShare: () -> Unit, onCopy: () -> Unit) {
    var textShare =
        stringResource(R.string.share_text).replace(
            "www.example.com",
            link
        )
    textShare = textShare.replace(
        "www.example.com",
        link
    )
    textShare += "*" + com.example.testfriends_jetpackcompose.util.Constant.Companion.ME!!.inviteId + "*"
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.share_it))
        },
        text = {
            Text(
                text = textShare
            )
        },
        confirmButton = {
            IconButton(
                text = stringResource(R.string.share),
                icon = painterResource(id = R.drawable.ic_share)
            ) {
                onShare()
            }
        },
        dismissButton = {
            IconButton(
                text = stringResource(R.string.copy),
                icon = painterResource(id = R.drawable.ic_copy)
            ) {
                onCopy()
            }
        })

}

@Composable
fun IconButton(text: String, icon: Painter, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),

        ) {
        Icon(
            painter = icon,
            contentDescription = "",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
