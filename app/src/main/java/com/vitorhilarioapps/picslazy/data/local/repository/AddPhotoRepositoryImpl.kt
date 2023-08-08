package com.vitorhilarioapps.picslazy.data.local.repository

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.vitorhilarioapps.picslazy.data.local.model.Image
import com.vitorhilarioapps.picslazy.domain.repository.AddPhotoRepository
import javax.inject.Inject

class AddPhotoRepositoryImpl @Inject constructor(
    private val context: Context
) : AddPhotoRepository {

    override suspend fun getImagesFromStorage(): List<Image> {
        val imageList = mutableListOf<Image>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        with(context) {
            val query = contentResolver.query(collection, projection, null, null, null)

            query?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val date = cursor.getLong(dateAddedColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageList.add(Image(contentUri, id, name, date))
                }
            }
        }

        return imageList.sortedByDescending { it.date }
    }

}