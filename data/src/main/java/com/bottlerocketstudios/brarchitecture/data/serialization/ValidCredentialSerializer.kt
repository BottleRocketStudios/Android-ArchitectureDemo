package com.bottlerocketstudios.brarchitecture.data.serialization

import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ValidCredentialSerializer : KSerializer<ValidCredentialModel> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ValidCredentialModel") {
        element<String>("id")
        element<String>("password")
    }

    override fun serialize(encoder: Encoder, value: ValidCredentialModel) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.id.value)
            encodeStringElement(descriptor, 1, value.password.value)
        }
    }

    override fun deserialize(decoder: Decoder): ValidCredentialModel {
        return decoder.decodeStructure(descriptor) {
            var id = ""
            var password = ""
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> password = decodeStringElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            ValidCredentialModel(id.toProtectedProperty(), password.toProtectedProperty())
        }
    }
}
