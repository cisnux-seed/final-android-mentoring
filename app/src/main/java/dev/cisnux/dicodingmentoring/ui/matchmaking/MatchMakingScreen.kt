package dev.cisnux.dicodingmentoring.ui.matchmaking

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.domain.models.GetMentor
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.components.MentorCard
import dev.cisnux.dicodingmentoring.ui.home.HomeBody
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.UiState

@Composable
fun MatchMakingScreen(
    mainViewModel: MainViewModel,
    navigateToDetailMentor: (id: String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    matchMakingViewModel: MatchMakingViewModel = hiltViewModel()
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(false)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current
    val mentorsState by matchMakingViewModel.mentorsState

    MatchMakingContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        navigateUp = navigateUp,
        body = { innerPadding ->
            when (mentorsState) {
                is UiState.Success -> {
                    MatchMakingBody(
                        context = context,
                        mentors = (mentorsState as UiState.Success<List<GetMentor>>).data!!,
                        navigateToDetailMentor = navigateToDetailMentor,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                is UiState.Error -> {
                    LaunchedEffect(snackbarHostState) {
                        (mentorsState as UiState.Error).error?.message?.let { message ->
                            snackbarHostState.showSnackbar(
                                message
                            )
                        }
                    }
                }

                is UiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {}
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun MatchMakingContentPreview() {
    DicodingMentoringTheme {
        MatchMakingContent(
            snackbarHostState = SnackbarHostState(),
            navigateUp = { /*TODO*/ },
            body = { innerPadding ->
                HomeBody(
                    context = LocalContext.current,
                    mentors = listOf(
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                        GetMentor(
                            id = "1",
                            fullName = "Eren Jaeger",
                            photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                            averageRating = 3.0,
                            job = "Front-End Developer"
                        ),
                    ),
                    navigateToDetailMentor = {},
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchMakingContent(
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back to home"
                        )
                    }
                },
                title = {
                    Text(text = "Recommended Mentors")
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
fun MatchMakingBody(
    mentors: List<GetMentor>,
    context: Context,
    navigateToDetailMentor: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (mentors.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_mentors),
                contentDescription = null,
                modifier = Modifier.size(150.dp),
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
            content = {
                items(mentors) { mentor ->
                    MentorCard(
                        fullName = mentor.fullName,
                        photoProfile = mentor.photoProfile,
                        job = mentor.job,
                        averageRating = mentor.averageRating,
                        context = context,
                        onClick = {
                            navigateToDetailMentor(mentor.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        color = Color.Gray.copy(alpha = 0.6f),
                        thickness = 0.5.dp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )
    }
}
