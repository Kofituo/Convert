package com.example.unitconverter.subclasses

data class FavouritesData(
    val drawable: Int? = null,
    val topText: CharSequence? = null,
    val metadata: String? = null,
    val textMessage: CharSequence? = null
) {
    companion object {
        fun favouritesBuilder(action: FavouritesData.() -> Unit) = FavouritesData().apply(action)
    }
}