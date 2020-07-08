package com.otuolabs.unitconverter.subclasses

import java.io.Serializable


data class FavouritesData(
    var drawableId: Int = -1,
    var topText: CharSequence? = null,
    var metadata: String? = null,
    var cardId: Int = -1,
    var cardName: String? = null
) : Serializable {
    companion object {
        fun favouritesBuilder(action: FavouritesData.() -> Unit) = FavouritesData().apply(action)
    }
}