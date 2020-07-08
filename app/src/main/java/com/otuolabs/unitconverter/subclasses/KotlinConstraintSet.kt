@file:Suppress("NOTHING_TO_INLINE")

package com.otuolabs.unitconverter.subclasses

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class KotlinConstraintSet : ConstraintSet() {

    var isConstant = false
    var margin: Int? = null
        get() {
            val result = field
            if (!isConstant)
                margin = null //reset it to work with other constraints
            return result
        }

    inline infix fun Unit.and(other: Int) = other // just to join two functions

    inline infix fun Int.topToBottomOf(bottom: Int) =
        margin?.let {
            connect(this, TOP, bottom, BOTTOM, it)
        } ?: connect(this, TOP, bottom, BOTTOM)

    inline infix fun margin(margin: Int) {
        this.margin = margin
    }

    inline fun margin(margin: Int, isConstant: Boolean) {
        this.isConstant = isConstant
        this.margin = margin
    }

    inline infix fun Int.bottomToBottomOf(bottom: Int) =
        margin?.let {
            connect(this, BOTTOM, bottom, BOTTOM, it)
        } ?: connect(this, BOTTOM, bottom, BOTTOM)

    inline infix fun Int.bottomToTopOf(top: Int) =
        margin?.let {
            connect(this, BOTTOM, top, TOP, it)
        } ?: connect(this, BOTTOM, top, TOP)

    inline infix fun Int.topToTopOf(top: Int) =
        margin?.let {
            connect(this, TOP, top, TOP, it)
        } ?: connect(this, TOP, top, TOP)

    infix fun Int.clear(constraint: Constraints) =
        when (constraint) {
            Constraints.TOP -> clear(this, TOP)
            Constraints.BOTTOM -> clear(this, BOTTOM)
            Constraints.END -> clear(this, END)
            Constraints.START -> clear(this, START)
        }

    inline infix fun Int.startToEndOf(end: Int) =
        margin?.let {
            connect(this, START, end, END, it)
        } ?: connect(this, START, end, END)

    inline infix fun Int.endToStartOf(start: Int) =
        margin?.let {
            connect(this, END, start, START, it)
        } ?: connect(this, END, start, START)

    //inline infix fun clearTopCon
    inline infix fun appliesTo(constraintLayout: ConstraintLayout) =
        applyTo(constraintLayout)

    inline infix fun clones(constraintLayout: ConstraintLayout) =
        clone(constraintLayout)

    inline fun constraint(view: Int, block: (Int) -> Unit) =
        view.also(block)
}