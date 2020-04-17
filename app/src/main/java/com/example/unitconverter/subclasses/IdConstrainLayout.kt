package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class IdConstrainLayout(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    var myId = -1

    var data: FavouritesData? = null
}