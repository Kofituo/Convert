package com.example.unitconverter.miscellaneous

import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.withName

class DeserializeStringArray<T : List<String>> : DeserializationStrategy<T> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Kofi")

    override fun deserialize(decoder: Decoder): T {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonArray
        val result = ArrayList<String>(array.size)
        array.forEach {
            it as JsonObject

            result.add(it as String)
        }
        TODO("Not yet implemented")
    }

    override fun patch(decoder: Decoder, old: T): T {
        TODO("Not yet implemented")
    }


}