package com.example.testfriends_jetpackcompose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testfriends_jetpackcompose.ui.theme.backgroundWhite
import com.example.testfriends_jetpackcompose.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.util.backgrounds
import kotlin.random.Random

@Composable
fun Avatar(name: String? = null, textColor: Color = backgroundWhite, enableText: Boolean = false) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(
                    CircleShape
                )
                .background(
                    if (name == null) backgroundWhite else backgrounds.colorList[Random.nextInt(
                        0,
                        backgrounds.colorList.size
                    )]
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = name!!.substring(0, 1).uppercase(),
                color = textColor,
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
            )

        }
        if (enableText)
            Text(name!!, style = MaterialTheme.typography.h6)
    }

}

@Composable
fun MyTextField(
    modifier: Modifier,
    text: String? = "",
    textStyle: TextStyle,
    placeholder: String,
    isPassword: Boolean = false,
    icon: ImageVector? = null,
    onChange: (String) -> Unit,
    onSearch: () -> Unit?,
) {
    //var textContent by rememberSaveable { mutableStateOf("") }
    TextField(
        textStyle = textStyle,
        value = text!!,

        trailingIcon = {
            if (icon != null)
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = darkGray,
                    modifier = Modifier.clickable {
                        Log.d("question", "cl")
                        onSearch()
                    })
        },
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

        modifier = modifier,
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
    progressBar: Boolean = false,
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
            Spacer(Modifier.width(50.dp))
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text, style = MaterialTheme.typography.h6, color = contentColor)
            Spacer(Modifier.weight(1f))
        } else Text(text, style = MaterialTheme.typography.h6, color = contentColor)
        if (progressBar)
            CircularProgressIndicator(color = contentColor, modifier = Modifier.size(20.dp))

    }
}

@Composable
fun CircularProgressIndicatorSample(color: Color = backgroundWhite) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = color)
    }
}

@Composable
fun MyText(text: String, style: TextStyle) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth(),
        style = style,
        color = darkGray,
        textAlign = TextAlign.Center
    )
}