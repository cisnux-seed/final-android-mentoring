package dev.cisnux.dicodingmentoring.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.cisnux.dicodingmentoring.R
import kotlin.math.ceil
import kotlin.math.floor


@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    starsColor: Color = Color.Yellow,
    stars: Int = 5,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_half_24),
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_border_24),
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

@Preview
@Composable
fun RatingPreview() {
    RatingBar(stars = 1, rating = 0.5)
}

@Preview
@Composable
fun RatingFullPreview() {
    RatingBar(stars = 5, rating = 5.0)
}