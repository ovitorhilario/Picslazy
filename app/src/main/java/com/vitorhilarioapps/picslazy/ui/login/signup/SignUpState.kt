package com.vitorhilarioapps.picslazy.ui.login.signup

import android.net.Uri

data class SignUpState (
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val avatar: Uri? = null,
)

data class SignUpError(
    val invalidName: Boolean = false,
    val invalidEmail: Boolean = false,
    val invalidPassword: Boolean = false,
    val invalidConfirmPassword: Boolean = false,
    val invalidMatch: Boolean = false,
)