package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import com.example.unitconverter.R
import com.example.unitconverter.dpToInt
import com.example.unitconverter.viewIdMap

var viewArray: ArrayList<View> = arrayListOf()

class GridConstraintLayout(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    data class GuideLines(val left: Int, val right: Int, val top: Int)

    private var guideLine: GuideLines = GuideLines(-1, -1, -1)

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child as View

        guideLine = GuideLines(
            if (child.id == R.id.leftGuide) R.id.leftGuide else guideLine.left,
            if (child.id == R.id.rightGuide) R.id.rightGuide else guideLine.right,
            if (child.id == R.id.topGuide) R.id.topGuide else guideLine.top
        )
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


    fun sort(number: Int, viewIdArray: ArrayList<Int>) {

        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(this@GridConstraintLayout)
            for (i in 0 until viewIdArray.size) {
                val modulo = i % number
                val topViewIndex = i - number
                val topView = if (topViewIndex >= 0) viewIdArray[topViewIndex] else guideLine.top
                val mainView = viewIdArray[i]

                when (modulo) {
                    0 -> {
                        //it means its at the left
                        constraintSetting(
                            this,
                            guideLine.left,
                            mainView,
                            viewIdArray[i + 1],
                            topView
                        )
                        setHorizontalChainStyle(mainView, ConstraintSet.CHAIN_SPREAD_INSIDE)

                    }
                    number - 1 -> {
                        //means its the thr right
                        constraintSetting(
                            this,
                            viewIdArray[i - 1],
                            mainView,
                            guideLine.right,
                            topView
                        )
                    }
                    else -> {
                        //means its in the middle
                        constraintSetting(
                            this,
                            viewIdArray[i - 1],
                            mainView,
                            viewIdArray[i + 1],
                            topView
                        )
                    }
                }
            }
            applyTo(this@GridConstraintLayout)
        }
        for (i in viewIdArray.indices) {
            val view = viewIdMap[viewIdArray[i]]
            Log.e("calling", "$view")
            val params = view?.layoutParams as MarginLayoutParams
            params.topMargin = if (i < number) 0 else 15.dpToInt()
            view.requestLayout()
        }
    }

    private fun ConstraintSet.constrainTopToBottom(firstView: Int, secondView: Int) =
        this.connect(firstView, ConstraintSet.TOP, secondView, ConstraintSet.BOTTOM, 0)

    private fun ConstraintSet.constrainEndToStart(firstView: Int, secondView: Int) =
        this.connect(firstView, ConstraintSet.END, secondView, ConstraintSet.START, 0)

    private fun ConstraintSet.constrainStartToEnd(firstView: Int, secondView: Int) =
        this.connect(firstView, ConstraintSet.START, secondView, ConstraintSet.END, 0)

    private fun Int.dpToInt(): Int = dpToInt(context)
}
