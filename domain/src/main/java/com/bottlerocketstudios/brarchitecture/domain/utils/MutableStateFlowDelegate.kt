package com.bottlerocketstudios.brarchitecture.domain.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty

class MutableStateFlowDelegate<T>(val flow: MutableStateFlow<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = flow.value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        flow.value = value
    }
}
