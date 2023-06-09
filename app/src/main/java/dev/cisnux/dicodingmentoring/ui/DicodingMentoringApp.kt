package dev.cisnux.dicodingmentoring.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.navigation.AppDestinations
import dev.cisnux.dicodingmentoring.ui.navigation.AppNavGraph
import dev.cisnux.dicodingmentoring.ui.navigation.AppNavigationActions
import dev.cisnux.dicodingmentoring.ui.navigation.DrawerButtonItem
import dev.cisnux.dicodingmentoring.ui.navigation.DrawerNavigationItem
import dev.cisnux.dicodingmentoring.ui.navigation.ImageVectorBottomNavigationItem
import dev.cisnux.dicodingmentoring.ui.navigation.PainterBottomNavigationItem
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DicodingMentoringApp(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldBottomBarOpen by mainViewModel.shouldBottomBarOpen
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navigationActions = AppNavigationActions(navController)

    MainAppContent(
        onLogout = {
            mainViewModel.logout()
            mainViewModel.updateBottomState(false)
            navigationActions.navigateToLogin()
        },
        shouldBottomBarOpen = shouldBottomBarOpen,
        currentRoute = currentRoute,
        navController = navController,
        coroutineScope = coroutineScope,
        drawerState = drawerState,
        body = { innerPadding ->
            AppNavGraph(
                navController = navController,
                navigationActions = navigationActions,
                mainViewModel = mainViewModel,
                drawerState = drawerState,
                modifier = modifier.padding(innerPadding)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainAppContentPreview() {
    val navController: NavHostController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    Surface {
        DicodingMentoringTheme {
            MainAppContent(
                onLogout = {},
                shouldBottomBarOpen = true,
                currentRoute = "home",
                navController = navController,
                coroutineScope = coroutineScope,
                drawerState = drawerState,
                body = {}
            )
        }
    }
}

@Composable
fun MainAppContent(
    onLogout: () -> Unit,
    shouldBottomBarOpen: Boolean,
    currentRoute: String?,
    navController: NavController,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    body: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = MaterialTheme.shapes.extraSmall
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_dicoding
                        ),
                        contentDescription = "Dicoding",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        text = "Dicoding Mentoring",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray.copy(alpha = 0.5f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItems(
                    onSelectedDestination = {},
                    onLogout = onLogout,
                    drawerState = drawerState,
                    coroutineScope = coroutineScope
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                AnimatedVisibility(
                    visible = shouldBottomBarOpen
                ) {
                    BottomBar(currentRoute = currentRoute, onSelected = { destination ->
                        navController.navigate(destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    })
                }
            }
        ) { innerPadding ->
            body(innerPadding)
        }
    }
}

@Preview
@Composable
fun BottomBarPreview(modifier: Modifier = Modifier) {
    DicodingMentoringTheme {
        BottomBar(currentRoute = "", onSelected = {})
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    onSelected: (destination: String) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        val navigationItems = listOf(
            ImageVectorBottomNavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                destination = AppDestinations.HOME_ROUTE,
                contentDescription = stringResource(R.string.home_page)
            ),
            PainterBottomNavigationItem(
                title = stringResource(R.string.menu_mentoring),
                icon = painterResource(id = R.drawable.ic_event_available_24),
                destination = AppDestinations.MENTORING_ROUTE,
                contentDescription = stringResource(R.string.mentoring_page)
            ),
            PainterBottomNavigationItem(
                title = stringResource(R.string.chat_menu),
                icon = painterResource(id = R.drawable.ic_chat_24),
                destination = AppDestinations.HOME_ROUTE,
                contentDescription = stringResource(R.string.chat_page)
            ),
            ImageVectorBottomNavigationItem(
                title = stringResource(R.string.menu_my_profile),
                icon = Icons.Filled.AccountCircle,
                destination = AppDestinations.MY_PROFILE_ROUTE,
                contentDescription = stringResource(R.string.my_profile_page)
            )
        )
        navigationItems.forEach { item ->
            NavigationBarItem(
                label = { Text(item.title, modifier = Modifier.clearAndSetSemantics {}) },
                icon = {
                    if (item is PainterBottomNavigationItem) {
                        Icon(
                            painter = item.icon,
                            contentDescription = null
                        )
                    } else if (item is ImageVectorBottomNavigationItem) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    }
                },
                selected = currentRoute == item.destination,
                onClick = {
                    onSelected(item.destination)
                },
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = item.contentDescription
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationDrawerItemsPreview() {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    Surface {
        DicodingMentoringTheme {
            NavigationDrawerItems(
                onSelectedDestination = {},
                onLogout = {},
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
        }
    }
}

@Composable
fun NavigationDrawerItems(
    onSelectedDestination: (destination: String) -> Unit,
    onLogout: () -> Unit,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
) {
    val drawerItems = listOf(
        DrawerNavigationItem(
            title = "Favorite Mentors",
            contentDescription = "favorite mentors page",
            destination = AppDestinations.FAVORITE_MENTOR_ROUTE,
            icon = Icons.Outlined.FavoriteBorder
        ),
        DrawerButtonItem(
            title = "Logout",
            contentDescription = "logout then navigate to login",
            icon = painterResource(id = R.drawable.ic_logout_24),
            onClick = onLogout
        )
    )
    var selectedDrawerItem by remember {
        mutableStateOf(drawerItems[0])
    }

    drawerItems.forEach { drawerItem ->
        NavigationDrawerItem(
            label = {
                Text(text = drawerItem.title)
            },
            selected = drawerItems == selectedDrawerItem,
            icon = {
                if (drawerItem.icon is ImageVector) {
                    Icon(imageVector = drawerItem.icon as ImageVector, contentDescription = null)
                } else if (drawerItem.icon is Painter) {
                    Icon(painter = drawerItem.icon as Painter, contentDescription = null)
                }
            },
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                }
                when (drawerItem) {
                    is DrawerNavigationItem -> {
                        onSelectedDestination(drawerItem.destination)
                    }

                    is DrawerButtonItem -> {
                        drawerItem.onClick()
                    }
                }
                selectedDrawerItem = drawerItem
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }

    BackHandler(drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }
}