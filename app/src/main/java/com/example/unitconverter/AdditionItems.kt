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
    var animateStart: Animator? = null
    var animateFinal: Animator? = null
    var orient = 0
    val isInitialized get() = this::popupWindow.isInitialized
    lateinit var motionHandler: Handler
    var statusBarHeight = 0
    var viewSparseArray: SparseArray<View> = SparseArray()
    lateinit var recentlyUsed: ArrayList<Int>
    var viewArray: ArrayList<View> = arrayListOf()
    var card: MyCardView? = null
    var cardY: Float = 1f
    var longPress: Boolean = false
    var mProgress = 0f
    var bugDetected = false
    const val pkgName = "com.example.unit_converter"
    const val TextMessage = "$pkgName.TextMessage"
    const val ViewIdMessage = "$pkgName.ViewIdMessage"
    fun endAnimation(): Boolean {
        animateStart?.apply {
            if (isRunning) {
                end()
                ObjectAnimator.ofFloat(card, View.Y, cardY).start()
                animateFinal?.apply {
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