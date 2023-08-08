package com.vitorhilarioapps.picslazy.domain.repository

import com.vitorhilarioapps.picslazy.data.local.model.Image

interface AddPhotoRepository {
    suspend fun getImagesFromStorage(): List<Image>
}