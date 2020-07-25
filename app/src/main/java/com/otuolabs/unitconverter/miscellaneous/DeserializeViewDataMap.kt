package com.otuolabs.unitconverter.miscellaneous

import com.otuolabs.unitconverter.builders.put
import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.withName

object DeserializeViewDataMap : DeserializationStrategy<MutableMap<String, ViewData>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Kofi")

    override fun deserialize(decoder: Decoder): MutableMap<String, ViewData> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonArray
        return LinkedHashMap<String, ViewData>(array.size).apply {
            array.map {
                it as JsonArray
                put {
                    @Suppress("UNCHECKED_CAST")
                    val content = it.content as List<JsonLiteral>
                    key = content[1].content
                    value = ViewData(id = content[0].int, name = key!!, text = content[2].content, metadata = content[3].content)
                }
            }
        }
    }

    override fun patch(decoder: Decoder, old: MutableMap<String, ViewData>): MutableMap<String, ViewData> =
            TODO("I don't know what this does")
}