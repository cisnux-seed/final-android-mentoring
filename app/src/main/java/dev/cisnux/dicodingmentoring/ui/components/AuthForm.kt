package dev.cisnux.dicodingmentoring.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.isEmail
import dev.cisnux.dicodingmentoring.utils.isPasswordSecure

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6_pro")
@Composable
fun AuthFormPreview() {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    DicodingMentoringTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AuthForm(
                email = email,
                password = password,
                onEmailQueryChanged = { email = it },
                onPasswordQueryChanged = { password = it },
            )
        }
    }
}

@Composable
fun AuthForm(
    email: String,
    password: String,
    onEmailQueryChanged: (email: String) -> Unit,
    onPasswordQueryChanged: (password: String) -> Unit,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth),
            contentDescription = null,
            modifier = Modifier.size(350.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailQueryChanged,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
            },
            maxLines = 1,
            singleLine = true,
            supportingText = {
                if (email.isNotEmpty() && !email.isEmail())
                    Text(text = stringResource(R.string.invalid_email_message))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
            ),
            label = {
                Text(text = stringResource(R.string.email_hint))
            },
            isError = email.isNotEmpty() && !email.isEmail()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordQueryChanged,
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            maxLines = 1,
            singleLine = true,
            supportingText = {
                if (password.isNotEmpty() && !password.isPasswordSecure())
                    Text(text = stringResource(R.string.invalid_password_message))
            },
            label = {
                Text(text = stringResource(R.string.password_hint))
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter =
                        painterResource(
                            id = if (isPasswordVisible)
                                R.drawable.icon_visibility_24
                            else R.drawable.icon_visibility_off_24
                        ),
                        contentDescription = if (isPasswordVisible)
                            stringResource(R.string.hide_your_password)
                        else stringResource(R.string.show_your_password)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
            ),
            isError = password.isNotEmpty() && !password.isPasswordSecure()
        )
        if (content != null) {
            content()
        }
    }
}
