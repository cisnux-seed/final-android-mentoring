package dev.cisnux.dicodingmentoring.ui.chatroom

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.data.realtime.Chat
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.withTimeFormat
import kotlinx.coroutines.launch

@Composable
fun RoomChatScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    chatRoomViewModel: ChatRoomViewModel = hiltViewModel()
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    val subscribeRoomChat by rememberUpdatedState(chatRoomViewModel::subscribeRoomChat)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(false)
        subscribeRoomChat()
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val shouldShowConnectionError by chatRoomViewModel.showConnectionError
    val realtimeChats by chatRoomViewModel.realtimeChats
    val message by chatRoomViewModel.message
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val currentUserId by chatRoomViewModel.currentUserId
    val receiver by chatRoomViewModel.receiver
    val lazyScrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    if (shouldShowConnectionError) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("no internet connection")
        }
    }

    ChatRoomContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        topBar = {
            receiver?.let {
                RoomTopBar(
                    fullName = it.fullName,
                    email = it.email,
                    context = context,
                    photoProfile = it.photoProfile,
                    navigateUp = {
                        chatRoomViewModel.closeSocket()
                        navigateUp()
                    }
                )
            }
        },
        body = { innerPadding ->
            ChatRoomBody(
                chats = realtimeChats?.chats ?: listOf(),
                message = message,
                textFieldScrollState = scrollState,
                onMessageChanged = chatRoomViewModel::onMessageChanged,
                onSender = { senderId ->
                    currentUserId == senderId
                },
                modifier = Modifier.padding(innerPadding),
                onSentMessage = {
                    if (message.isNotBlank()) {
                        chatRoomViewModel.onSentNewMessage()
                        coroutineScope.launch {
                            lazyScrollState.animateScrollToItem(0)
                        }
                    }
                },
                lazyScrollState = lazyScrollState
            )
        })

    BackHandler {
        chatRoomViewModel.closeSocket()
        navigateUp()
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRoomContentPreview() {
    ChatRoomContent(topBar = {
        RoomTopBar(fullName = "Fajra",
            email = "fajra@gmail.com",
            context = LocalContext.current,
            photoProfile = "",
            navigateUp = { })
    }, snackbarHostState = SnackbarHostState(), body = { innerPadding ->
        ChatRoomBody(
            modifier = Modifier.padding(innerPadding),
            chats = listOf(
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sit amet risus auctor, volutpat dui et, luctus nulla.",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sit amet risus.",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id2",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id2",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id2",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    createdAt = 2999991029,
                ),
                Chat(
                    id = "",
                    roomChatId = "",
                    senderId = "id1",
                    receiverId = "",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sit amet risus auctor, volutpat dui et, luctus nulla.",
                    createdAt = 2999991029,
                ),
            ),
            message = "",
            textFieldScrollState = rememberScrollState(),
            onMessageChanged = {},
            onSender = {
                "id2" == it
            },
            onSentMessage = {},
            lazyScrollState = rememberLazyListState()
        )
    })
}

@Composable
fun ChatRoomContent(
    topBar: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState,
    body: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier, topBar = topBar, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { innerPadding ->
        body(innerPadding)
    }
}

@Preview(showBackground = true)
@Composable
fun BubbleMessageUserIsMePreview() {
    DicodingMentoringTheme {
        BubbleMessage(
            message = "Halo", isUserMe = false, time = "23:24"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BubbleMessageUserIsNotMePreview() {
    DicodingMentoringTheme {
        BubbleMessage(
            message = "Halo guys david disini",
            isUserMe = false,
            time = "23:28",
        )
    }
}

@Composable
fun ChatRoomBody(
    chats: List<Chat>,
    modifier: Modifier = Modifier,
    message: String,
    textFieldScrollState: ScrollState,
    lazyScrollState: LazyListState,
    onMessageChanged: (String) -> Unit,
    onSender: (String) -> Boolean,
    onSentMessage: () -> Unit
) {
    var prevSenderId by rememberSaveable {
        mutableStateOf("")
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (messages, messageForm) = createRefs()

        LazyColumn(contentPadding = PaddingValues(bottom = 8.dp),
            modifier = modifier.constrainAs(messages) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                bottom.linkTo(messageForm.top)
                top.linkTo(parent.top)
                end.linkTo(parent.end, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            },
            state = lazyScrollState,
            reverseLayout = true,
            content = {
                items(chats) { chat ->
                    prevSenderId = chat.senderId
                    val currentSenderId = chat.senderId
                    val isUserMe = onSender(currentSenderId)
                    if (prevSenderId != currentSenderId) Spacer(modifier = Modifier.height(8.dp))
                    BubbleMessage(
                        message = chat.message,
                        time = chat.createdAt.withTimeFormat(),
                        isUserMe = isUserMe
                    )
                    Spacer(modifier = Modifier.height(if (prevSenderId != currentSenderId) 12.dp else 6.dp))
                }
            })
        OutlinedTextField(value = message,
            maxLines = 4,
            onValueChange = onMessageChanged,
            modifier = Modifier
                .verticalScroll(textFieldScrollState)
                .background(
                    color = MaterialTheme.colorScheme.background, shape = RectangleShape
                )
                .padding(8.dp)
                .constrainAs(messageForm) {
                    width = Dimension.fillToConstraints
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            placeholder = {
                Text(
                    text = "Write message....",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = MaterialTheme.shapes.extraLarge,
            trailingIcon = {
                IconButton(onClick = onSentMessage) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "send message",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            })
    }
}

@Composable
fun BubbleMessage(
    message: String,
    time: String,
    isUserMe: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val bubbleShape = if (isUserMe) RoundedCornerShape(4.dp, 20.dp, 4.dp, 20.dp)
    else RoundedCornerShape(20.dp, 4.dp, 20.dp, 4.dp)

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(if (isUserMe) Alignment.BottomEnd else Alignment.BottomStart)
        ) {
            Surface(
                color = backgroundBubbleColor,
                shape = bubbleShape,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.align(if (isUserMe) Alignment.End else Alignment.Start)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .padding(
                            horizontal = 10.dp, vertical = 6.dp
                        )
                        .wrapContentWidth()
                ) {
                    val (userMessageContainer, createdAt) = createRefs()
                    SelectionContainer(modifier = Modifier.constrainAs(userMessageContainer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                        Text(
                            textAlign = TextAlign.Start,
                            color = if (isUserMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = time,
                        textAlign = TextAlign.End,
                        color = if (isUserMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.constrainAs(createdAt) {
                            top.linkTo(userMessageContainer.bottom)
                            end.linkTo(parent.end, margin = 2.dp)
                        })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomTopBar(
    fullName: String,
    email: String,
    context: Context,
    photoProfile: String?,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
            ) {
                photoProfile?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(photoProfile).crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.circle_avatar_loading_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .border(1.dp, Color.White, CircleShape)
                            .clip(CircleShape)
                            .size(45.dp)
                    )
                } ?: Box(
                    modifier = Modifier
                        .border(1.dp, Color.White, CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        )
                        .size(45.dp)
                ) {
                    Text(
                        text = fullName[0].toString(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        })
}
