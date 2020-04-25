package com.example.unitconverter

import java.io.Serializable

data class RecyclerDataClass(
    val quantity: String,
    val correspondingUnit: CharSequence,
    val id: Int
):Serializable