package com.example.unitconverter.subclasses

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.unitconverter.AdditionItems.mRecentlyUsed
import com.example.unitconverter.AdditionItems.originalMap
import com.example.unitconverter.AdditionItems.viewsMap
import com.example.unitconverter.R
import com.example.unitconverter.Utils.dpToInt
import com.example.unitconverter.Utils.name
import com.example.unitconverter.builders.arrayListOf
import com.example.unitconverter.builders.buildConstraintSet
import com.example.unitconverter.miscellaneous.isNotNull
import com.example.unitconverter.miscellaneous.layoutParams
import com.google.android.material.card.MaterialCardView
import java.util.*

class GridConstraintLayout(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    private data class GuideLines(val left: Int, val right: Int, val top: Int)

    private var guideLine: GuideLines = GuideLines(-1, -1, -1)

    private fun Int.dpToInt(): Int = dpToInt(context)

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child as View
        guideLine = GuideLines(
            if (child.id == R.id.leftGuide) R.id.leftGuide else guideLine.left,
            if (child.id == R.id.rightGuide) R.id.rightGuide else guideLine.right,
            if (child.id == R.id.topGuide) R.id.topGuide else guideLine.top
        )
        if (child is MaterialCardView) {
            originalMap[child.name] = child.id
            viewsMap.append(child.id, child)
        }
        super.addView(child, params)
    }

    private inline val sortValue
        get() =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5

    fun sort(map: Map<String, Int>) {
        val o = System.currentTimeMillis()
        val number = sortValue //gets value ones per function call
        val viewIds = arrayListOf<Int>(capacity = map.size)

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
                if (mRecentlyUsed[viewName] != id && id.isNotNull())
                    mRecentlyUsed[viewName] = id

                if (id.isNotNull()) viewIds.add(id)//adds the correct id
                Log.e("called", "$viewId  $id")
                continue
            }
            viewIds.add(viewId)
        }
        buildConstraintSet {
            this clones this@GridConstraintLayout
            for (i in 0 until viewIds.size) {
                val modulo = i % number
                val topViewIndex = i - number
                val topView = if (topViewIndex >= 0) viewIds[topViewIndex] else guideLine.top
                val mainView = viewIds[i]
                margin(0, true)
                when (modulo) {
                    0 -> //it means its at the left
                        constraint(mainView) {
                            it topToBottomOf topView
                            it startToEndOf guideLine.left
                            it endToStartOf viewIds[i + 1]
                            setHorizontalChainStyle(it, ConstraintSet.CHAIN_SPREAD_INSIDE)
                        }
                    number - 1 ->
                        //means its at the right
                        constraint(mainView) { its ->
                            its topToBottomOf topView
                            its startToEndOf viewIds[i - 1]
                            its endToStartOf guideLine.right
                        }
                    else ->
                        // means its at the middle
                        constraint(mainView) { its ->
                            its topToBottomOf topView
                            its startToEndOf viewIds[i - 1]
                            its endToStartOf viewIds[i + 1]
                        }
                }
            }
            TransitionManager
                .beginDelayedTransition(this@GridConstraintLayout,
                    ChangeBounds().apply { interpolator = AccelerateInterpolator(0.057f) }
                )
            this appliesTo this@GridConstraintLayout
        }
        for (i in viewIds.indices) {
            //viewSparseArray int -> View
            //view can never be null

            val view = viewsMap[viewIds[i]] as View //to throws an exception means problem
            view.layoutParams<MarginLayoutParams> {
                topMargin = if (i < number) 0 else 15.dpToInt()
            }
        }
        Log.e("fin", "${System.currentTimeMillis() - o}")
    }

    var selectionInProgress = false

    /**
     * Called if selection is in progress
     * */
    inline fun selectionInProgress(block: () -> Unit) {
        if (selectionInProgress) block()
    }

    lateinit var selection: Selection

    fun initiateSelectItems() {
        selectionInProgress = true
        selection.changeSearchButton(false)
        for (i in 0 until childCount) {
            getChildAt(i).apply {
                if (this is MyCardView) {
                    // shrink its size
                    this.apply {
                        shrinkSize()
                        showCheckBox()
                        enableCheckBox()
                    }
                }
            }
        }
    }

    fun endSelection() {
        for (i in 0 until childCount) {
            getChildAt(i).apply {
                if (this is MyCardView) {
                    // shrink its size
                    this.apply {
                        restoreSize()
                        disableCheckBox()
                    }
                }
            }
        }
        selectionInProgress = false
        selection.changeSearchButton(true)
    }

    // initialize only when needed
    private val selectionMap by lazy(LazyThreadSafetyMode.NONE) {
        ArrayDeque<String>(30)
    }

    fun addToMap(viewName: String) {
        selectionMap.offerFirst(viewName)
    }

    fun removeFromMap(viewName: String) {
        selectionMap.remove(viewName)
    }

    fun getArray() = selectionMap

    infix fun setSelectionListener(selection: Selection) {
        this.selection = selection
    }

    fun addOneToFavorites(viewName: String) = selection.addOneToFavourites(viewName)

    interface Selection {
        fun changeSearchButton(useDefault: Boolean)
        fun addOneToFavourites(viewName: String)
    }
}
