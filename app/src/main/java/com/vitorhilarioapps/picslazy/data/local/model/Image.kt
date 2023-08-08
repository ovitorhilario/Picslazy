package com.vitorhilarioapps.picslazy.data.local.model

import android.net.Uri

data class Image(
        val uri: Uri?,
        val id: Long,
        val name: String,
        val date: Long
)