package com.example.unitconverter


import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.*
import android.util.TypedValue
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unitconverter.subclasses.*
import kotlinx.android.synthetic.main.front_page_activity.*
import kotlinx.android.synthetic.main.scroll.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round


var animateStart: Animator? = null

var animateFinal: Animator? = null

var orient = 0

var mMotion = 0

lateinit var handler: Handler

var statusBarHeight = 0

class MainActivity : AppCompatActivity() {


    private val downTime = SystemClock.uptimeMillis()
    private val eventTime = SystemClock.uptimeMillis() + 50
    private val xPoint = 0f
    private val yPoint = 0f
    private val metaState = 0
    private val motionEventDown =
        MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xPoint, yPoint, metaState)
    private val motionEventUp: MotionEvent =
        MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xPoint, yPoint, metaState)

    private val motionEventMove: MotionEvent =
        MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xPoint, yPoint, metaState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.front_page_activity)

        setSupportActionBar(app_bar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        myConfiguration(this.resources.configuration.orientation)

        window.statusBarColor = Color.parseColor("#4DD0E1")
        val rect = Rect()
        window?.decorView?.apply {
            post {
                getWindowVisibleDisplayFrame(rect)
                statusBarHeight = rect.top

                if (Build.VERSION.SDK_INT > 22) systemUiVisibility =
                    systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        /***********************************************************************************************/

        if (motion != null) {
            handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        1 -> {
                            bugDetected =
                                if (motion?.progress == 1F || motion?.progress == 0f) {
                                    false
                                } else {
                                    scrollable.dispatchTouchEvent(motionEventDown)
                                    scrollable.dispatchTouchEvent(motionEventMove)
                                    scrollable.dispatchTouchEvent(motionEventMove)
                                    scrollable.dispatchTouchEvent(motionEventUp)
                                    true
                                }
                            return
                        }
                        2 -> {
                            GlobalScope.launch {
                                delay(318)
                                if (motion?.progress != 0F) {
                                    handler.obtainMessage(1).sendToTarget()
                                }
                            }
                            return
                        }
                    }
                    return super.handleMessage(msg)
                }
            }
        }

        Area.setOnClickListener {

        }
        //app_bar.viewTreeObserver.removeOnGlobalLayoutListener()
    }

    fun test(v: View) {
        Toast.makeText(this, "well", Toast.LENGTH_SHORT).show()
    }

    private fun myConfiguration(orientation: Int) {
        return when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                orient = Configuration.ORIENTATION_PORTRAIT
                if (mMotion == 1) {
                    motion?.progress = 1f
                } else mMotion = 1
            }

            else -> {
                orient = Configuration.ORIENTATION_LANDSCAPE
                mMotion = 1
            }
        }
    }

    // create full screen mode
    fun showBars() {
        window.decorView.systemUiVisibility = //SYSTEM_UI_FLAG_LAYOUT_STABLE or
            SYSTEM_UI_FLAG_VISIBLE or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    fun dimBars() {
        window?.decorView?.systemUiVisibility = //SYSTEM_UI_FLAG_LAYOUT_STABLE or
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    SYSTEM_UI_FLAG_IMMERSIVE or
                    SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    /**
    private var mVelocityTracker: VelocityTracker? = null

    private var callAgain = 2
     */

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //Log.e("animate","")

        if (ev.actionMasked == MotionEvent.ACTION_DOWN) bugDetected = false

        if (bugDetected && ev.actionMasked != MotionEvent.ACTION_UP) {
            return true
        } else {
            bugDetected = false
        }
        if (ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            bugDetected = true
            handler.obtainMessage(1).sendToTarget()
        }

        endAnimation()
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.front_menu, menu)
        return true

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        app_bar_bottom = app_bar.bottom - app_bar.top

    }

    override fun onDestroy() {
        super.onDestroy()
        if (pw is MyPopupWindow)
            pw.dismiss()
    }
}

fun endAnimation(): Boolean {
    if ((animateStart != null && animateStart!!.isRunning)) {
        ////////////////////////////////////
        animateStart?.end()
        ///////////////////////////////////
        ObjectAnimator.ofFloat(card, Y, cardY).start()
        animateFinal!!.apply {
            duration = 200
            start()
        }
        if (pw.isShowing) pw.dismiss()
        return true
    }
    return false
}

var app_bar_bottom = 0

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


/*

    <!--E6F5F5F5/////E6000000-->


override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.front_menu,menu)
        return true
    }
lateinit var fs : MotionLayout

/*
        val a = listOf(
            "#FCE4EC",
            "#FFF3E5F5",
            "#E3F2FD",
            "#FFF3E0",
            "#FFF8E1",
            "#FFFDE7",
            "#FFCDD2",
            "#E0F7FA",
            "#F8BBD0",
            "#E1F5FE",
            "#FBE9E7",
            "#C5CAE9",
            "#BBDEFB",
            "#B3E5FC",
            "#B2EBF2",
            "#E8F5E9",
            "#DCEDC8",
            "#E1BEE7",
            "#D1C4E9",
            "#F0F4C3",
            "#FFECB3",
            "#B2DFDB",
            "#C8E6C9",
            "#F1F8E9",
            "#FFF9C4",
            "#FFE0B2",
            "#FFCCBC",
            "#E57373",
            "#F06292",
            "#BA68C8",
            "#9575CD",
            **************************************************cccab5*************************
 */
            "#F9FBE7",
            "#E0F2F1",
            "#FF8A65",
            "#EDE7F6",
            "#7986CB",
            "#64B5F6",
            "#4FC3F7",
            "#4DD0E1",
            "#FFFFEBEE",
            "#E8EAF6",
            "#FFB74D",
            "#FFD54F",
            "#FFF176",
            "#DCE775",
            "#AED581",
            "#FFEE58",
            "#D4E157",
            "#9CCC65",
            "#81C784",
            "#4DB6AC",
            "#EF5350",
            "#EC407A",
            "#AB47BC",
            "#7E57C2",
            "#5C6BC0",
            "#42A5F5",
            "#29B6F6",
            "#26C6DA",
            "#66BB6A",
            "#26A69A",
            "#FF7043",
            "#FFA726",
            "#FFCA28",
            "#FFFFFF"
        )

        if (_colour == a.size) {
            _colour = 0
            Toast.makeText(this, "STARTING OVER", Toast.LENGTH_LONG).show()
        }

        window.statusBarColor = Color.parseColor(a[_colour])
        if (motion == null) app_bar.setBackgroundColor(Color.parseColor(a[_colour]))
        else motion.setBackgroundColor(Color.parseColor(a[_colour]))

        _colour += 1

 */


/*

Mass.setOnClickListener {
Mass.setOnTouchListener { v, event ->
            val imageView = Mass.getChildAt(0) as ImageView
            (Temperature.getChildAt(0) as ImageView).setImageDrawable(imageView.drawable)

            false
        }

            var rect = Rect()
            Mass.getGlobalVisibleRect(rect)
            //Log.e(
                //   "topleft  topright",
                "${Area.width} ${Area.height} ${Area.top}  ${Area.y}  ${Area.x}  ${Area.bottom}   "
            //)
            Log.e("well","${rect.width()}  ${rect.height()}  ${rect.left}  ${rect.right}  ${rect.top}  ${rect.bottom} ")
            val lo = IntArray(2)
            Mass.getLocationOnScreen(lo)
            Log.e("area", "${lo[0]} , ${lo[1]} , ${Mass.width}, ${Mass.height}")
        }
            mb?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }



        Temperature.setOnClickListener {
            val icon = this.resources.getIdentifier("ic_lacee","drawable",this.packageName)
            Log.e("adad","$icon")
            val drawable = this.resources.getDrawable(icon,null)
            drawable.setBounds(0,0,50,50)
            tesid.setImageDrawable(drawable)
        }


        Area.setOnClickListener {
            var rect = Rect()
            //Area.getGlobalVisibleRect(rect)
            Log.e("topleft  topright","${Area.width} ${Area.height} ${Area.top}  ${Area.y}  ${Area.x}  ${Area.bottom}   ")
            //Log.e("well","${rect.width()}  ${rect.height()}  ${rect.left}  ${rect.right}  ${rect.top}  ${rect.bottom} ")
            val lo = IntArray(2)
            Area.getLocationOnScreen(lo)
            Log.e("area","${lo[0]} , ${lo[1]} , ${Area.width }, ${Area.height}")
            //Log.e("screen","${displayMetrics.widthPixels}  , ${displayMetrics.heightPixels}")

            //pw.isTouchable = true
            pw.setTouchInterceptor { v, event ->
                Log.e("touch","$v  $event")
                val icon = this.resources.getIdentifier("ic_lacee","drawable",this.packageName)
                Log.e("adad","$icon")
                val drawable = this.resources.getDrawable(icon,null)
                drawable.setBounds(0,0,50,50)
                tesid.setImageDrawable(drawable)
                v.findViewById<View>(R.id.info).getGlobalVisibleRect(rect)
                val displayMetrics = DisplayMetrics()

                windowManager.defaultDisplay.getMetrics(displayMetrics)
                Log.e("screen","${displayMetrics.widthPixels}  , ${displayMetrics.heightPixels}")
                val plusTenPercent = round((2.toDouble()/100) * displayMetrics.widthPixels).toInt()
                Log.e("percent","$plusTenPercent")
                val lo = IntArray(2)
                v.getLocationOnScreen(lo)

                Log.e("quickaction  x  y  width   height","${lo[0]} , ${lo[1]} , ${v.width }, ${v.height}")
                Mass.getLocationOnScreen(lo)
                Log.e("mass","${lo[0]} , ${lo[1]} , ${Mass.width }, ${Mass.height}")
                Area.getLocationOnScreen(lo)
                Log.e("area","${lo[0]} , ${lo[1]} , ${Area.width }, ${Area.height}")
                Temperature.getLocationOnScreen(lo)
                Log.e("temp","${lo[0]} , ${lo[1]} , ${Temperature.width }, ${Temperature.height}")
                false
            }
        }

        /**********************************************************************************************/

 */