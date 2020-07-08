package com.otuolabs.unitconverter

import android.view.View
import java.io.Serializable

data class RecyclerDataClass(
    val topText: String,
    var bottomText: CharSequence,
    var id: Int = -1,
    var view: View? = null
) : Serializable