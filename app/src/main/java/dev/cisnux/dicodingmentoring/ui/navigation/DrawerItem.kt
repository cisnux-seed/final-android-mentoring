package dev.cisnux.dicodingmentoring.ui.navigation

import androidx.compose.runtime.Immutable

@Immutable
interface DrawerItem<out T> {
    val title: String
    val contentDescription: String
    val icon: T
}

@Immutable
data class DrawerNavigationItem<out T>(
    override val title: String,
    override val contentDescription: String,
    override val icon: T,
    val destination: String
) : DrawerItem<T>

@Immutable
data class DrawerButtonItem<out T>(
    override val title: String,
    override val contentDescription: String,
    override val icon: T,
    val onClick: () -> Unit,
) : DrawerItem<T>