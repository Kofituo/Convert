package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import com.example.unitconverter.R
import kotlin.math.round


class GridConstraintLayout(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    data class GuideLines(val left: Int, val right: Int, val top: Int)

    private var guideLine: GuideLines = GuideLines(-1, -1, -1)

    var viewArray: MutableList<View> = mutableListOf()

    init {
        setOnTouchListener { _, _ ->
            sort()
            false
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child as View

        guideLine = GuideLines(
            if (child.id == R.id.leftGuide) R.id.leftGuide else guideLine.left,
            if (child.id == R.id.rightGuide) R.id.rightGuide else guideLine.right,
            if (child.id == R.id.topGuide) R.id.topGuide else guideLine.top
        )
        Log.e("view", child.name)
        if (child !is Guideline) viewArray.add(child)
        super.addView(child, params)
    }

    private fun constraintSetting(
        constraintSet: ConstraintSet,
        LeftView: Int,
        MainView: Int,
        RightView: Int,
        topView: Int = guideLine.top
    ) {
        constraintSet.apply {
            constrainTopToBottom(MainView, topView)
            constrainStartToEnd(MainView, LeftView)
            constrainEndToStart(MainView, RightView)
        }
    }

    fun sort() {
        viewArray.shuffle()

        aSort(5)
    }

    fun aSort(number: Int) {

        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(this@GridConstraintLayout)
            for (i in 0 until viewArray.size) {
                val modulo = i % number
                val topViewIndex = i - number
                val topView = if (topViewIndex >= 0) viewArray[topViewIndex].id else guideLine.top
                val mainView = viewArray[i]
                when (modulo) {
                    0 -> {
                        //it means its at the left
                        constraintSetting(
                            this,
                            guideLine.left,
                            mainView.id,
                            viewArray[i + 1].id,
                            topView
                        )
                        setHorizontalChainStyle(mainView.id, ConstraintSet.CHAIN_SPREAD_INSIDE)

                    }
                    number - 1 -> {
                        //means its the thr right
                        constraintSetting(
                            this,
                            viewArray[i - 1].id,
                            mainView.id,
                            guideLine.right,
                            topView
                        )
                    }
                    else -> {
                        //means its in the middle
                        constraintSetting(
                            this,
                            viewArray[i - 1].id,
                            mainView.id,
                            viewArray[i + 1].id,
                            topView
                        )
                    }
                }
            }
            applyTo(this@GridConstraintLayout)
        }

        for (i in 0 until viewArray.size) {

            val params = viewArray[i].layoutParams as MarginLayoutParams
            params.topMargin = if (i < number) 0 else 15.dpToInt(context)
            viewArray[i].requestLayout()

        }
    }

    private fun ConstraintSet.constrainTopToBottom(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.TOP, secondView, ConstraintSet.BOTTOM, 0)

    }

    private fun ConstraintSet.constrainEndToStart(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.END, secondView, ConstraintSet.START, 0)
    }

    private fun ConstraintSet.constrainStartToEnd(firstView: Int, secondView: Int) {
        this.connect(firstView, ConstraintSet.START, secondView, ConstraintSet.END, 0)
    }
}


internal fun Int.dpToInt(context: Context): Int = round(
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
).toInt()

// used to get name from id
val View.name: String
    get() =
        if (this.id == -0x1) "no id"
        else resources.getResourceEntryName(this.id)

