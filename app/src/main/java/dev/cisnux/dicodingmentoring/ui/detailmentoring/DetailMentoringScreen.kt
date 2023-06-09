package dev.cisnux.dicodingmentoring.ui.detailmentoring

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.UserType
import dev.cisnux.dicodingmentoring.utils.withDateFormat
import dev.cisnux.dicodingmentoring.utils.withTimeFormat

@Composable
fun DetailMentoringScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToRoomChat: (roomChatId: String) -> Unit,
    detailMentoringViewModel: DetailMentoringViewModel = hiltViewModel(),
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(false)
        detailMentoringViewModel.subscribeDetailMentoring()
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val shouldShowConnectionError by detailMentoringViewModel.showConnectionError
    val shouldShowLoading by detailMentoringViewModel.shouldShowLoading
    val detailMentoring by detailMentoringViewModel.detailMentoring
    val isAccepted by detailMentoringViewModel.isAccepted
    val userType by detailMentoringViewModel.userType
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    if (shouldShowConnectionError) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("no internet connection")
        }
    }

    val checkBoxDurations = detailMentoringViewModel.checkBoxDurations
    DetailMentoringContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        body = { innerPadding ->
            if (!shouldShowLoading) {
                DetailMentoringBody(
                    roomChatId = detailMentoring!!.roomChatId,
                    onAccept = {
                        detailMentoringViewModel.onAccepted(true)
                        detailMentoringViewModel.onUpdateDetailMentoring()
                    },
                    checkBoxDurations = checkBoxDurations,
                    onCheckedChanged = { duration ->
                        detailMentoringViewModel.onCheckedChanged(duration)
                    },
                    onVideoChat = { /*TODO*/ },
                    title = detailMentoring!!.title,
                    mentoringDate = detailMentoring!!.eventTime.withDateFormat(),
                    mentoringTime = detailMentoring!!.eventTime.withTimeFormat(),
                    description = detailMentoring!!.description,
                    isCompleted = detailMentoring!!.isCompleted,
                    isAccepted = isAccepted,
                    showChatButton = detailMentoring!!.roomChatId != null,
                    mentorFullName = detailMentoring!!.mentor.fullName,
                    menteeFullName = detailMentoring!!.mentee.fullName,
                    mentorPhotoProfile = detailMentoring?.mentor?.photoProfile,
                    menteePhotoProfile = detailMentoring?.mentee?.photoProfile,
                    mentorEmail = detailMentoring!!.mentor.email,
                    menteeEmail = detailMentoring!!.mentee.email,
                    scrollState = scrollState,
                    context = context,
                    userType = userType,
                    navigateUp = {
                        detailMentoringViewModel.closeSocket()
                        navigateUp()
                    },
                    showVideoChatButton = detailMentoring!!.videoChatLink != null,
                    navigateToRoomChat = navigateToRoomChat
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                }
            }
        })

    BackHandler {
        detailMentoringViewModel.closeSocket()
        navigateUp()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailMentoringContentPreview() {
    DicodingMentoringTheme {
        DetailMentoringContent(snackbarHostState = SnackbarHostState(), body = { innerPadding ->
            DetailMentoringBody(
                roomChatId = "",
                title = "Migration from XML to Jetpack Compose",
                mentoringDate = "Saturday, July 23, 2025",
                mentoringTime = "17:00",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed commodo ex ac quam fermentum, in tempus est iaculis. Nulla facilisi. Ut eu ex vitae nulla ultricies volutpat. Vestibulum efficitur semper nunc, eu lacinia nisi commodo non. Nam a metus id nisi efficitur fermentum. Pellentesque non aliquet nisi. Duis ac turpis volutpat, scelerisque libero eget, rhoncus dolor. ",
                isCompleted = false,
                isAccepted = false,
                mentorFullName = "Eren Jaeger",
                menteeFullName = "Levi Ackerman",
                mentorEmail = "eren@gmail.com",
                menteeEmail = "leren@gmail.com",
                context = LocalContext.current,
                scrollState = rememberScrollState(),
                onAccept = {},
                onVideoChat = {},
                modifier = Modifier.padding(innerPadding),
                checkBoxDurations = listOf(
                    10,
                    15,
                    20
                ),
                onCheckedChanged = { },
                showChatButton = true,
                userType = UserType.Mentee,
                navigateUp = {},
                showVideoChatButton = true,
                navigateToRoomChat = {}
            )
        })
    }
}

@Composable
fun DetailMentoringContent(
    snackbarHostState: SnackbarHostState,
    body: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }, modifier = modifier
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
fun DetailMentoringBody(
    onAccept: (Boolean) -> Unit,
    roomChatId: String?,
    navigateUp: () -> Unit,
    checkBoxDurations: List<Int>,
    onCheckedChanged: (Int) -> Unit,
    onVideoChat: () -> Unit,
    title: String,
    mentoringDate: String,
    mentoringTime: String,
    description: String,
    isCompleted: Boolean,
    isAccepted: Boolean,
    mentorFullName: String,
    menteeFullName: String,
    showChatButton: Boolean,
    showVideoChatButton: Boolean,
    mentorEmail: String,
    menteeEmail: String,
    scrollState: ScrollState,
    context: Context,
    userType: UserType,
    navigateToRoomChat: (roomChatId: String) -> Unit,
    modifier: Modifier = Modifier,
    mentorPhotoProfile: String? = null,
    menteePhotoProfile: String? = null,
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    var selected by remember {
        mutableIntStateOf(checkBoxDurations.first())
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 16.dp))
            .verticalScroll(scrollState)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = navigateUp) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_date_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = mentoringDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = mentoringTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isAccepted) R.drawable.ic_check_circle_24
                        else R.drawable.ic_remove_circle_24
                    ),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAccepted) "Accepted" else if (isCompleted) "Mentoring session is completed" else "Not accepted yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mentor",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            UserCard(
                fullName = mentorFullName,
                email = mentorEmail,
                context = context,
                isNotCurrentUser = userType != UserType.Mentor,
                showChatButton = showChatButton,
                photoProfile = mentorPhotoProfile,
                onVideoChat = onVideoChat,
                showVideoChatButton = showVideoChatButton,
                onChat = {
                    roomChatId?.let {
                        navigateToRoomChat(
                            roomChatId,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mentee",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            UserCard(
                fullName = menteeFullName,
                email = menteeEmail,
                context = context,
                isNotCurrentUser = userType != UserType.Mentee,
                showChatButton = showChatButton,
                photoProfile = menteePhotoProfile,
                onVideoChat = onVideoChat,
                showVideoChatButton = showVideoChatButton,
                onChat = {
                    roomChatId?.let {
                        navigateToRoomChat(
                            roomChatId,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(visible = !isAccepted && userType == UserType.Mentor) {
                Button(
                    onClick = {
                        openDialog = true
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Accept")
                }
            }
        }
        if (openDialog) {
            Dialog(onDismissRequest = { openDialog = false }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp)
                ) {
                    Text("Choose durations", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.onBackground, thickness = 0.5.dp)
                    checkBoxDurations.forEach { checkBoxDuration ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .selectableGroup()
                                .fillMaxWidth()
                                .clickable {
                                    selected = checkBoxDuration
                                    onCheckedChanged(checkBoxDuration)
                                }
                        ) {
                            RadioButton(
                                selected = checkBoxDuration == selected,
                                onClick = {
                                    selected = checkBoxDuration
                                    onCheckedChanged(checkBoxDuration)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.onSurface,
                                    unselectedColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.semantics { contentDescription = title }
                            )
                            Text(
                                text = "$checkBoxDuration minutes",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Divider(color = MaterialTheme.colorScheme.onSurface, thickness = 0.5.dp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            openDialog = false
                        }) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = {
                            onAccept(true)
                            openDialog = false
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    fullName: String,
    email: String,
    context: Context,
    isNotCurrentUser: Boolean,
    showChatButton: Boolean,
    showVideoChatButton: Boolean,
    onVideoChat: () -> Unit,
    onChat: () -> Unit,
    modifier: Modifier = Modifier,
    photoProfile: String? = null,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            photoProfile?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoProfile)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.circle_avatar_loading_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .border(1.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                        .size(30.dp)
                )
            } ?: Box(
                modifier = Modifier
                    .border(1.dp, Color.White, CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .size(30.dp)
            ) {
                Text(
                    text = fullName[0].toString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = fullName, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = email, style = MaterialTheme.typography.bodySmall)
            }
        }
        Row(modifier = modifier) {
            AnimatedVisibility(visible = showChatButton && isNotCurrentUser) {
                IconButton(onClick = onChat) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chat_24),
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(visible = showVideoChatButton && isNotCurrentUser) {
                IconButton(onClick = onVideoChat) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_video_chat_24),
                        contentDescription = null
                    )
                }
            }
        }
    }
}



