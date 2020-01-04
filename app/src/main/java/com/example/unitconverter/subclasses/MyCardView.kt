package com.example.unitconverter.subclasses

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.PopupWindow
import android.widget.TextView
import com.example.unitconverter.*
import com.google.android.material.card.MaterialCardView


var card : MaterialCardView? = null

var cardY : Float = 1f

var pw =PopupWindow()
var longPress: Boolean = false

const val MESSAGE = "com.kofi.unitconverter.MESSAGE"

class MyCardView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context,attributeSet) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        card = this@MyCardView

        animateFinal = AnimatorInflater.loadAnimator(context, R.animator.animation1_end)
            .apply {
                setTarget(this@MyCardView)
            }

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                // Apply animation
                //Log.e("X","${this.y}")
                AnimatorInflater.loadAnimator(context, R.animator.animation1_start)
                    .apply {
                        setTarget(this@MyCardView)
                        start()
                    }
                pw = MyPopupWindow(context, this, R.layout.quick_actions)
                (pw as MyPopupWindow).determinePosition()
                //Log.e("Y","${this.y} ${this.x}  ${this.top}  ${this.bottom}")
                longPress = false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //Apply animation
                cardY = this@MyCardView.y

                if (longPress) {
                    animateStart =
                        AnimatorInflater.loadAnimator(context, R.animator.animation2).apply {
                            setTarget(this@MyCardView)
                            start()
                            //longPress = false
                        }

                } else {
                    animateFinal?.start()
                }
            }
        }
        return super.onTouchEvent(event)
    }
    init {
        setOnLongClickListener {
            if (orient == Configuration.ORIENTATION_PORTRAIT) {
                longPress = true

                (pw as MyPopupWindow).apply{
                    val textView = this@MyCardView.getChildAt(0) as TextView
                    val drawable = textView.compoundDrawables[1]
                    setDrawable(drawable)
                    show()
                }
            }

            true
        }
        setOnClickListener {
            updateArray()
            startActivity()
        }

    }

    fun updateArray() {
        recentlyUsed.apply {
            remove(this@MyCardView.id)
            add(0, this@MyCardView.id)
        }
    }
    fun startActivity() {
        Intent(context, ConvertActivity::class.java).apply {
            val textViewText = (this@MyCardView.getChildAt(0) as TextView).text
            putExtra(MESSAGE, textViewText)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(this)
        }
    }
}
