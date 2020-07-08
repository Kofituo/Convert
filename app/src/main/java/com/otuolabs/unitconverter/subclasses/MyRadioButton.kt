package com.otuolabs.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.radiobutton.MaterialRadioButton

class MyRadioButton(context: Context, attributeSet: AttributeSet?) :
    MaterialRadioButton(context, attributeSet) {
    var myId = -1
}