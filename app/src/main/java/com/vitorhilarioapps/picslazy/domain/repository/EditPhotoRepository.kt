package com.vitorhilarioapps.picslazy.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

interface EditPhotoRepository {
    suspend fun uriToBitmap(uri: Uri): Bitmap
    suspend fun getOrientationFromUri(uri: Uri): Int
    suspend fun getPreviewBitmap(bitmap: Bitmap): Bitmap
    suspend fun applyFilter(bitmap: Bitmap, filter: GPUImageFilter): Bitmap
    suspend fun saveImage(bitmap: Bitmap): Boolean
}