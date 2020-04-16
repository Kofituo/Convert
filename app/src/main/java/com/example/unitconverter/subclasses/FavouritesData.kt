package com.example.unitconverter.subclasses

import java.io.Serializable


data class FavouritesData(
    var drawableId: Int? = null,
    var topText: CharSequence? = null,
    var metadata: String? = null,
    var cardId: Int? = null
) : Serializable {
    companion object {
        fun favouritesBuilder(action: FavouritesData.() -> Unit) = FavouritesData().apply(action)
    }
}