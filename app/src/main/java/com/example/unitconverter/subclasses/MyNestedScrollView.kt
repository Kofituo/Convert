package com.example.unitconverter.subclasses

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.core.widget.NestedScrollView
import com.example.unitconverter.Utils.dpToInt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

var bugDetected =false

class MyNestedScrollView(context: Context, attributeSet: AttributeSet) : NestedScrollView (context,attributeSet) ,
    GestureDetector.OnGestureListener {

    private var mScroll : Int = -1

    private var scrollChanged = false

    private val displayMetrics: DisplayMetrics = resources.displayMetrics
    private val h = displayMetrics.heightPixels.toDouble() / displayMetrics.ydpi.toDouble()
    private val u = displayMetrics.widthPixels.toDouble() / displayMetrics.xdpi.toDouble()
    private val screenSize = (round(sqrt((h.pow(2)) + (u.pow(2))) * 10) / 10)

    private var detectorCompat = GestureDetectorCompat(context,this)

    private val minVelocity = (-58).dpToInt(context)

    override fun onShowPress(e: MotionEvent?) = Unit

    override fun onSingleTapUp(e: MotionEvent?): Boolean = true

    override fun onDown(e: MotionEvent?): Boolean = true

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        Log.e("size", "$screenSize")
        if (e1 != null && e2 != null) {
            val iDontKnow = (e2.rawY - e1.rawY) * screenSize
            val justDoIt = minVelocity * screenSize
            if (iDontKnow < justDoIt)
                mScroll = 0
        }
        return true
    }


    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        detectorCompat.onTouchEvent(ev)

        return super.dispatchTouchEvent(ev)

    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {

        performClick()
        //return super.onTouchEvent(ev)

        if (scrollChanged || !canScrollVertically(1)) {
            scrollChanged = false
        } else if (mScroll == 0) {
            mScroll = -1
            GlobalScope.launch {
                delay(220)
                val first = mProgress
                delay(40)
                if (abs(mProgress - first) <= 0.065 && !bugDetected) {

                    bugDetected = true
                    handler.obtainMessage(1).sendToTarget()
                }
            }

        } else if (ev?.actionMasked == MotionEvent.ACTION_UP) {
            requestDisallowInterceptTouchEvent(
                true
            )
        }

        return super.onTouchEvent(ev)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {

        scrollChanged = true
        if (t == 0) handler.obtainMessage(2).sendToTarget()
        super.onScrollChanged(l, t, oldl, oldt)
    }


    override fun getHandler(): Handler {
        return com.example.unitconverter.motionHandler
    }
}
