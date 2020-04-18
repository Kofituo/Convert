package com.example.unitconverter.miscellaneous

import kotlinx.serialization.Encoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationStrategy
import java.util.*

object SerializeStringDeque : SerializationStrategy<ArrayDeque<String>> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun serialize(encoder: Encoder, obj: ArrayDeque<String>) {
        //    encoder.beginStructure(ArrayListSerializer)
        TODO("Not yet implemented")
    }
}