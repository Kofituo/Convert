package com.otuolabs.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.textfield.TextInputEditText

class DisableEditText(context: Context, attributeSet: AttributeSet) :
    TextInputEditText(context, attributeSet) {

    private val textColorSecondary: Int
        get() {
            val typeArray =
                context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.textColorSecondary))
            val color = typeArray.getColor(0, 0)
            typeArray.recycle()
            return color
        }
    private val textColorPrimary: Int
        get() {
            val typeArray =
                context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
            val color = typeArray.getColor(0, 0)
            typeArray.recycle()
            return color
        }

    private var shouldDisable = false
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return if (shouldDisable) true else super.dispatchTouchEvent(event)
    }

    fun shouldDisable(shouldDisable: Boolean) {
        if (this.shouldDisable != shouldDisable)
            setTextColor(
                if (shouldDisable) {
                    isFocusable = false
                    textColorSecondary
                } else {
                    isFocusableInTouchMode = true
                    textColorPrimary
                }
            )
        this.shouldDisable = shouldDisable
        //if (shouldDisable)
    }
}