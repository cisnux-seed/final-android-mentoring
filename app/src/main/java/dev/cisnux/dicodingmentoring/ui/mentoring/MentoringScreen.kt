package dev.cisnux.dicodingmentoring.ui.mentoring

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.domain.models.GetMentoringSession
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.withDateFormat
import dev.cisnux.dicodingmentoring.utils.withTimeFormat

@Composable
fun MentoringScreen(
    mainViewModel: MainViewModel,
    navigateToDetailMentoring: (String) -> Unit,
    modifier: Modifier = Modifier,
    mentoringViewModel: MentoringViewModel = hiltViewModel(),
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(true)
        mentoringViewModel.subscribeMentoringSessions()
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val showConnectionError by mentoringViewModel.showConnectionError
    val mentoringSessions by mentoringViewModel.getMentoringSessions.collectAsStateWithLifecycle()

    if (showConnectionError) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                "no internet connection"
            )
        }
    }

    MentoringContent(snackbarHostState = snackbarHostState, body = { innerPadding ->
        MentoringBody(
            mentoringSessions = mentoringSessions,
            modifier = modifier.padding(innerPadding),
            navigateToDetailMentoring = navigateToDetailMentoring
        )
    })
}

@Preview(showBackground = true)
@Composable
fun MentoringContentPreview() {
    Surface {
        DicodingMentoringTheme {
            MentoringContent(
                snackbarHostState = SnackbarHostState(),
                body = { innerPadding ->
                    MentoringBody(
                        mentoringSessions = listOf(
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose aaaaaaaaaaaaaaaw",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                            GetMentoringSession(
                                id = "a123",
                                title = "Hilt pada Jetpack Compose",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum dolor a fringilla eleifend. Curabitur at sagittis nisl. In venenatis euismod odio sed suscipit. Integer ullamcorper tortor vitae tellus fermentum, a consequat odio pellentesque. Mauris et mauris at felis tristique accumsan.",
                                eventTime = 1788692032000,
                                isOnlyChat = false
                            ),
                        ),
                        navigateToDetailMentoring = {},
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MentoringContentPreviewWhenEmpty() {
    Surface {
        DicodingMentoringTheme {
            MentoringContent(
                snackbarHostState = SnackbarHostState(),
                body = { innerPadding ->
                    MentoringBody(
                        mentoringSessions = emptyList(),
                        modifier = Modifier.padding(innerPadding),
                        navigateToDetailMentoring = {}
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentoringContent(
    snackbarHostState: SnackbarHostState,
    body: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Mentoring Sessions")
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
fun MentoringBody(
    mentoringSessions: List<GetMentoringSession>,
    navigateToDetailMentoring: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (mentoringSessions.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_mentoring_session),
                contentDescription = null,
                modifier = Modifier.size(170.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No Mentoring Sessions",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
            content = {
                items(mentoringSessions) { mentoringSession ->
                    MentoringCard(
                        title = mentoringSession.title,
                        description = mentoringSession.description,
                        mentoringDate = mentoringSession.eventTime.withDateFormat(),
                        mentoringTime = mentoringSession.eventTime.withTimeFormat(),
                        isOnlyChat = mentoringSession.isOnlyChat,
                        onClick = {
                            navigateToDetailMentoring(mentoringSession.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MentoringCardPreview() {
    DicodingMentoringTheme {
        MentoringCard(
            title = "Hilt pada Jetpack Compose",
            description = "saya ingin mengunnakan hilt pada jetpack compose",
            mentoringDate = "Sabtu, 27 Januari 2023",
            mentoringTime = "17:30",
            isOnlyChat = false,
            onClick = {}
        )
    }
}

@Composable
fun MentoringCard(
    title: String,
    description: String,
    mentoringDate: String,
    mentoringTime: String,
    isOnlyChat: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(7f)
                )
                Icon(
                    painter = painterResource(
                        id = if (isOnlyChat) R.drawable.ic_chat_24
                        else R.drawable.ic_video_chat_24
                    ),
                    contentDescription = null,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_date_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = mentoringDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = mentoringTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
