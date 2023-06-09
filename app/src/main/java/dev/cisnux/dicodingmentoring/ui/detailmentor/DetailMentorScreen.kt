package dev.cisnux.dicodingmentoring.ui.detailmentor

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.domain.models.Expertise
import dev.cisnux.dicodingmentoring.domain.models.Review
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.components.ProfileBody
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.UiState

@Composable
fun DetailMentorScreen(
    mainViewModel: MainViewModel,
    id: String,
    navigateToCreateMentoring: (id: String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    detailMentorViewModel: DetailMentorViewModel = hiltViewModel()
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(false)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val mentorDetailState by detailMentorViewModel.mentorDetailState

    MentorDetailContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        onFabClick = {
            navigateToCreateMentoring(id)
        }
    ) { innerPadding ->
        when (mentorDetailState) {
            is UiState.Initialize -> detailMentorViewModel.getMentorProfile()

            is UiState.Error -> {
                (mentorDetailState as UiState.Error).error?.let { exception ->
                    LaunchedEffect(snackbarHostState) {
                        exception.message?.let { snackbarHostState.showSnackbar(it) }
                    }
                }
            }

            is UiState.Success -> {
                val context = LocalContext.current
                val mentorProfile = (mentorDetailState as UiState.Success).data
                mentorProfile?.let {
                    MentorDetailBody(
                        fullName = it.fullName,
                        job = it.job,
                        email = it.email,
                        username = it.username,
                        about = it.about,
                        photoProfile = it.photoProfile,
                        isMentor = it.isMentorValid,
                        modifier = Modifier.padding(innerPadding),
                        context = context,
                        reviews = listOf(
                            Review(
                                fullName = "Eren Jaeger",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                            Review(
                                fullName = "Erwin Smith",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                            Review(
                                fullName = "Levi Ackerman",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                            Review(
                                fullName = "Mikassa Ackerman",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                            Review(
                                fullName = "Armin",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                            Review(
                                fullName = "Zeke Jaeger",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                            Review(
                                fullName = "Reiner",
                                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                                rating = 4.5f,
                            ),
                        ),
                        expertises = it.expertises,
                        isEditable = false,
                        navigateUp = navigateUp
                    )
                }
            }

            is UiState.Loading -> {
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
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DetailMentorContentPreview() {
    Surface {
        DicodingMentoringTheme {
            MentorDetailContent(
                snackbarHostState = SnackbarHostState(),
                onFabClick = { }
            ) { innerPadding ->
                MentorDetailBody(
                    fullName = "Exodus Trivellan",
                    job = "Computer Engineering Student at Telkom University",
                    email = "exodusjack@gmail.com",
                    username = "exoduse123",
                    about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus. Donec nec tortor a dolor consectetur tincidunt eu sit amet elit. Nulla convallis ligula et nisl hendrerit, id ultrices elit malesuada. In tincidunt risus in arcu tempor, id malesuada metus congue.",
                    photoProfile = "https://i.pinimg.com/originals/50/d4/29/50d429ea5c9afe0ef9cb3c96f784bea4.jpg",
                    context = LocalContext.current,
                    isMentor = true,
                    reviews = listOf(
                        Review(
                            fullName = "Eren Jaeger",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                        Review(
                            fullName = "Erwin Smith",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                        Review(
                            fullName = "Levi Ackerman",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                        Review(
                            fullName = "Mikassa Ackerman",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                        Review(
                            fullName = "Armin",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                        Review(
                            fullName = "Zeke Jaeger",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                        Review(
                            fullName = "Reiner",
                            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                            rating = 4.5f,
                        ),
                    ),
                    isEditable = false,
                    expertises = listOf(
                        Expertise(
                            learningPath = "Android",
                            experienceLevel = "Beginner",
                            skills = listOf(
                                "Object-Oriented Programming",
                                "Room",
                                "Jetpack Compose"
                            ),
                            certificates = listOf(
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                            )
                        ),
                        Expertise(
                            learningPath = "iOS",
                            experienceLevel = "Expert",
                            skills = listOf(
                                "Object-Oriented Programming",
                                "Room",
                                "Jetpack Compose"
                            ), certificates = listOf(
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                            )
                        ),
                        Expertise(
                            learningPath = "Front-End",
                            experienceLevel = "Intermediate",
                            skills = listOf(
                                "Object-Oriented Programming",
                                "Room",
                                "Jetpack Compose"
                            ), certificates = listOf(
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                            )
                        ),
                        Expertise(
                            learningPath = "Machine Learning",
                            experienceLevel = "Beginner",
                            skills = listOf(
                                "Object-Oriented Programming",
                                "Room",
                                "Jetpack Compose"
                            ),
                            certificates = listOf(
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                                "https://www.google.com",
                            )
                        ),
                    ),
                    modifier = Modifier.padding(innerPadding),
                    navigateUp = {},
                )
            }
        }
    }
}

@Composable
fun MentorDetailContent(
    snackbarHostState: SnackbarHostState,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onFabClick,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_connect),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Connect", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
fun MentorDetailBody(
    fullName: String,
    job: String,
    email: String,
    username: String,
    about: String,
    isMentor: Boolean,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    expertises: List<Expertise>,
    reviews: List<Review>,
    context: Context,
    photoProfile: String? = null,
    navigateUp: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        ProfileBody(
            fullName = fullName,
            job = job,
            email = email,
            username = username,
            about = about,
            photoProfile = photoProfile,
            modifier = modifier,
            context = context,
            isMentor = isMentor,
            reviews = reviews,
            expertises = expertises,
            isEditable = isEditable
        )
        IconButton(
            onClick = navigateUp, modifier = Modifier
                .padding(start = 5.dp, top = 6.dp)
                .align(
                    Alignment.TopStart
                )
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onBackground,
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back to home page",
            )
        }
    }
}
