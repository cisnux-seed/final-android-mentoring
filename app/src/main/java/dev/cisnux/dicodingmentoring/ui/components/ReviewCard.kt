package dev.cisnux.dicodingmentoring.ui.components

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme

@Preview(showBackground = true)
@Composable
fun ReviewCardPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        DicodingMentoringTheme {
            ReviewCard(
                fullName = "Eren Jaeger",
                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce finibus leo id enim feugiat, sed tempor erat lacinia. Vestibulum luctus, nisl non sagittis interdum, quam velit condimentum purus, ac pulvinar orci quam id metus.",
                context = LocalContext.current,
                rating = 4.7f,
            )
        }
    }
}

@Composable
fun ReviewCard(
    fullName: String,
    comment: String,
    context: Context,
    rating: Float,
    modifier: Modifier = Modifier,
    photoProfile: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
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
            Text(text = fullName, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = comment,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                tint = colorResource(id = R.color.dark_yellow),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = rating.toString(),
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Divider(color = Color.Gray.copy(alpha = 0.4f), thickness = 1.dp)
    }
}