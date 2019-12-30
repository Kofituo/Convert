package com.example.unitconverter.subclasses

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.core.widget.NestedScrollView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

var bugDetected =false

class MyNestedScrollView(context: Context, attributeSet: AttributeSet) : NestedScrollView (context,attributeSet) ,
            GestureDetector.OnGestureListener , GestureDetector.OnDoubleTapListener{

    private var mScroll : Int = -1

    private var scrollChanged = false

    private var detectorCompat = GestureDetectorCompat(context,this)

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        if (velocityY <= -2500) mScroll = 0
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

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        detectorCompat.onTouchEvent(ev)

        performClick()

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
        return com.example.unitconverter.handler
    }
}
