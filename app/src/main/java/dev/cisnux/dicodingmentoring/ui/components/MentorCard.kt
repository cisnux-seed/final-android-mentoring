package dev.cisnux.dicodingmentoring.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MentorCardPreview() {
    Surface {
        DicodingMentoringTheme {
            MentorCard(
                fullName = "Eren Jaeger",
                job = "Software Engineer",
                averageRating = 4.5,
                photoProfile = "https://upload.wikimedia.org/wikipedia/it/a/a7/Eren_jaeger.png",
                context = LocalContext.current,
                onClick = {}
            )
        }
    }
}

@Composable
fun MentorCard(
    fullName: String,
    job: String,
    averageRating: Double,
    context: Context,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    photoProfile: String? = null,
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        photoProfile?.let {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photoProfile)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .border(2.dp, Color.White, MaterialTheme.shapes.small)
                    .clip(MaterialTheme.shapes.small)
                    .size(75.dp)
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    Surface(
                        color = Color.DarkGray,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .size(75.dp)
                    ) {}
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
        } ?: Box(
            modifier = Modifier
                .border(2.dp, Color.White, MaterialTheme.shapes.small)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                )
                .size(70.dp)
        ) {
            Text(
                text = fullName[0].toString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            Text(
                text = fullName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = job,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            RatingBar(
                rating = averageRating,
                starsColor = colorResource(id = R.color.dark_yellow),
            )
        }
    }
}
