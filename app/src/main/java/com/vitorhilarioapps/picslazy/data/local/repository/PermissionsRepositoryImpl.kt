package com.vitorhilarioapps.picslazy.data.local.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.vitorhilarioapps.picslazy.domain.repository.PermissionsRepository
import javax.inject.Inject

class PermissionsRepositoryImpl @Inject constructor(
    private val context: Context
): PermissionsRepository {

    override fun hasPermissions(): Boolean {
        val readPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

        val permissions = listOf(readPermission, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
    }
}