package online.k0ras1k.pulse.data.models.inout.output

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ProfileRespondModel(
    val login: String,
    val email: String,
    val countryCode: String,
    val isPublic: Boolean,
    val phone: String? = null,
    val image: String? = null
)

@Serializer(forClass = String::class)
object StringAsEmptySerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("WithDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        if (value.isNotEmpty()) {
            encoder.encodeString(value)
        }
    }

    override fun deserialize(decoder: Decoder): String {
        return decoder.decodeString()
    }
}