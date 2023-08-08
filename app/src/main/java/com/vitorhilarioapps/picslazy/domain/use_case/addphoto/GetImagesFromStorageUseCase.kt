package com.vitorhilarioapps.picslazy.domain.use_case.addphoto

import com.vitorhilarioapps.picslazy.data.local.model.Image
import com.vitorhilarioapps.picslazy.domain.repository.AddPhotoRepository
import javax.inject.Inject

class GetImagesFromStorageUseCase @Inject constructor(
    private val repository: AddPhotoRepository
) {
    suspend operator fun invoke(): List<Image> {
        return repository.getImagesFromStorage()
    }
}