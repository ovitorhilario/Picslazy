package com.vitorhilarioapps.picslazy.domain.use_case.editphoto

import android.graphics.Bitmap
import android.net.Uri
import com.vitorhilarioapps.picslazy.domain.repository.EditPhotoRepository
import javax.inject.Inject

class UriToBitmapUseCase @Inject constructor(
    private val repository: EditPhotoRepository
) {
    suspend operator fun invoke(uri: Uri): Bitmap {
        return repository.uriToBitmap(uri)
    }
}