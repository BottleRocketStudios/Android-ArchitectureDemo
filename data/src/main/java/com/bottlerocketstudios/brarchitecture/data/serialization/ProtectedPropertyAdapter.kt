package com.bottlerocketstudios.brarchitecture.data.serialization

import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/** Supports serialization to ProtectedProperty/from json string directly in DTO models that use retrofit+moshi */
class ProtectedPropertyAdapter {
    @ToJson
    fun toJson(protectedProperty: ProtectedProperty<String>): String = protectedProperty.value

    @FromJson
    fun fromJson(jsonValue: String): ProtectedProperty<String> = jsonValue.toProtectedProperty()
}
