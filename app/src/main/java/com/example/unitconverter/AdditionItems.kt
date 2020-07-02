package com.example.unitconverter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Handler
import android.util.SparseArray
import android.view.View
import com.example.unitconverter.subclasses.MyCardView
import com.example.unitconverter.subclasses.MyPopupWindow

object AdditionItems {
    lateinit var popupWindow: MyPopupWindow

    /**
     * bouncing animation
     * */
    var animateStart: Animator? = null

    /**
     * Restores the card view to its original position
     * */
    var animationEnd: Animator? = null
    val isInitialized get() = ::popupWindow.isInitialized
    lateinit var motionHandler: Handler
    var statusBarHeight = 0

    /**
     * To prevent find view by id.
     * */
    val viewsMap: SparseArray<View> = SparseArray(30)

    /**
     * Maps the view name to id
     * */
    val originalMap = LinkedHashMap<String, Int>(30)

    /**
     * Map which keeps track of opened activities
     * in descending order (last open is the first in the map)
     * */
    lateinit var mRecentlyUsed: MutableMap<String, Int>

    //var viewArray: ArrayList<View> = arrayListOf()
    var card: MyCardView? = null
    var cardY: Float = 1f
    var longPress: Boolean = false
    var mProgress = 0f
    var bugDetected = false
    const val pkgName = "com.example.unit_converter"
    const val TextMessage = "$pkgName.TextMessage"
    const val ViewIdMessage = "$pkgName.ViewIdMessage"
    const val FavouritesCalledIt = "$pkgName.favourites_favourites_called_it"
    const val ToolbarColor = "$pkgName.toolbarColor"
    const val Author = "Kofi Otuo"
    const val SearchActivityExtra = "$pkgName.searchActivity"

    fun endAnimation(): Boolean {
        animateStart?.apply {
            if (isRunning) {
                end() //stops the bouncing animation
                ObjectAnimator.ofFloat(card, View.Y, cardY).start() // keeps the y value constant
                animationEnd?.apply {
                    duration = 200
                    start()
                }
                if (popupWindow.isShowing) popupWindow.dismiss()
                return true
            }
        }
        return false
    }
}