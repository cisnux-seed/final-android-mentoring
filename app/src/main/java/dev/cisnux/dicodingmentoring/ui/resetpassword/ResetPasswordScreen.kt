package dev.cisnux.dicodingmentoring.ui.resetpassword

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen(
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val email by viewModel.email
    val isValidEmail by viewModel.isValidEmail

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val resetPasswordState by viewModel.resetPasswordState.collectAsStateWithLifecycle(UiState.Loading)

    when (resetPasswordState) {
        is UiState.Success -> {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(context.getString(R.string.reset_success_message))
                delay(400L)
                navigateToSignIn()
            }
        }

        is UiState.Error -> {
            (resetPasswordState as UiState.Error).error?.message?.let { message ->
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        else -> {}
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        ResetPasswordBody(
            onResetPassword = viewModel::resetPassword,
            navigateToSignIn = navigateToSignIn,
            email = email,
            scope = scope,
            context = context,
            onEmailQueryChanged = viewModel::onEmailQueryChanged,
            isValidEmail = isValidEmail,
            snackbarHostState = snackbarHostState,
            modifier = modifier.padding(innerPadding),
            keyboardController = keyboardController
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
fun ResetPasswordBodyPreview() {
    DicodingMentoringTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            ResetPasswordBody(
                onResetPassword = { /*TODO*/ },
                email = "",
                onEmailQueryChanged = {},
                isValidEmail = true,
                scope = rememberCoroutineScope(),
                snackbarHostState = SnackbarHostState(),
                context = LocalContext.current,
                navigateToSignIn = {},
                keyboardController = LocalSoftwareKeyboardController.current
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordBody(
    onResetPassword: () -> Unit,
    navigateToSignIn: () -> Unit,
    email: String,
    scope: CoroutineScope,
    context: Context,
    onEmailQueryChanged: (email: String) -> Unit,
    isValidEmail: Boolean,
    keyboardController: SoftwareKeyboardController?,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.reset_password),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.reset_password_display),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.reset_password_message_display),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailQueryChanged,
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
            },
            maxLines = 1,
            supportingText = {
                if (!isValidEmail) Text(text = stringResource(R.string.invalid_email_message))
            },
            label = {
                Text(text = stringResource(R.string.email_hint))
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary
            ),
            isError = !isValidEmail
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (!isValidEmail) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_email_message))
                    }
                    return@Button
                }
                onResetPassword()
                keyboardController?.hide()
            },
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text = stringResource(R.string.reset_password_title))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = navigateToSignIn),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Back to Sign In",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
