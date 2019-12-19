package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.constraintlayout.widget.Guideline

class GridConstraintLayout (context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context,attributeSet) {

    var i = 0
    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        Log.e("called fistr","called  $i   ${child is Guideline}")
        i+=1
        super.addView(child, params)
    }

    init {
        
    }

}