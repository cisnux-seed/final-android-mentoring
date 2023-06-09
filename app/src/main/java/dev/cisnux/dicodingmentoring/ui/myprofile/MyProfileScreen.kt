package dev.cisnux.dicodingmentoring.ui.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.Review
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.components.ProfileBody
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.UiState

@Composable
fun MyProfileScreen(
    mainViewModel: MainViewModel,
    navigateToAddMentor: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    myProfileViewModel: MyProfileViewModel = hiltViewModel(),
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(true)
    }
    val isRefreshContent by mainViewModel.isRefreshMyProfileContent
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val myProfileState by myProfileViewModel.myProfileState
    val isMentorValid by myProfileViewModel.isMentorValid
    val currentUserId by myProfileViewModel.currentUserId

    if(isRefreshContent){
        myProfileViewModel.getMenteeProfile(currentUserId)
        // reset
        mainViewModel.refreshMyProfileContent(false)
    }

    MyProfileContent(
        snackbarHostState = snackbarHostState,
        isMentorValid = isMentorValid,
        onFabClick = {
            currentUserId.let(navigateToAddMentor)
        },
        modifier = modifier,
    ) { innerPadding ->
        when (myProfileState) {
            is UiState.Error -> {
                (myProfileState as UiState.Error).error?.let { exception ->
                    LaunchedEffect(snackbarHostState) {
                        exception.message?.let { snackbarHostState.showSnackbar(it) }
                    }
                }
            }

            is UiState.Success -> {
                val context = LocalContext.current
                val userProfile = (myProfileState as UiState.Success).data
                userProfile?.let {
                    ProfileBody(
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
                        isEditable = true,
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
            else ->{}
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = true,
    device = "spec:parent=pixel_6_pro,orientation=landscape"
)
@Composable
fun MyProfileContentLandscapePreviewWhenSuccess() {
    DicodingMentoringTheme {
        Surface {
            MyProfileContent(
                snackbarHostState = SnackbarHostState(),
                isMentorValid = false,
                onFabClick = {}
            ) {
                ProfileBody(
                    fullName = "Exodus Trivellan",
                    job = "Software Engineer at Apple",
                    email = "exodusjack@gmail.com",
                    username = "exoduse123",
                    about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus. Donec nec tortor a dolor consectetur tincidunt eu sit amet elit. Nulla convallis ligula et nisl hendrerit, id ultrices elit malesuada. In tincidunt risus in arcu tempor, id malesuada metus congue.",
                    photoProfile = null,
                    modifier = Modifier.padding(it),
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
                    isEditable = true,
                    expertises = emptyList()
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = true,
    device = "spec:parent=pixel_6_pro,orientation=portrait"
)
@Composable
fun MyProfileContentPortraitPreviewWhenSuccess() {
    DicodingMentoringTheme {
        Surface {
            MyProfileContent(
                snackbarHostState = SnackbarHostState(),
                isMentorValid = false,
                onFabClick = {}
            ) {
                ProfileBody(
                    fullName = "Exodus Trivellan",
                    job = "Software Engineer at Apple",
                    email = "exodusjack@gmail.com",
                    username = "exoduse123",
                    about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus. Donec nec tortor a dolor consectetur tincidunt eu sit amet elit. Nulla convallis ligula et nisl hendrerit, id ultrices elit malesuada. In tincidunt risus in arcu tempor, id malesuada metus congue.",
                    photoProfile = "https://i.pinimg.com/originals/50/d4/29/50d429ea5c9afe0ef9cb3c96f784bea4.jpg",
                    modifier = Modifier.padding(it),
                    context = LocalContext.current,
                    isMentor = false,
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
                    isEditable = true,
                    expertises = emptyList()
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MyProfileContentPreviewWhenLoading() {
    DicodingMentoringTheme {
        Surface {
            MyProfileContent(
                snackbarHostState = SnackbarHostState(),
                onFabClick = {},
                isMentorValid = false
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}

@Composable
fun MyProfileContent(
    snackbarHostState: SnackbarHostState,
    onFabClick: () -> Unit,
    isMentorValid: Boolean,
    modifier: Modifier = Modifier,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (!isMentorValid) {
                ExtendedFloatingActionButton(
                    onClick = onFabClick,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(text = "JOIN US", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
fun GridChipInterest(
    count: Int,
    interests: List<String>,
    modifier: Modifier = Modifier
) {
    val gridItems = interests.chunked(count)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
    ) {
        gridItems.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                it.forEach { interest ->
                    Text(
                        text = interest,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .clip(CircleShape)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}
