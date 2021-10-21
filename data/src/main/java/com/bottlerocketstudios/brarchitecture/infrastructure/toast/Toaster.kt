package com.bottlerocketstudios.brarchitecture.infrastructure.toast

import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import com.bottlerocketstudios.brarchitecture.infrastructure.util.toast

/** Abstracts sending toasts to allow usage in classes under test (without bringing in robolectric) */
interface Toaster {
    @MainThread
    /** Displays a "toast" with the given message */
    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT)

    @MainThread
    /** Displays a "toast" with the given message */
    fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
}

class ToasterImpl(app: Context) : Toaster {
    private val app = app.applicationContext

    override fun toast(message: String, duration: Int) {
        app.toast(message, duration)
    }

    override fun toast(@StringRes resId: Int, duration: Int) {
        app.toast(resId, duration)
    }
}
