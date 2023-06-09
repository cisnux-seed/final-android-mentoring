package dev.cisnux.dicodingmentoring.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme

@Preview(showBackground = true, device = "id:pixel_6_pro", showSystemUi = true)
@Composable
fun SearchBarButtonPreview() {
    var expanded by remember { mutableStateOf(false) }
    Surface {
        DicodingMentoringTheme {
            SearchBarButton(
                onMenuClick = {},
                query = "",
                onSearch = {},
                onOpenSearchBar = {
                    expanded = true
                },
                expanded = expanded, onCloseSearchBar = {
                    expanded = false
                },
                onQueryChange = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarButton(
    onMenuClick: () -> Unit,
    onOpenSearchBar: () -> Unit,
    query: String,
    onSearch: (query: String) -> Unit,
    expanded: Boolean,
    onCloseSearchBar: () -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = expanded,
        transitionSpec = {
            fadeIn(animationSpec = tween(150, 150)).togetherWith(
                fadeOut(animationSpec = tween(150))
            ).using(SizeTransform { initialSize, targetSize ->
                if (targetState) {
                    keyframes {
                        IntSize(targetSize.width, initialSize.height) at 150
                        durationMillis = 300
                    }
                } else {
                    keyframes {
                        IntSize(initialSize.width, targetSize.height) at 150
                        durationMillis = 300
                    }
                }
            })
        }) { isExpanded ->
        if (isExpanded) {
            SearchBar(query = query,
                leadingIcon = {
                    IconButton(onClick = onCloseSearchBar) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = null
                        )
                    }
                },
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                active = true,
                onActiveChange = {},
                content = {})
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable(onClick = onOpenSearchBar)
                    .padding(start = 8.dp, end = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu, contentDescription = null
                        )
                    }
                    Text(
                        "Search learning paths, skills",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = null,
                )
            }
        }
    }
}

