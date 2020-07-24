package com.otuolabs.unitconverter.subclasses

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.edit
import androidx.core.view.get
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils.dpToInt
import com.otuolabs.unitconverter.Utils.isTablet
import com.otuolabs.unitconverter.Utils.name
import com.otuolabs.unitconverter.builders.add
import com.otuolabs.unitconverter.builders.buildConstraintSet
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.miscellaneous.JsonConvertible.Companion.toJson
import com.otuolabs.unitconverter.miscellaneous.ViewData
import com.otuolabs.unitconverter.miscellaneous.globalPreferences
import com.otuolabs.unitconverter.miscellaneous.layoutParams
import com.otuolabs.unitconverter.miscellaneous.put
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class GridConstraintLayout(context: Context, attributeSet: AttributeSet? = null) :
        ConstraintLayout(context, attributeSet) {

    private data class GuideLines(val left: Int, val right: Int, val top: Int)

    private var activity: Activity? = null

    private var guideLine = GuideLines(-1, -1, -1)

    private fun Int.dpToInt(): Int = dpToInt(context)

    /**
     * To Prevent find view by Id
     * */
    private val viewsMap = SparseArray<View>(30)
    private val viewData = ArrayList<ViewData>(30)
    private val viewNameToId = buildMutableMap<String, Int>(30)
    private val viewNames = ArrayList<String>(30)

    override fun addView(child: View, params: ViewGroup.LayoutParams?) {
        guideLine = GuideLines(
                if (child.id == R.id.leftGuide) R.id.leftGuide else guideLine.left,
                if (child.id == R.id.rightGuide) R.id.rightGuide else guideLine.right,
                if (child.id == R.id.topGuide) R.id.topGuide else guideLine.top
        )
        if (child is MaterialCardView) {
            viewsMap.append(child.id, child)
            viewNameToId[child.name] = child.id
            viewNames.add { child.name }
            val dataTextView = child[1] as DataTextView
            viewData.add { ViewData(child.id, child.name, dataTextView.text, dataTextView.metadata) }
        }
        super.addView(child, params)
    }

    @ImplicitReflectionSerializer
    fun saveLists() {
        //save the list to shared preferences
        context.globalPreferences.edit {
            put<String> {
                key = "viewData"
                value = viewData.toJson()
            }
            put<String> {
                key = "originalList"
                value = Json.stringify(viewNames)
            }
        }
    }

    private inline val sortValue
        get() =
            if (activity?.isTablet() == true ||
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 5
            else 3

    fun sort(list: Collection<String>) {
        val number = sortValue //gets value ones per function call
        @Suppress("UNCHECKED_CAST")
        val viewIds = list.map { viewNameToId[it] } as List<Int>
        buildConstraintSet {
            this clones this@GridConstraintLayout
            for (i in viewIds.indices) {
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
            val view = viewsMap[viewIds[i]] //to throws an exception means problem
            view.layoutParams<MarginLayoutParams> {
                topMargin = if (i < number) 0 else 15.dpToInt()
            }
        }
    }

    var selectionInProgress = false

    /**
     * Called if selection is in progress
     * */
    inline fun selectionInProgress(block: () -> Unit) {
        if (selectionInProgress) block()
    }

    private lateinit var selection: Selection

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

    fun setSelectionListener(selection: Selection) {
        this.selection = selection
    }

    fun addOneToFavorites(viewName: String) = selection.addOneToFavourites(viewName)

    fun convertInfo(viewId: Int, viewName: String) = selection.convertInfo(viewId, viewName)
    interface Selection {
        fun changeSearchButton(useDefault: Boolean)
        fun addOneToFavourites(viewName: String)
        fun convertInfo(viewId: Int, viewName: String)
    }
}
