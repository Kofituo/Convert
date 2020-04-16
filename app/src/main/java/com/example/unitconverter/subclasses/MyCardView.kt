package com.example.unitconverter.subclasses

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.animateStart
import com.example.unitconverter.AdditionItems.animationEnd
import com.example.unitconverter.AdditionItems.card
import com.example.unitconverter.AdditionItems.cardY
import com.example.unitconverter.AdditionItems.longPress
import com.example.unitconverter.AdditionItems.mRecentlyUsed
import com.example.unitconverter.AdditionItems.popupWindow
import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.R
import com.example.unitconverter.Utils.name
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.subclasses.MyPopupWindow.Companion.myPopUpWindow
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyCardView(context: Context, attributeSet: AttributeSet) :
    MaterialCardView(context, attributeSet), MyPopupWindow.PopupListener {

    private val checkBox: MaterialCheckBox

    private lateinit var mParent: GridConstraintLayout

    init {
        // adding view programmatically so i don't have to get a huge xml file
        post {
            mParent = this.parent as GridConstraintLayout
        }
        checkBox = MaterialCheckBox(context, attributeSet).apply {
            //default layout param are wrap content
            //so no need to reset them
            gravity = Gravity.TOP and Gravity.START
            scaleX = 0.95f
            scaleY = 0.95f
            visibility = View.INVISIBLE
            this@MyCardView.addView(this)
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mParent.apply {
                if (isChecked) addToMap(this@MyCardView.name)
                else removeFromMap(this@MyCardView.name)
            }
        }
    }

    /**
     * Select items called
     * */
    override fun callback() {
        Log.e("called", "called select  $this  \n${this.parent}")
        mParent.initiateSelectItems()
        checkBox.apply {
            visibility = View.VISIBLE
            isChecked = true
        }
    }

    override fun addOneToFavourites() {
        mParent.addOneToFavorites(name)
    }

    var checkBoxIsEnabled = false

    override fun dispatchTouchEvent(ev: MotionEvent?) =
        if (checkBoxIsEnabled) {
            getChildAt(0).dispatchTouchEvent(ev)
            true
        } else
            super.dispatchTouchEvent(ev)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        card = this@MyCardView
        animationEnd =
            AnimatorInflater
                .loadAnimator(context, R.animator.animation1_end)
                .apply { setTarget(this@MyCardView) }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Apply animation
                cardY = this.y
                Log.e("DOWN", "${this.y}")
                AnimatorInflater
                    .loadAnimator(context, R.animator.animation1_start)
                    .apply {
                        setTarget(this@MyCardView)
                        start()
                    }
                popupWindow = myPopUpWindow {
                    mContext = context
                    anchor = this@MyCardView
                    resInt = R.layout.quick_actions
                }.apply {
                    determinePosition()
                    setListener(this@MyCardView)
                }
                //Log.e("Y","${this.y} ${this.x}  ${this.top}  ${this.bottom}")
                longPress = false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (longPress)
                // bouncing animation
                    animateStart =
                        AnimatorInflater
                            .loadAnimator(context, R.animator.animation2)
                            .apply {
                                setTarget(this@MyCardView)
                                start()
                            }
                else animationEnd?.start()
            }
        }
        return super.onTouchEvent(event)
    }

    init {
        setOnLongClickListener {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                longPress = true
                popupWindow.apply {
                    val textView = this@MyCardView.getChildAt(1) as TextView
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
        mRecentlyUsed = buildMutableMap {
            put(this@MyCardView.name, id)
            putAll(mRecentlyUsed)
        }
    }

    fun shrinkSize() {
        AnimatorInflater
            .loadAnimator(context, R.animator.selection_animaton)
            .apply {
                setTarget(this@MyCardView)
                start()
            }
    }

    fun restoreSize() {
        AnimatorInflater
            .loadAnimator(context, R.animator.animation1_end)
            .apply {
                setTarget(this@MyCardView)
                start()
            }
        checkBox.apply {
            visibility = View.INVISIBLE
            isChecked = false
        }
    }

    fun showCheckBox() {
        checkBox.visibility = View.VISIBLE
    }

    fun startActivity() {
        buildIntent<ConvertActivity>(context) {
            val textViewText = (this@MyCardView.getChildAt(1) as TextView).text
            putExtra(TextMessage, textViewText)
            putExtra(ViewIdMessage, this@MyCardView.id)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(this)
        }
    }
}