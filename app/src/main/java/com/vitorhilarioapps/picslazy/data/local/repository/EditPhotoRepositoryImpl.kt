package com.vitorhilarioapps.picslazy.data.local.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.vitorhilarioapps.picslazy.domain.repository.EditPhotoRepository
import com.vitorhilarioapps.picslazy.common.ext.rotate
import com.vitorhilarioapps.picslazy.common.helpers.FileHelper
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import java.io.IOException
import javax.inject.Inject

class EditPhotoRepositoryImpl @Inject constructor(
    private val context: Context,
    private val fileHelper: FileHelper
) : EditPhotoRepository {

    override suspend fun uriToBitmap(uri: Uri): Bitmap {
        val orientation = getOrientationFromUri(uri)
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap.rotate(orientation)
    }

     override suspend fun getOrientationFromUri(uri: Uri): Int = runCatching {
        var orientation = 0
        val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)
                if (columnIndex >= 0) {
                    orientation = it.getInt(columnIndex)
                }
            }
        }

        orientation
    }.getOrDefault(0)

    override suspend fun getPreviewBitmap(bitmap: Bitmap): Bitmap = runCatching {
        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
    }.getOrDefault(bitmap)

    override suspend fun applyFilter(bitmap: Bitmap, filter: GPUImageFilter): Bitmap = runCatching {
        val gpuImage = GPUImage(context)
        gpuImage.setFilter(filter)
        gpuImage.getBitmapWithFilterApplied(bitmap)
    }.getOrDefault(bitmap)

    override suspend fun saveImage(bitmap: Bitmap): Boolean {
        val resolver = context.contentResolver
        val fileName = fileHelper.getFileName()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val imageUri = resolver.insert(collection, imageDetails)

        return try {
            val out = imageUri?.let { resolver.openOutputStream(it) }

            out?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                out?.let {
                    imageDetails.clear()
                    imageDetails.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri, imageDetails, null, null)
                }
            }

            out != null
        } catch (e: IOException) {
            imageUri?.let { resolver.delete(it, null, null) }
            false
        }
    }
}