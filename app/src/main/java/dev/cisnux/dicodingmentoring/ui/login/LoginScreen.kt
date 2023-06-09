package dev.cisnux.dicodingmentoring.ui.login

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.components.AuthForm
import dev.cisnux.dicodingmentoring.ui.components.GoogleSignInButton
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.Failure
import dev.cisnux.dicodingmentoring.utils.UiState
import dev.cisnux.dicodingmentoring.utils.isEmail
import dev.cisnux.dicodingmentoring.utils.isPasswordSecure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun LoginContentPreview() {
    DicodingMentoringTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val email = ""
            val password = ""

            val scope = rememberCoroutineScope()
            val snackbarHostState = remember {
                SnackbarHostState()
            }
            val context = LocalContext.current

            LoginContent(
                email = email,
                password = password,
                onEmailQueryChanged = {},
                onPasswordQueryChanged = {},
                onAuthEmailPassword = { /*TODO*/ },
                onAuthGoogle = { /*TODO*/ },
                context = context,
                snackbarHostState = snackbarHostState,
                scope = scope,
                isLoading = false,
                navigateToSignUp = {},
                navigateToResetPassword = {},
                keyboardController = LocalSoftwareKeyboardController.current,
                scrollState = rememberScrollState()
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToRegisterProfile: (String) -> Unit,
    navigateToResetPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val email by viewModel.email
    val password by viewModel.password
    val currentUserId by viewModel.currentUserId.collectAsState(null)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scrollState = rememberScrollState()
    val launcher = rememberGoogleAuthLauncher(
        onLogin = { token ->
            viewModel.googleSignIn(token)
        }
    )
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val loginState by viewModel.loginState.collectAsStateWithLifecycle(UiState.Loading)

    when (loginState) {
        is UiState.Success -> {
            (loginState as UiState.Success).data?.let {
                viewModel.saveAuthSession(it, true)
            }
            navigateToHome()
        }

        is UiState.Error -> {
            (loginState as UiState.Error).error?.let { exception ->
                if (exception is Failure.NotFoundFailure) {
                    currentUserId?.let(navigateToRegisterProfile)
                } else {
                    LaunchedEffect(snackbarHostState) {
                        exception.message?.let { snackbarHostState.showSnackbar(it) }
                    }
                }
            }
        }

        else -> {}
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        LoginContent(
            email = email,
            password = password,
            onEmailQueryChanged = viewModel::onEmailQueryChanged,
            onPasswordQueryChanged = viewModel::onPasswordQueryChanged,
            onAuthEmailPassword = { viewModel.loginWithEmailAndPassword() },
            onAuthGoogle = { launcher.launch(viewModel.getGoogleIntent) },
            context = context,
            snackbarHostState = snackbarHostState,
            scope = scope,
            modifier = modifier.padding(innerPadding),
            isLoading = loginState is UiState.Loading,
            navigateToSignUp = navigateToRegister,
            navigateToResetPassword = navigateToResetPassword,
            keyboardController = keyboardController,
            scrollState = scrollState,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginContent(
    email: String,
    password: String,
    onEmailQueryChanged: (email: String) -> Unit,
    onPasswordQueryChanged: (password: String) -> Unit,
    onAuthEmailPassword: () -> Unit,
    onAuthGoogle: () -> Unit,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean,
    keyboardController: SoftwareKeyboardController?,
    navigateToSignUp: () -> Unit,
    navigateToResetPassword: () -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    AuthForm(
        email = email,
        password = password,
        onEmailQueryChanged = onEmailQueryChanged,
        onPasswordQueryChanged = onPasswordQueryChanged,
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Forgot Password?",
            modifier = Modifier
                .clickable(onClick = navigateToResetPassword)
                .fillMaxWidth(),
            textAlign = TextAlign.End,
            color = if(!isSystemInDarkTheme()) Color.Blue else Color.Cyan,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if (!email.isEmail()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_email_message))
                    }
                    return@Button
                }
                if (!password.isPasswordSecure()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_password_message))
                    }
                    return@Button
                }
                onAuthEmailPassword()
                keyboardController?.hide()
            },
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary),
            shape = MaterialTheme.shapes.small,
            enabled = !isLoading
        ) {
            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            AnimatedVisibility(visible = !isLoading) {
                Text(text = stringResource(id = R.string.sign_in))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        GoogleSignInButton(onAuth = onAuthGoogle)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(stringResource(R.string.signup_title_action))
                }
                withStyle(style = SpanStyle(color = if(!isSystemInDarkTheme()) Color.Blue else Color.Cyan)) {
                    append(stringResource(R.string.sign_up))
                }
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(onClick = navigateToSignUp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}