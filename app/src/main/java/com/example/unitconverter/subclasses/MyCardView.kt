package com.example.unitconverter.subclasses

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import com.example.unitconverter.*
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


var card: MyCardView? = null
var cardY: Float = 1f
var longPress: Boolean = false
const val TextMessage = "com.kofi.unitconverter.TextMessage"
const val ViewIdMessage = "com.kofi.unitconverter.ViewIdMessage"

class MyCardView(context: Context, attributeSet: AttributeSet) :
    MaterialCardView(context, attributeSet) {

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
                popupWindow = MyPopupWindow(context, this, R.layout.quick_actions)
                popupWindow.determinePosition()
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

                popupWindow.apply {
                    val textView = this@MyCardView.getChildAt(0) as TextView
                    val drawable = textView.compoundDrawables[1]
                    setDrawable(drawable)
                    show()
                }
            }

            true
        }
        setOnClickListener {
            GlobalScope.launch {
                updateArray()
                startActivity()
            }
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
            putExtra(TextMessage, textViewText)
            putExtra(ViewIdMessage, this@MyCardView.id)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(this)
        }
    }

}
