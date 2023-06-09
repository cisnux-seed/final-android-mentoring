package dev.cisnux.dicodingmentoring.ui.navigation

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val RESET_PASSWORD_ROUTE = "resetpassword"
    const val HOME_ROUTE = "home"
    const val REGISTER_PROFILE_ROUTE = "registerprofile/{id}"
    const val FAVORITE_MENTOR_ROUTE = "favoritementor"
    const val DETAIL_MENTOR_ROUTE = "detailmentor/{id}"
    const val MENTORING_ROUTE = "mentoring"
    const val DETAIL_MENTORING_ROUTE = "mentoring/{mentoringId}"
    const val CREATE_MENTORING_ROUTE = "creatementoring/{id}"
    const val MY_PROFILE_ROUTE = "myprofile"
    const val ADD_MENTOR_ROUTE = "addmentor/{id}"
    const val CHAT_ROOM_ROUTE = "chat?roomChatId={roomChatId}"
    const val MATCH_MAKING_ROUTE = "matchmaking?needs={needs}"
}

class AppNavigationActions(
    navController: NavHostController,
) {
    val navigateToLogin: () -> Unit = {
        navController.popBackStack()
        navController.navigate(AppDestinations.LOGIN_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    val navigateToRegister: () -> Unit = {
        navController.navigate(AppDestinations.REGISTER_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToMentoring: () -> Unit = {
        navController.popBackStack()
        navController.navigate(AppDestinations.MENTORING_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToResetPassword: () -> Unit = {
        navController.navigate(AppDestinations.RESET_PASSWORD_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToAddMentor: (id: String) -> Unit = { id ->
        navController.navigate("addmentor/$id") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToDetailMentor: (id: String) -> Unit = { id ->
        navController.navigate("detailmentor/$id")
    }
    val navigateToDetailMentoring: (mentoringId: String) -> Unit = { mentoringId ->
        navController.navigate("mentoring/$mentoringId")
    }
    val navigateToRoomChat: (roomChatId: String) -> Unit =
        { roomChatId ->
            navController.navigate(
                "chat?roomChatId=$roomChatId"
            )
        }
    val navigateToCreateMentoring: (id: String) -> Unit = { id ->
        navController.navigate("creatementoring/$id")
    }
    val navigateToMyProfile: () -> Unit = {
        navController.navigate(AppDestinations.MY_PROFILE_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToMatchMaking: (needs: String) -> Unit = {needs->
        navController.navigate("matchmaking?needs=$needs")
    }

    val navigateToRegisterProfile: (id: String) -> Unit = { id ->
        navController.popBackStack()
        navController.navigate("registerprofile/$id") {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    val navigateToHome: () -> Unit = {
        navController.popBackStack()
        navController.navigate(AppDestinations.HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
    val navigateToHomeForLogin: () -> Unit = {
        navController.navigate(AppDestinations.HOME_ROUTE) {
            popUpTo(AppDestinations.LOGIN_ROUTE) {
                inclusive = true
            }
        }
    }
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
    val takePictureFromGallery: (launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) -> Unit =
        {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            it.launch(chooser)
        }
}
