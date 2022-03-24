package com.bottlerocketstudios.compose.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ByteArray.convertToImageBitmap(): ImageBitmap? {
    return try {
        BitmapFactory.decodeByteArray(this, 0, this.count()).asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
