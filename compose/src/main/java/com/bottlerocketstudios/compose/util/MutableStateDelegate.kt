package com.bottlerocketstudios.compose.util

import androidx.compose.runtime.MutableState
import kotlin.reflect.KProperty

class MutableStateDelegate<T>(val flow: MutableState<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = flow.value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        flow.value = value
    }
}
