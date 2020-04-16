package com.example.unitconverter.miscellaneous

import android.util.Log
import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import kotlinx.serialization.withName

object DeserializeStringStringMap : DeserializationStrategy<Map<String, String>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Kofi")

    override fun deserialize(decoder: Decoder): Map<String, String> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonArray
        Log.e("si", "${array.size}")
        val map = LinkedHashMap<String, String>(array.size)
        array.map {
            it as JsonObject
            val first = it.keys.first()
            map.put(first, it[first]!!.content)
        }
        return map
    }

    override fun patch(decoder: Decoder, old: Map<String, String>): Map<String, String> {
        TODO("I don't know what this does")
    }
}