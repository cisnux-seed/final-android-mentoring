package dev.cisnux.dicodingmentoring.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.addmentor.AddMentorScreen
import dev.cisnux.dicodingmentoring.ui.chatroom.RoomChatScreen
import dev.cisnux.dicodingmentoring.ui.creatementoring.CreateMentoringScreen
import dev.cisnux.dicodingmentoring.ui.detailmentor.DetailMentorScreen
import dev.cisnux.dicodingmentoring.ui.detailmentoring.DetailMentoringScreen
import dev.cisnux.dicodingmentoring.ui.home.HomeScreen
import dev.cisnux.dicodingmentoring.ui.login.LoginScreen
import dev.cisnux.dicodingmentoring.ui.matchmaking.MatchMakingScreen
import dev.cisnux.dicodingmentoring.ui.mentoring.MentoringScreen
import dev.cisnux.dicodingmentoring.ui.mentoring.MentoringViewModel
import dev.cisnux.dicodingmentoring.ui.myprofile.MyProfileScreen
import dev.cisnux.dicodingmentoring.ui.register.RegisterScreen
import dev.cisnux.dicodingmentoring.ui.registerprofile.RegisterProfileScreen
import dev.cisnux.dicodingmentoring.ui.resetpassword.ResetPasswordScreen

const val URI = "https://www.mentoring.cisnux.xyz"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationActions: AppNavigationActions,
    mainViewModel: MainViewModel,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
    val authSession by mainViewModel.authSession.collectAsStateWithLifecycle()

    authSession?.let { isSessionExist ->
        val startDestination = if (isSessionExist) AppDestinations.HOME_ROUTE
        else AppDestinations.LOGIN_ROUTE

        NavHost(
            navController = navController, startDestination = startDestination, modifier = modifier
        ) {
            composable(
                route = AppDestinations.LOGIN_ROUTE,
            ) {
                LoginScreen(
                    navigateToHome = navigationActions.navigateToHomeForLogin,
                    navigateToRegister = navigationActions.navigateToRegister,
                    navigateToResetPassword = navigationActions.navigateToResetPassword,
                    navigateToRegisterProfile = navigationActions.navigateToRegisterProfile,
                )
            }
            composable(
                route = AppDestinations.REGISTER_ROUTE,
            ) {
                RegisterScreen(
                    navigateToRegisterProfile = navigationActions.navigateToRegisterProfile,
                    navigateToLogin = navigationActions.navigateUp,
                )
            }
            composable(
                route = AppDestinations.RESET_PASSWORD_ROUTE,
            ) {
                ResetPasswordScreen(
                    navigateToSignIn = navigationActions.navigateUp,
                )
            }
            composable(
                route = AppDestinations.HOME_ROUTE
            ) {
                HomeScreen(
                    mainViewModel = mainViewModel,
                    navigateToDetailMentor = navigationActions.navigateToDetailMentor,
                    drawerState = drawerState,
                    navigateToMatchMaking = navigationActions.navigateToMatchMaking
                )
            }
            composable(
                route = AppDestinations.MENTORING_ROUTE
            ) {
                MentoringScreen(
                    mainViewModel = mainViewModel,
                    navigateToDetailMentoring = navigationActions.navigateToDetailMentoring
                )
            }
            composable(
                route = AppDestinations.MY_PROFILE_ROUTE
            ) {
                MyProfileScreen(
                    navigateToAddMentor = navigationActions.navigateToAddMentor,
                    mainViewModel = mainViewModel,
                )
            }
            composable(
                route = AppDestinations.ADD_MENTOR_ROUTE, arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) {
                AddMentorScreen(
                    navigateToMyProfile = navigationActions.navigateToMyProfile,
                    mainViewModel = mainViewModel,
                )
            }
            composable(
                route = AppDestinations.DETAIL_MENTOR_ROUTE, arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) {
                val id = it.arguments?.getString("id")
                id?.let {
                    DetailMentorScreen(
                        id = id,
                        navigateUp = navigationActions.navigateUp,
                        mainViewModel = mainViewModel,
                        navigateToCreateMentoring = navigationActions.navigateToCreateMentoring
                    )
                }
            }
            composable(
                route = AppDestinations.DETAIL_MENTORING_ROUTE,
                arguments = listOf(navArgument("mentoringId") {
                    type = NavType.StringType
                }),
                deepLinks = listOf(navDeepLink {
                    uriPattern = "$URI/${AppDestinations.DETAIL_MENTORING_ROUTE}"
                })
            ) {
                DetailMentoringScreen(
                    navigateUp = navigationActions.navigateUp,
                    mainViewModel = mainViewModel,
                    navigateToRoomChat = navigationActions.navigateToRoomChat
                )
            }
            composable(
                route = AppDestinations.CHAT_ROOM_ROUTE,
                arguments = listOf(
                    navArgument("roomChatId") {
                        type = NavType.StringType
                    },
                ), deepLinks = listOf(navDeepLink {
                    uriPattern = "$URI/${AppDestinations.CHAT_ROOM_ROUTE}"
                })
            ) {
                RoomChatScreen(
                    mainViewModel = mainViewModel,
                    navigateUp = navigationActions.navigateUp
                )
            }
            composable(
                route = AppDestinations.MATCH_MAKING_ROUTE,
                arguments = listOf(
                    navArgument("needs") {
                        type = NavType.StringType
                    },
                ),
            ) {
                MatchMakingScreen(
                    mainViewModel = mainViewModel,
                    navigateUp = navigationActions.navigateUp,
                    navigateToDetailMentor = navigationActions.navigateToDetailMentor
                )
            }
            composable(
                route = AppDestinations.CREATE_MENTORING_ROUTE,
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) {
                CreateMentoringScreen(
                    navigateUp = navigationActions.navigateUp,
                    navigateToMentoring = navigationActions.navigateToMentoring,
                    mainViewModel = mainViewModel,
                )
            }
            composable(
                route = AppDestinations.REGISTER_PROFILE_ROUTE,
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) {
                val id = it.arguments?.getString("id")
                id?.let { uid ->
                    RegisterProfileScreen(
                        id = uid,
                        onNavigateToHome = navigationActions.navigateToHome,
                        takePictureFromGallery = navigationActions.takePictureFromGallery,
                    )
                }
            }
        }
    }
}