package com.otuolabs.unitconverter

import com.otuolabs.unitconverter.miscellaneous.ViewData
import java.io.Serializable

data class RecyclerDataClass(
        val topText: String,
        var bottomText: CharSequence,
        var id: Int = -1,
        var view: ViewData? = null
) : Serializable