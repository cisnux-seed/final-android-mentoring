package dev.cisnux.dicodingmentoring.ui.components

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.domain.models.Expertise
import dev.cisnux.dicodingmentoring.domain.models.Review
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
@Composable
fun MyProfileBodyPreview() {
    DicodingMentoringTheme {
        Surface {
            ProfileBody(
                fullName = "Exodus Trivellan",
                job = "Computer Engineering Student at Telkom University",
                email = "exodusjack@gmail.com",
                username = "exoduse123",
                about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus. Donec nec tortor a dolor consectetur tincidunt eu sit amet elit. Nulla convallis ligula et nisl hendrerit, id ultrices elit malesuada. In tincidunt risus in arcu tempor, id malesuada metus congue.",
                photoProfile = "https://i.pinimg.com/originals/50/d4/29/50d429ea5c9afe0ef9cb3c96f784bea4.jpg",
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
                )
            )
        }
    }
}

@Composable
fun ProfileBody(
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
) {
    val tabTitles = listOf("Overview", "Reviews")
    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                        .border(2.dp, Color.White, CircleShape)
                        .shadow(2.dp, CircleShape, true)
                        .clip(CircleShape)
                        .size(110.dp)
                )
            } ?: Box(
                modifier = Modifier
                    .border(2.dp, Color.White, CircleShape)
                    .shadow(2.dp, CircleShape, true)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .size(110.dp)
            ) {
                Text(
                    text = fullName[0].toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = fullName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = job,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$email | @$username",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TabRow(selectedTabIndex = selectedTab) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        }) {
                        Text(text = title, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            when (selectedTab) {
                0 -> Overview(
                    about = about,
                    isMentor = isMentor,
                    expertises = expertises,
                    isEditable = isEditable
                )

                1 -> ReviewWithList(reviews = reviews, context = context)
            }
        }
        if (isEditable) {
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
            }
        }
    }

}

@Composable
private fun Overview(
    about: String,
    isMentor: Boolean,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    expertises: List<Expertise>
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "About Me",
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = about,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Status",
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isMentor) "Mentor" else "Mentee",
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (expertises.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Expertises",
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                if (isEditable) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            expertises.forEach { expertise ->
                ExpertiseCard(
                    learningPath = expertise.learningPath,
                    skills = expertise.skills,
                    experienceLevel = expertise.experienceLevel
                )
            }
        }
    }
}

@Composable
private fun ReviewWithList(reviews: List<Review>, modifier: Modifier = Modifier, context: Context) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(reviews) { review ->
                ReviewCard(
                    fullName = review.fullName,
                    comment = review.comment,
                    context = context,
                    rating = review.rating
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}