package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintsChangedListener
import androidx.constraintlayout.widget.Guideline
import kotlin.math.round


var viewArray: MutableList<View> = mutableListOf()

class GridConstraintLayout(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    data class GuideLines(val left: Int, val right: Int, val top: Int)

    var guideLine: GuideLines = GuideLines(-1, -1, -1)

    var i = 0

    init {

        setOnTouchListener { v, event ->
            sort()
            for (i in viewArray) {
                val p = i.layoutParams as LayoutParams
                Log.e(
                    "c",
                    "\n ${i.name}    ${findViewById<View>(p.startToEnd).name}     ${findViewById<View>(p.topToTop).name}     ${findViewById<View>(
                        p.endToStart
                    ).name} ${(p as MarginLayoutParams).topMargin} "
                )
            }
            false
        }
        setOnConstraintsChanged(object : ConstraintsChangedListener() {
            override fun postLayoutChange(stateId: Int, constraintId: Int) {
                Log.e("csasdsdsadsdasfa", "adsa")
                super.postLayoutChange(stateId, constraintId)
            }

            override fun preLayoutChange(stateId: Int, constraintId: Int) {
                Log.e("csasdsdsadsdasfa", "adsa")

                super.preLayoutChange(stateId, constraintId)
            }
        })
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child as View // or child!!

        if (child !is Guideline) {
            viewArray.add(child)
            Log.e("name", child.name + "   $i   ${i % 5}")
            i += 1
        }
        guideLine =
            GuideLines(
                if (child.id == com.example.unitconverter.R.id.leftGuide) com.example.unitconverter.R.id.leftGuide else guideLine.left,
                if (child.id == com.example.unitconverter.R.id.rightGuide) com.example.unitconverter.R.id.leftGuide else guideLine.right,
                if (child.id == com.example.unitconverter.R.id.leftGuide) com.example.unitconverter.R.id.leftGuide else guideLine.top
            )

        super.addView(child, params)
    }

    fun sort() {
        constraintSet.clone(this)
        viewArray.sortBy {
            it.name
        }


        Log.e("0", "$viewArray")
        preSort(3)

        for (i in 0 until viewArray.size) {
            val marginLayoutParams = viewArray[i].layoutParams as MarginLayoutParams
            if (i < 3) {
                marginLayoutParams.topMargin = 0
            } else {
                marginLayoutParams.topMargin = 15.dpToInt(context)
            }
            viewArray[i].requestLayout()
        }



    }

    val constraintSet = ConstraintSet()
    fun preSort(numberOfViews: Int) {

        constraintSet.clone(this)
        for (i in 0 until viewArray.size) {
            // means its at the start
            val modulo = i % numberOfViews
            val topViewIndex = i - numberOfViews

            if (modulo == 0) {
                if (topViewIndex < 0) {
                    Log.e("1", "${viewArray[i].name}    ")
                    constraintSetting(guideLine.left, viewArray[i].id, viewArray[i + 1].id)

                } else {
                    constraintSetting(
                        guideLine.left,
                        viewArray[i].id,
                        viewArray[i + 1].id,
                        viewArray[topViewIndex].id
                    )

                    Log.e(
                        "2",
                        "${viewArray[i].name} top${viewArray[topViewIndex].name}  i-1${viewArray[i - 1].name}    i + 1 ${viewArray[i + 1].name}"
                    )

                }
                constraintSet.setHorizontalChainStyle(viewArray[i].id,ConstraintSet.CHAIN_SPREAD_INSIDE)
            } else if (modulo == numberOfViews - 1) {
                if (topViewIndex < 0) {
                    constraintSetting(viewArray[i - 1].id, viewArray[i].id, guideLine.right)
                    Log.e("3", "${viewArray[i].name} i-1${viewArray[i - 1].name}   ")
                    constraintSet.applyTo(this)

                } else {
                    Log.e(
                        "4",
                        "${viewArray[i].name}  top${viewArray[topViewIndex].name}   i-1${viewArray[i - 1].name}  "
                    )
                    constraintSetting(
                        viewArray[i - 1].id, viewArray[i].id, guideLine.right,
                        viewArray[topViewIndex].id
                    )

                }
            } else {

                if (topViewIndex < 0) {
                    constraintSetting(viewArray[i - 1].id, viewArray[i].id, viewArray[i + 1].id)
                    Log.e(
                        "5",
                        "${viewArray[i].name}  i-1 is left${viewArray[i - 1].name}    i + 1 ${viewArray[i + 1].name}"
                    )

                } else {
                    Log.e(
                        "5",
                        "${viewArray[i].name} ${viewArray[topViewIndex].name}  i-1${viewArray[i - 1].name}    i + 1 ${viewArray[i + 1].name}"
                    )
                    constraintSetting(
                        viewArray[i - 1].id,
                        viewArray[i].id,
                        viewArray[i + 1].id,
                        viewArray[topViewIndex].id
                    )
                }
            }
        }

        constraintSet.applyTo(this)
    }

    private fun constraintSetting(
        LeftView: Int,
        MainView: Int,
        RightView: Int,
        topView: Int = guideLine.top
    ) {
        constraintSet.apply {
            //     clearAllConstraints(MainView)
            //clear(MainView)
            constrainStartToEnd(MainView, LeftView)
            constrainTopToTop(MainView, topView)
            constrainEndToStart(MainView, RightView)
        }
    }


    // functions to make constraining easy

    private fun ConstraintSet.clearAllConstraints(viewId: Int) {

        this.apply {
            clear(viewId, ConstraintSet.TOP)
            clear(viewId, ConstraintSet.BOTTOM)
            clear(viewId, ConstraintSet.START)
            clear(viewId, ConstraintSet.END)
        }
    }

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

