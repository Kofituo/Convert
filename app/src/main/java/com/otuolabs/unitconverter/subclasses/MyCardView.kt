package com.otuolabs.unitconverter.subclasses

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.otuolabs.unitconverter.AdditionItems.TextMessage
import com.otuolabs.unitconverter.AdditionItems.ViewIdMessage
import com.otuolabs.unitconverter.AdditionItems.animateStart
import com.otuolabs.unitconverter.AdditionItems.animationEnd
import com.otuolabs.unitconverter.AdditionItems.card
import com.otuolabs.unitconverter.AdditionItems.cardY
import com.otuolabs.unitconverter.AdditionItems.longPress
import com.otuolabs.unitconverter.AdditionItems.popupWindow
import com.otuolabs.unitconverter.ConvertActivity
import com.otuolabs.unitconverter.MainActivity
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils.name
import com.otuolabs.unitconverter.builders.addAll
import com.otuolabs.unitconverter.builders.buildIntent
import com.otuolabs.unitconverter.subclasses.MyPopupWindow.Companion.myPopUpWindow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyCardView(context: Context, attributeSet: AttributeSet) :
        MaterialCardView(context, attributeSet), MyPopupWindow.PopupListener {

    private val checkBox: MaterialCheckBox

    init {
        // adding view programmatically so i don't have to get a huge xml file
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
            (parent as GridConstraintLayout).apply {
                if (isChecked) addToMap(this@MyCardView.name)
                else removeFromMap(this@MyCardView.name)
            }
        }
    }

    /**
     * Select items called
     * */
    override fun callback() {
        (parent as GridConstraintLayout).initiateSelectItems()
        checkBox.apply {
            visibility = View.VISIBLE
            isChecked = true
        }
    }

    override fun addOneToFavourites() {
        (parent as GridConstraintLayout).addOneToFavorites(name)
    }

    override fun convertInfo() {
        (parent as GridConstraintLayout).convertInfo(id, name)
    }

    private var checkBoxIsEnabled = false

    fun enableCheckBox() {
        checkBoxIsEnabled = true
    }

    fun disableCheckBox() {
        checkBoxIsEnabled = false
    }

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

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun updateArray() {
        MainActivity.leastRecentlyUsed.apply {
            if (isEmpty()) {
                //means it's the first time
                addAll { MainActivity.viewNameToViewData.keys }
            }
            remove(name)
            add(name)
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