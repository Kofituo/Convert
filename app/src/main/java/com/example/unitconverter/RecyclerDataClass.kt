package com.example.unitconverter

import android.view.View
import java.io.Serializable

data class RecyclerDataClass(
    val quantity: String,
    var correspondingUnit: CharSequence,
    val id: Int,
    var view: View? = null
) : Serializable