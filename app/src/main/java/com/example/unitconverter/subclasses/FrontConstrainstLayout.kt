package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlin.math.round


var viewArray: MutableList<View> = mutableListOf()

class GridConstraintLayout(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    data class GuideLines(val left: Int, val right: Int, val top: Int)

    var guideLine: GuideLines = GuideLines(-1, -1, -1)


    









    private fun ConstraintSet.constrainTopToTop(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.TOP, secondView, ConstraintSet.TOP, 0)
    }

    private fun ConstraintSet.constrainTopToBottom(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.TOP, secondView, ConstraintSet.BOTTOM, 0)

    }

    private fun ConstraintSet.constrainEndToStart(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.END, secondView, ConstraintSet.START, 0)
    }

    private fun ConstraintSet.constrainStartToStart(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.START, secondView, ConstraintSet.START, 0)
    }

    private fun ConstraintSet.constrainStartToEnd(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.START, secondView, ConstraintSet.END, 0)
    }
}