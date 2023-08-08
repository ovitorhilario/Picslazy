package com.vitorhilarioapps.picslazy.domain.use_case.editphoto

import android.graphics.Bitmap
import com.vitorhilarioapps.picslazy.domain.repository.EditPhotoRepository
import javax.inject.Inject

class GetPreviewBitmapUseCase @Inject constructor(
    private val repository: EditPhotoRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): Bitmap {
        return repository.getPreviewBitmap(bitmap)
    }
}