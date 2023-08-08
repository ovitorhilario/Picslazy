package com.vitorhilarioapps.picslazy.ui.login.signin

data class SignInState(
    var email: String = "",
    var password: String = "",
)

data class SignInError(
    val invalidEmail: Boolean = false,
    val invalidPassword: Boolean = false,
)