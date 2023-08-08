package com.vitorhilarioapps.picslazy.common.model

import android.content.Context
import com.vitorhilarioapps.picslazy.R

class InvalidEntry (val context: Context) {

    val NAME
        get() = context.getString(R.string.invalid_entry_name)

    val EMAIL
        get() = context.getString(R.string.invalid_entry_email)

    val PASSWORD
        get() = context.getString(R.string.invalid_entry_password)

    val MATCH
        get() = context.getString(R.string.invalid_entry_match)
}
