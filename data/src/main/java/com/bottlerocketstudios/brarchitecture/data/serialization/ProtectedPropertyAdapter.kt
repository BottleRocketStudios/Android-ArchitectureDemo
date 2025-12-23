package com.bottlerocketstudios.brarchitecture.data.serialization

import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Supports serialization to ProtectedProperty/from json string directly in DTO models */
class ProtectedPropertySerializer : KSerializer<ProtectedProperty<String>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ProtectedProperty", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ProtectedProperty<String>) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ProtectedProperty<String> {
        return decoder.decodeString().toProtectedProperty()
    }
}
