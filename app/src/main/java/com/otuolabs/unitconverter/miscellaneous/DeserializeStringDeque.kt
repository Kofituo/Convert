package com.otuolabs.unitconverter.miscellaneous

import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.withName
import java.util.*

object DeserializeStringDeque : DeserializationStrategy<ArrayDeque<String>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Kofi")

    override fun deserialize(decoder: Decoder): ArrayDeque<String> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonArray
        val arrayDeque = ArrayDeque<String>(array.size)
        array.forEach {
            it as JsonLiteral
            arrayDeque.add(it.content)
        }
        return arrayDeque
    }

    override fun patch(decoder: Decoder, old: ArrayDeque<String>): ArrayDeque<String> {
        TODO("I don't know what this does")
    }
}