package dev.cisnux.dicodingmentoring.ui.registerprofile

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.components.rememberGalleryLauncher
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterProfileScreen(
    id: String,
    onNavigateToHome: () -> Unit,
    takePictureFromGallery: (launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val fullName by viewModel.fullName
    val username by viewModel.username
    val job by viewModel.job
    val about by viewModel.about
    val pictureFromGallery by viewModel.pictureFromGallery

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val launcher = rememberGalleryLauncher(onSuccess = {
        viewModel.setPhotoFromGallery(uri = it)
    })
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val maxAboutLength = 320
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val createProfileState by viewModel.createProfileState.collectAsStateWithLifecycle()
    when (createProfileState) {
        is UiState.Error -> {
            (createProfileState as UiState.Error).error?.message?.let { message ->
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        is UiState.Success -> {
            viewModel.saveAuthSession(id, true)
            onNavigateToHome()
        }

        else -> {
        }
    }

    Scaffold(modifier = modifier, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { innerPadding ->
        RegisterProfileContent(
            fullName = fullName,
            username = username,
            job = job,
            onCreateProfile = {
                viewModel.onCreateProfile(id)
            },
            onProfilePicture = { takePictureFromGallery(launcher) },
            about = about,
            onFullNameQueryChanged = viewModel::onFullNameQueryChanged,
            onUsernameQueryChanged = viewModel::onUsernameQueryChanged,
            onJobQueryChanged = viewModel::onJobQueryChanged,
            onAboutQueryChanged = {
                viewModel.onAboutQueryChanged(it, maxAboutLength)
            },
            snackbarHostState = snackbarHostState,
            scope = coroutineScope,
            context = context,
            scrollState = scrollState,
            keyboardController = keyboardController,
            modifier = Modifier.padding(innerPadding),
            imageUri = pictureFromGallery,
            isLoading = createProfileState is UiState.Loading,
            aboutMaxLength = maxAboutLength
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun RegisterProfileContentPreview() {
    DicodingMentoringTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterProfileContent(
                fullName = "",
                username = "",
                job = "",
                about = "",
                onFullNameQueryChanged = {},
                onUsernameQueryChanged = {},
                onJobQueryChanged = {},
                onAboutQueryChanged = {},
                keyboardController = null,
                onCreateProfile = {},
                context = LocalContext.current,
                snackbarHostState = SnackbarHostState(),
                scope = rememberCoroutineScope(),
                onProfilePicture = {},
                scrollState = rememberScrollState(),
                isLoading = true,
                aboutMaxLength = 160,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterProfileContent(
    fullName: String,
    username: String,
    job: String,
    onCreateProfile: () -> Unit,
    onProfilePicture: () -> Unit,
    about: String,
    isLoading: Boolean,
    onFullNameQueryChanged: (String) -> Unit,
    onUsernameQueryChanged: (String) -> Unit,
    onJobQueryChanged: (String) -> Unit,
    onAboutQueryChanged: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
    aboutMaxLength: Int,
    modifier: Modifier = Modifier,
    imageUri: Uri? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onProfilePicture)
            )
        } ?: Image(
            painter = painterResource(id = R.drawable.profile_picture_placeholder),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable(onClick = onProfilePicture),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Set up your profile ✍🏼",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create an account so you can mentoring\nwith professional mentors",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            supportingText = {
                if (fullName.isNotEmpty() && fullName.isBlank()) Text(text = "Please enter your full name")
            },
            placeholder = {
                Text(text = "Enter your full name")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
            ),
            isError = fullName.isNotEmpty() && fullName.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null
                )
            },
            supportingText = {
                if (username.isNotEmpty() && username.isBlank())
                    Text(text = "Please enter your username")
            },
            placeholder = {
                Text(text = "Enter your username")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
            ),
            isError = username.isNotEmpty() && username.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = job,
            onValueChange = onJobQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    painter = painterResource(id = R.drawable.ic_outline_work_24),
                    contentDescription = null
                )
            },
            supportingText = {
                if (job.isNotEmpty() && job.isBlank()) Text(text = "Please enter your job")
            },
            placeholder = {
                Text(text = "Enter your job")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
            ),
            isError = job.isNotEmpty() && job.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = about,
            onValueChange = onAboutQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Tell us us more about yourself")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
            ),
            supportingText = {
                Text(
                    text = "${about.length} / $aboutMaxLength",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (fullName.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_full_name_message))
                    }
                    return@Button
                }
                if (username.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_username_message))
                    }
                    return@Button
                }
                if (job.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_job_message))
                    }
                    return@Button
                }
                if (about.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.invalid_about_message))
                    }
                    return@Button
                }
                onCreateProfile()
                keyboardController?.hide()
            },
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            if (!isLoading) Text(text = "Continue")
            else CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp)
            )
        }
    }
}