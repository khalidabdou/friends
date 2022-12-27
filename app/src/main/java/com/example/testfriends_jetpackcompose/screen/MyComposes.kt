package com.example.testfriends_jetpackcompose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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

import com.example.testfriends_jetpackcompose.util.backgrounds
import kotlin.random.Random

@Composable
fun Avatar(
    name: String? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(
                    CircleShape
                )
                .background(
                    if (name == null) MaterialTheme.colorScheme.background else backgrounds.colorList[Random.nextInt(
                        0,
                        backgrounds.colorList.size
                    )]
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = name!!.substring(0, 1).uppercase(),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier,
    text: String? = "",
    placeholder: String,
    isPassword: Boolean = false,
    icon: ImageVector? = null,
    onChange: (String) -> Unit,
    onSearch: () -> Unit?,
) {
    //var textContent by rememberSaveable { mutableStateOf("") }
    TextField(
        textStyle = MaterialTheme.typography.bodyMedium,
        value = text!!,
        trailingIcon = {
            if (icon != null)
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onSearch()
                    })
            else Box(modifier = Modifier.size(0.dp))

        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        maxLines = 1,
        singleLine = true,
        onValueChange = {
            onChange(it)
        },

        visualTransformation = if (!isPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldQuestion(
    text: String? = "",
    placeholder: String,
    modifier: Modifier,
    onChange: (String) -> Unit,
) {

    // Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color.Green))
    TextField(
        textStyle = MaterialTheme.typography.bodyMedium,
        value = text!!,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        maxLines = 1,
        singleLine = true,
        onValueChange = {
            onChange(it)
        },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = MaterialTheme.colorScheme.primary,
        ),
    )

}


@Composable
fun MyButton(
    text: String,
    icon: Int?,
    progressBar: Boolean = false,
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
            Text(text, style = MaterialTheme.typography.bodyMedium, color = contentColor)
            Spacer(Modifier.weight(1f))
        } else
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        if (progressBar)
            CircularProgressIndicator(color = contentColor, modifier = Modifier.size(20.dp))

    }
}

@Composable
fun CircularProgressIndicatorSample(color: Color = MaterialTheme.colorScheme.background) {
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
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}