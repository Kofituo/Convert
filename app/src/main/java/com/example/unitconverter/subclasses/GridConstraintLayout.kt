package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.unitconverter.AdditionItems.mRecentlyUsed
import com.example.unitconverter.AdditionItems.originalMap
import com.example.unitconverter.AdditionItems.viewsMap
import com.example.unitconverter.R
import com.example.unitconverter.Utils.dpToInt
import com.example.unitconverter.Utils.name
import com.example.unitconverter.miscellaneous.isNotNull

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
        if (child !is Guideline) {
            originalMap[child.name] = child.id
            viewsMap.append(child.id, child)
        }
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


    fun sort(number: Int, map: Map<String, Int>) {
        val o = System.currentTimeMillis()
        val array = arrayListOf<Int>()

        for ((viewName, viewId) in map) {
            /**
             * eg original  = {"temp:123,"time",789}
             * eg recently used = {"temp":123,"time",456}
             * when 456 is wrong....
             * should update without destroying order
             * assuming the number of views are the same
             * @id is the correct one
             * */
            val id = originalMap[viewName]
            if (viewId != id) {
                Log.e("well", "${mRecentlyUsed[viewName] != id}")
                if (mRecentlyUsed[viewName] != id && id.isNotNull()) mRecentlyUsed[viewName] = id

                if (id.isNotNull()) array.add(id)//adds the correct id
                Log.e("called", "$viewId  $id")
                continue
            }
            array.add(viewId)
        }

        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(this@GridConstraintLayout)
            for (i in 0 until array.size) {
                val modulo = i % number
                val topViewIndex = i - number
                val topView = if (topViewIndex >= 0) array[topViewIndex] else guideLine.top
                val mainView = array[i]
                when (modulo) {
                    0 -> {
                        //it means its at the left
                        constraintSetting(
                            this,
                            guideLine.left,
                            mainView,
                            array[i + 1],
                            topView
                        )
                        setHorizontalChainStyle(mainView, ConstraintSet.CHAIN_SPREAD_INSIDE)

                    }
                    number - 1 -> {
                        //means its the thr right
                        constraintSetting(
                            this,
                            array[i - 1],
                            mainView,
                            guideLine.right,
                            topView
                        )
                    }
                    else -> {
                        //means its in the middle
                        constraintSetting(
                            this,
                            array[i - 1],
                            mainView,
                            array[i + 1],
                            topView
                        )
                    }
                }
            }
            /* Looks cool but no
            val transitionArray = arrayListOf<Interpolator>(AccelerateInterpolator(0.05f),AccelerateDecelerateInterpolator())
            transitionArray.shuffle()*/

            TransitionManager
                .beginDelayedTransition(this@GridConstraintLayout, ChangeBounds()
                    .apply {
                        interpolator = AccelerateInterpolator(0.057f)
                    })
            applyTo(this@GridConstraintLayout)
        }
        for (i in array.indices) {
            //viewSparseArray int -> View
            //view can never be null
            val view = viewsMap[array[i]] as View //to throws an exception means problem
            view.apply {
                val params = layoutParams as MarginLayoutParams
                params.topMargin = if (i < number) 0 else 15.dpToInt()
                requestLayout()
            }
        }
        Log.e("fin", "${System.currentTimeMillis() - o}")
    }

    /*fun sort(number: Int, viewIdArray: ArrayList<Int>) {
        val o = System.currentTimeMillis()
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
            *//* Looks cool but no
            val transitionArray = arrayListOf<Interpolator>(AccelerateInterpolator(0.05f),AccelerateDecelerateInterpolator())
            transitionArray.shuffle()*//*
            TransitionManager
                .beginDelayedTransition(this@GridConstraintLayout, ChangeBounds()
                    .apply {
                        interpolator = AccelerateInterpolator(0.057f)
                    })
            applyTo(this@GridConstraintLayout)
        }
        for (i in viewIdArray.indices) {
            val view =
                viewsMap[viewIdArray[i]] as View // throws an exception means something's wrong

            view.apply {
                val params = layoutParams as MarginLayoutParams
                params.topMargin = if (i < number) 0 else 15.dpToInt()
                requestLayout()
            }
        }
        Log.e("fin1", "${System.currentTimeMillis() - o}")
    }*/


    private fun ConstraintSet.constrainTopToBottom(firstView: Int, secondView: Int) =
        this.connect(firstView, ConstraintSet.TOP, secondView, ConstraintSet.BOTTOM, 0)

    private fun ConstraintSet.constrainEndToStart(firstView: Int, secondView: Int) =
        this.connect(firstView, ConstraintSet.END, secondView, ConstraintSet.START, 0)

    private fun ConstraintSet.constrainStartToEnd(firstView: Int, secondView: Int) =
        this.connect(firstView, ConstraintSet.START, secondView, ConstraintSet.END, 0)

    private fun Int.dpToInt(): Int = dpToInt(context)
}
