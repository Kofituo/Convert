package com.example.unitconverter.miscellaneous

import android.util.Log
import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.withName

object DeserializeIntList : DeserializationStrategy<List<Int>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Kofi")

    override fun deserialize(decoder: Decoder):  List<Int> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonArray
        val list = ArrayList<Int>(array.size)
        array.forEach {
            it as JsonLiteral
            list.add(it.int)
            Log.e("jsonObject","$it")
        }
        return list
    }

    override fun patch(decoder: Decoder, old: List<Int>): List<Int> {
        TODO("I don't know what this does")
    }
}