package com.vitorhilarioapps.picslazy.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberPicslazyAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember() {
    PicslazyAppState(navController, context)
}

class PicslazyAppState(
    val navController: NavHostController,
    private val context: Context
) {

    val isOnline
        get() = checkIfOnline()

    fun navigateBack() = navController.popBackStack()

    // Resume Top Level Screens

    fun resumeLogin() {
        navController.navigate(LoginLevel.SignIn.route) {
            launchSingleTop = true

            popUpTo(LoginLevel.SignIn.route) {
                inclusive = true
            }
        }
    }

    fun resumeHome() {
        navController.navigate(HomeLevel.AddPhoto.route) {
            launchSingleTop = true

            popUpTo(HomeLevel.AddPhoto.route) {
                inclusive = true
            }
        }
    }

    // Splash Screen

    fun navigateToLoginFromSplash() {
        navController.navigate(TopLevel.Login.route) {
            launchSingleTop = true

            popUpTo(TopLevel.Splash.route) {
                inclusive = true
            }
        }
    }

    fun navigateToHomeFromSplash() {
        navController.navigate(TopLevel.Home.route) {
            launchSingleTop = true

            popUpTo(TopLevel.Splash.route) {
                inclusive = true
            }
        }
    }

    // Login Level

    fun navigateToSignUp() {
        navController.navigate(LoginLevel.SignUp.route) {
            launchSingleTop = true

            popUpTo(LoginLevel.SignIn.route) {
                saveState = true
            }
        }
    }

    fun navigateToForgotPassword() {
        navController.navigate(LoginLevel.ForgotPassword.route) {
            launchSingleTop = true

            popUpTo(LoginLevel.SignIn.route) {
                saveState = true
            }
        }
    }

    fun navigateToHomeFromLogin() {
        navController.navigate(HomeLevel.AddPhoto.route) {
            launchSingleTop = true

            popUpTo(LoginLevel.SignIn.route) {
                inclusive = true
            }
        }
    }

    // Home Level

    // Sing out
    fun navigateToLoginFromHome() {
        navController.navigate(TopLevel.Login.route) {
            launchSingleTop = true

            popUpTo(TopLevel.Home.route) {
                inclusive = true
            }
        }
    }

    fun navigateToProfile() {
        navController.navigate(HomeLevel.Profile.route) {
            launchSingleTop = true

            popUpTo(HomeLevel.AddPhoto.route) {
                saveState = true
            }
        }
    }

    fun navigateToEditPhoto(encodedUri: String?) {
        navController.navigate(HomeLevel.EditPhoto.route + "/${encodedUri ?: "pick"}") {
            launchSingleTop = true

            popUpTo(HomeLevel.AddPhoto.route) {
                saveState = true
            }
        }
    }


    // Network Checker

    private fun checkIfOnline(): Boolean {
        val cm = getSystemService(context, ConnectivityManager::class.java)
        val capabilities = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}