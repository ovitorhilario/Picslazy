package com.vitorhilarioapps.picslazy.ui.login.forgotpassword

data class ForgotPasswordState(
    val email: String = ""
)

data class ForgotPasswordError(
    val invalidEmail: Boolean = false
)