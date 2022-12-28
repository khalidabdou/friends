package com.example.testfriends_jetpackcompose.screen


import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.Language
import com.example.testfriends_jetpackcompose.data.ListResults
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.example.testfriends_jetpackcompose.util.NetworkResults
import com.example.testfriends_jetpackcompose.util.Utils
import com.example.testfriends_jetpackcompose.viewmodel.AnswerTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.CreateTestViewModel
import com.example.testfriends_jetpackcompose.viewmodel.ResultsViewModel


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun HomeScreen(
    navController: NavHostController, createTestViewModel: CreateTestViewModel,
    answerTestViewModel: AnswerTestViewModel
) {

    val viewModelresults: ResultsViewModel = hiltViewModel()
    //var user = viewModelresults.userAuth
    val openDialog = remember { mutableStateOf(false) }
    val openDialogLanguage = remember { mutableStateOf(false) }
    val openDialogRate = remember { mutableStateOf(false) }
    val selectedLanguage = remember { mutableStateOf(Language.LANGUAGES[0]) }

    createTestViewModel.getResults()
    createTestViewModel.shareDynamcLink()
    var scaffoldState = rememberScaffoldState()
    val activity = (LocalContext.current as? Activity)


    Surface(color = MaterialTheme.colorScheme.background) {
        //Log.d("abc",ME.toString())
        NavigationDrawer(context = activity as Context)
        var navigateClick by remember { mutableStateOf(false) }
        val offSetAnim by animateDpAsState(
            targetValue = if (navigateClick) 500.dp else 0.dp,
            tween(1000)
        )

        val clipDp by animateDpAsState(
            targetValue = if (navigateClick) 60.dp else 0.dp,
            tween(1000)
        )
        val scaleAnim by animateFloatAsState(
            targetValue = if (navigateClick) 0.7f else 1.0f,
            tween(1000)
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleAnim)
                .offset(y = offSetAnim)
                .clip(RoundedCornerShape(clipDp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    navigateClick = false
                },

            backgroundColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppBar(
                    ME!!,
                    viewModelresults.search.value,
                    onShare = {
                        if (ME!!.myQuestions == null) {
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.create_your_quiz),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            return@AppBar
                        }
                        createTestViewModel.isLanguageSelected.value = false
                        createTestViewModel.setQuestion()
                        createTestViewModel.index = 0
                        navController.navigate(Screen.Create.route)
                    },
                    onDrawer = {
                        navigateClick = !navigateClick
                    },
                    search = { viewModelresults.setSearchText(it) },
                    onSearchClick = {
                        if (it == "") {
                            return@AppBar
                        }
                        answerTestViewModel.challenge(it)
                        openDialog.value = true
                    },
                    onLanguageChange = {
                        openDialogLanguage.value = true
                    },
                    language = selectedLanguage.value
                )
            },
            floatingActionButton = {
                if (createTestViewModel.dynamicLink == "")
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.secondary, onClick = {

                            openDialogLanguage.value = true
                            return@FloatingActionButton
                            createTestViewModel.cleanAnswers()
                            navController.navigate(Screen.Create.route)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_create),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                else
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.secondary, onClick = {
                            Utils.shareChallenge(
                                context = activity as Context,
                                createTestViewModel.dynamicLink
                            )
                        }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
            }
        ) {
            when (createTestViewModel.resultsList.value) {
                is NetworkResults.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        (createTestViewModel.resultsList.value as NetworkResults.Error<ListResults>).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResults.Success -> {
                    val result =
                        (createTestViewModel.resultsList.value as NetworkResults.Success<ListResults>).data!!.listResults
                    ResultsFriends(
                        paddingValues = it,
                        resultTest = result,
                        onClick = {
                            createTestViewModel.resultsByUser.value = it
                            navController.navigate(Screen.ResultsScreen.route)
                        })
                }
                is NetworkResults.Loading -> {
                    //Toast.makeText(LocalContext.current, "loading data ...", Toast.LENGTH_SHORT).show()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicatorSample(MaterialTheme.colorScheme.primary)
                    }
                }
            }
            if (openDialog.value) {
                when (answerTestViewModel.sender.value) {
                    is NetworkResults.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            stringResource(R.string.user_not_found),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        openDialog.value = false
                    }
                    is NetworkResults.Success -> {
                        val SENDER = answerTestViewModel.sender.value!!.data
                        if (SENDER!!.myQuestions.isEmpty()) {
                            Toast.makeText(
                                LocalContext.current,
                                "${SENDER!!.username}  ${stringResource(R.string.have_no_answers)} ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            openDialog.value = false
                        } else
                            ChallengeDialog(
                                user = answerTestViewModel.sender.value!!.data,
                                onClick = { openDialog.value = it },
                                onConfirm = {
                                    if (it) navController.navigate(Screen.Answer.route)
                                }
                            )
                    }
                    is NetworkResults.Loading -> {
                        ChallengeDialog(user = null, onClick = {
                            openDialog.value = true
                        }, onConfirm = {
                        })
                    }
                    else -> {}
                }
            }

            if (openDialogLanguage.value)
                LanguagesDialog(createTestViewModel, onDismiss = {
                    openDialogLanguage.value = false
                },
                    onSelect = {
                        selectedLanguage.value = it
                        createTestViewModel.setQuestion(it)
                        openDialogLanguage.value = false
                        navController.navigate(Screen.Create.route)
                    })

            if (openDialogRate.value)
                RateDialog(activity, onDismiss = {
                    openDialogRate.value = false
                })

        }
    }
    BackHandler {
        openDialogRate.value = true
        //activity?.finish()
    }
}

@Composable
fun LanguageRadioGroup(languages: List<Language>, onSelectedChange: (Language) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .height(300.dp)
    ) {
        items(languages.size) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        onSelectedChange(languages[it])
                    }
            ) {
                AsyncImage(
                    model = languages[it].image,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = languages[it].label, color = MaterialTheme.colorScheme.onPrimary)
            }

        }


    }

}


@Composable
fun AppBar(
    user: User,
    textSearch: String,
    language: Language,
    onDrawer: () -> Unit,
    onShare: () -> Unit,
    search: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onLanguageChange: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                //horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconCircle(icon = Icons.Default.Info, onClick = { onDrawer() })
                Text(
                    text = stringResource(R.string.profile),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconCircle(icon = Icons.Default.Edit, onClick = {
                    onShare()
                })

            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar), contentDescription = "",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${stringResource(R.string.welcome)} - ${ME!!.inviteId}",
                        color = MaterialTheme.colorScheme.primary.copy(0.7f)
                    )
                    Text(
                        text = "${user.username}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                MyTextField(
                    placeholder = "code ex: jh12 or name ",
                    text = textSearch,
                    icon = Icons.Default.Search,
                    isPassword = false,
                    onChange = { search(it) },
                    modifier = Modifier
                        .weight(3f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .padding(vertical = 0.dp),
                    onSearch = {
                        try {
                            onSearchClick(textSearch)
                        } catch (ex: Exception) {
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun IconCircle(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Icon(
        imageVector = icon,
        tint = MaterialTheme.colorScheme.primary.copy(0.5f),
        contentDescription = "",
        modifier = Modifier
            .size(27.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(0.7f),
                shape = CircleShape
            )
            .padding(4.dp)
            .clickable {
                onClick()
            }
    )
}

fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }

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
                                text = "${ME!!.dynamicLink}",
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

