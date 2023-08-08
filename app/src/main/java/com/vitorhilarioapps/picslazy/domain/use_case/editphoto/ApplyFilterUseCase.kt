package com.vitorhilarioapps.picslazy.domain.use_case.editphoto

import android.graphics.Bitmap
import com.vitorhilarioapps.picslazy.domain.repository.EditPhotoRepository
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import javax.inject.Inject

class ApplyFilterUseCase @Inject constructor(
    private val repository: EditPhotoRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, filter: GPUImageFilter): Bitmap {
        return repository.applyFilter(bitmap, filter)
    }
}