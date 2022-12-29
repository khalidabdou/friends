package com.example.testfriends_jetpackcompose.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.util.Constant
import com.example.testfriends_jetpackcompose.util.Utils

@Composable
fun NavigationDrawer(context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                Step(
                    text = stringResource(R.string.step1),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Step(
                    text = stringResource(R.string.step2),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Step(
                    text = stringResource(R.string.step3),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Step(
                    text = stringResource(R.string.step4),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Step(
                    text = stringResource(R.string.step5),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(10.dp)
                        .dashedBorder(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            radius = 6.dp
                        )
                        .clickable {
                            Utils.copyTextToClipboard(
                                text = "${Constant.ME!!.dynamicLink}",
                                context = context
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "tap to copy your link",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

}