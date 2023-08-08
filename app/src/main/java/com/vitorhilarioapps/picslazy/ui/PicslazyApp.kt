package com.vitorhilarioapps.picslazy.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.vitorhilarioapps.picslazy.ui.home.editphoto.EditPhotoScreen
import com.vitorhilarioapps.picslazy.ui.home.addphoto.AddPhotoScreen
import com.vitorhilarioapps.picslazy.ui.home.profile.ProfileScreen
import com.vitorhilarioapps.picslazy.ui.login.forgotpassword.ForgotPasswordScreen
import com.vitorhilarioapps.picslazy.ui.login.signin.SignInScreen
import com.vitorhilarioapps.picslazy.ui.login.signup.SignUpScreen
import com.vitorhilarioapps.picslazy.ui.splash.SplashScreen

sealed class TopLevel(val route: String) {
    object Login : TopLevel("login")
    object Home : TopLevel("home")
    object Splash : TopLevel("splash")
}

sealed class LoginLevel(val route: String) {
    object SignIn : LoginLevel("sign-in")
    object SignUp : LoginLevel("sign-up")
    object ForgotPassword : LoginLevel("forgot-password")
}

sealed class HomeLevel(val route: String) {
    object AddPhoto : HomeLevel("add-photo") // Initial Back Stack
    object EditPhoto : HomeLevel("edit-photo")
    object Profile : HomeLevel("profile")
}

@Composable
fun PicslazyApp(
    appState: PicslazyAppState = rememberPicslazyAppState(),
) {
    NavHost(
        navController = appState.navController,
        startDestination = TopLevel.Splash.route
    ) {

        composable(TopLevel.Splash.route) {
            SplashScreen(
                onFinishSplash = { isAuthenticated ->
                    if (isAuthenticated) {
                        appState.navigateToHomeFromSplash()
                    } else {
                        appState.navigateToLoginFromSplash()
                    }
                }
            )
        }

        navigation(LoginLevel.SignIn.route, TopLevel.Login.route) {

            composable(LoginLevel.SignIn.route) {
                SignInScreen(
                    navigateToSignUp = appState::navigateToSignUp,
                    navigateToForgotPassword = appState::navigateToForgotPassword,
                    navigateToHome = appState::navigateToHomeFromLogin
                )
            }

            composable(LoginLevel.SignUp.route) {
                SignUpScreen(onBackNavigate = appState::resumeLogin)
            }

            composable(LoginLevel.ForgotPassword.route) {
                ForgotPasswordScreen(onBackNavigate = appState::resumeLogin)
            }
        }

        navigation(HomeLevel.AddPhoto.route, TopLevel.Home.route) {

            composable(HomeLevel.AddPhoto.route) {
                AddPhotoScreen(
                    onOpenProfile = appState::navigateToProfile,
                    onEditPhoto = appState::navigateToEditPhoto
                )
            }

            composable(
                route = HomeLevel.EditPhoto.route + "/{action-type}",
                arguments = listOf(navArgument("action-type") { type = NavType.StringType })
            ) { entry ->
                val action = entry.arguments?.getString("action-type")

                EditPhotoScreen(
                    args = action,
                    onBackNavigate = appState::resumeHome
                )
            }

            composable(HomeLevel.Profile.route) {
                ProfileScreen(
                    onSingOut = appState::navigateToLoginFromHome,
                    onBackNavigate = appState::resumeHome,
                )
            }
        }
    }
}