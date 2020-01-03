package com.example.unitconverter


import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.*
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
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
lateinit var app_context: Context
var statusBarHeight = 0
var viewIdMap: MutableMap<Int, View> = mutableMapOf()
lateinit var recentlyUsed: ArrayList<Int>

//change manifest setting to backup allow true
class MainActivity : AppCompatActivity(), BottomSheetFragment.SortDialogInterface {

    private val downTime = SystemClock.uptimeMillis()
    private val eventTime = SystemClock.uptimeMillis() + 10
    private val xPoint = 0f
    private val yPoint = 0f
    private val metaState = 0
    private val motionEventDown =
        MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xPoint, yPoint, metaState)
    private val motionEventUp: MotionEvent =
        MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xPoint, yPoint, metaState)

    private val motionEventMove: MotionEvent =
        MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xPoint, yPoint, metaState)

    private lateinit var viewIdArray: ArrayList<Int>
    private var sortValue = -1
    private var sharedArray = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page_activity)
        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        myConfiguration(this.resources.configuration.orientation)
        window.statusBarColor = Color.parseColor("#4DD0E1")
        app_context = applicationContext
        Log.e("id", "${dataStorage.id}")
        val rect = Rect()

        window?.decorView?.apply {
            post {
                getWindowVisibleDisplayFrame(rect)
                statusBarHeight = rect.top

                if (Build.VERSION.SDK_INT > 22) systemUiVisibility =
                    systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        Toast.makeText(app_context, "hi bro", Toast.LENGTH_LONG).show()
        if (motion != null) {
            handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        1 -> {
                            val motion = motion
                            bugDetected =
                                if ((motion?.progress) == 1F || motion.progress == 0f) {
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
        sortValue =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                3
            else
                5

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            // gets the array if its there
            viewIdArray = sharedPreferences.getIntegerArrayList("originalLis", arrayListOf())
            recentlyUsed = sharedPreferences.getIntegerArrayList("recentlyUsed", arrayListOf())
            sharedArray = sharedPreferences.getIntegerArrayList("selectedOrder", arrayListOf())
            descending = sharedPreferences.getBoolean("descending", false)
            recentlyUsedBool = sharedPreferences.getBoolean("recentlyUsedBoolean", false)
            // does this check in case the ids have been changed or new views added
            //val check: ArrayList<Int> = arrayListOf() not necessaray
            //for (i in viewArray) check.add(i.id)
            // means the array is not there or has to be updated
            if (viewIdArray.isEmpty()) {
                for (i in viewArray) viewIdArray.add(i.id)
                putIntegerArrayList("originalLis", viewIdArray)
            }
            if (recentlyUsed.isEmpty()) {
                recentlyUsed.addAll(viewIdArray)
                putIntegerArrayList("recentlyUsed", recentlyUsed)
            }
            // creating the map
            for (i in viewIdArray.indices) viewIdMap[viewIdArray[i]] = viewArray[i]
            if (sharedArray.isNotEmpty()) {
                grid.sort(sortValue, sharedArray)
                Log.e("hisd", "$sharedArray  ${sharedArray.size}  ")
            }
            apply()
        }

        onCreateCalled = true
    }


    override fun selection(firstSelection: Int, secondSelection: Int) {
        recentlyUsedBool = false
        var bufferArray = arrayListOf<Int>()
        if (firstSelection == -1) {
            // use default
            grid.sort(sortValue, viewIdArray)
            sharedArray = ArrayList(viewIdArray)
            return
        }
        descending = secondSelection == R.id.descending
        if (firstSelection == R.id.titleButton) {
            val newArray =
                viewArray.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            for (i in
            if (descending)
                newArray.reversed()
            else newArray
            ) bufferArray.add(i.id)

        } else {
            recentlyUsedBool = true
            bufferArray =
                if (recentlyUsed == viewIdArray || descending)
                    ArrayList(recentlyUsed)
                else ArrayList(
                    recentlyUsed.reversed()
                )
        }
        grid.sort(sortValue, bufferArray)
        sharedArray = ArrayList(bufferArray)
        bufferArray.clear()
    }

    private var descending = false

    private var recentlyUsedBool = false

    private var onCreateCalled = false
    override fun onResume() {
        super.onResume()
        if (recentlyUsedBool && (recentlyUsed != viewIdArray) && !onCreateCalled) {
            grid.sort(
                sortValue, if (descending)
                    recentlyUsed
                else
                    (ArrayList(
                        recentlyUsed.reversed()
                    ))
            )
        }
        onCreateCalled = false
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putIntegerArrayList("recentlyUsed", recentlyUsed)
            val arrayHasChanged = recentlyUsed != viewIdArray
            putIntegerArrayList(
                "selectedOrder", if (arrayHasChanged && recentlyUsedBool) {
                    if (descending) recentlyUsed
                    else ArrayList(
                        recentlyUsed.reversed()
                    )
                } else sharedArray
            )
            putBoolean("descending", descending)
            putBoolean("recentlyUsedBoolean", recentlyUsedBool)
            apply()
        }
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

        if (bugDetected) return true
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort -> {
                val bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(supportFragmentManager, "dialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        app_bar_bottom = app_bar.bottom - app_bar.top

    }

    override fun onDestroy() {
        super.onDestroy()
        if (pw is MyPopupWindow)
            pw.dismiss()
        viewArray.clear()
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
fun SharedPreferences.Editor.putIntegerArrayList(
    key: String,
    list: ArrayList<Int>
): SharedPreferences.Editor {
    putString(key, list.joinToString(","))
    return this
}

fun SharedPreferences.getIntegerArrayList(
    key: String,
    default: ArrayList<Int>
): ArrayList<Int> {
    val value = getString(key, null)
    if (value.isNullOrBlank()) return default
    return ArrayList(value.split(",").map { it.toInt() })
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

/*
android:width="75dp"
    android:height="90dp"
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
 2477.399
2019-12-25 15:08:01.250 2201-2201/com.example.unitconverter E/5: called 5
2019-12-25 15:08:01.408 2201-2201/com.example.unitconverter E/1: called 1
2019-12-25 15:08:01.729 2201-2576/com.example.unitconverter E/4: called 4
2019-12-25 15:08:01.744 2201-2201/com.example.unitconverter E/2: called 2
2019-12-25 15:08:01.745 2201-2201/com.example.unitconverter E/5: called 5
2019-12-25 15:08:01.745 2201-2201/com.example.unitconverter E/side: way
2019-12-25 15:08:01.745 2201-2201/com.example.unitconverter E/6: called 6
2019-12-25 15:08:01.745 2201-2201/com.example.unitconverter E/6: called 6
 */