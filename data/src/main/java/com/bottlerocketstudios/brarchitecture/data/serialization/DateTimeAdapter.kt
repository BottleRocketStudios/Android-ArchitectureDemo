package com.bottlerocketstudios.brarchitecture.data.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Clock
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import timber.log.Timber

class DateTimeSerializer(private val clock: Clock) : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return try {
            ZonedDateTime.parse(decoder.decodeString())
        } catch (exception: DateTimeParseException) {
            Timber.e(exception, "Failed to parse zonedDateTime")
            ZonedDateTime.now(clock)
        }
    }
}
