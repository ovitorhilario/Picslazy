package com.vitorhilarioapps.picslazy.data.remote.model

import android.net.Uri

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatar: Uri? = null,
    val isEmailVerified: Boolean = false,
    val isAnonymous: Boolean = true
)