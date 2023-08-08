package com.vitorhilarioapps.picslazy.common.helpers

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class FileHelper @Inject constructor(
    private val context: Context
) {

    fun getStorage(): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Saved Images")
    }

    fun getFileName() : String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MM-dd-yyyy-HH:mm", Locale.US)
        return buildString { append("Picslazy-", formatter.format(time)) }
    }
}