package com.bottlerocketstudios.compose.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import timber.log.Timber

@Suppress("TooGenericExceptionCaught") // since decodeByteArray can return a few subtypes of RuntimeException (ArrayIndexOutOfBoundsException and IllegalArgumentException), just catch RuntimeException
fun ByteArray.convertToImageBitmap(): ImageBitmap? {
    return try {
        BitmapFactory.decodeByteArray(this, 0, this.count()).asImageBitmap()
    } catch (e: RuntimeException) {
        Timber.w(e, "[convertToImageBitmap] error converting ByteArray to ImageBitmap")
        null
    }
}
