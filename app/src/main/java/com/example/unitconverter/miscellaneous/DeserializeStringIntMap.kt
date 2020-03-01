package com.example.unitconverter.miscellaneous

import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import kotlinx.serialization.withName

object DeserializeStringIntMap : DeserializationStrategy<Map<String, Int>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Kofi")

    override fun deserialize(decoder: Decoder): Map<String, Int> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonArray
        return array.map {
            it as JsonObject
            val first = it.keys.first()
            first to it[first]!!.content.toInt()
        }.toMap()
    }

    override fun patch(decoder: Decoder, old: Map<String, Int>): Map<String, Int> =
        TODO("I don't know what this does")
}