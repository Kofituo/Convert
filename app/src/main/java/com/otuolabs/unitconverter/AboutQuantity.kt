package com.otuolabs.unitconverter

data class AboutQuantity(
        val quantity: CharSequence,
        val definition: CharSequence,
        val instruments: CharSequence?,
        val units: CharSequence?,
        val didYouKnow: CharSequence?
)