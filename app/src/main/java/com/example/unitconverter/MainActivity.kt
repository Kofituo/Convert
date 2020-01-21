package com.example.unitconverter


import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.*
import android.util.Log
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.unitconverter.Utils.app_bar_bottom
import com.example.unitconverter.Utils.getIntegerArrayList
import com.example.unitconverter.Utils.name
import com.example.unitconverter.Utils.putIntegerArrayList
import com.example.unitconverter.subclasses.*
import kotlinx.android.synthetic.main.front_page_activity.*
import kotlinx.android.synthetic.main.scroll.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


lateinit var popupWindow: MyPopupWindow
var animateStart: Animator? = null
var animateFinal: Animator? = null
var orient = 0
lateinit var motionHandler: Handler
var statusBarHeight = 0
var viewSparseArray: SparseArray<View> = SparseArray()
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

    private var h = 0
    private var w = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page_activity)
        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        myConfiguration(this.resources.configuration.orientation)
        window.statusBarColor = Color.parseColor("#4DD0E1")
        h = resources.displayMetrics.heightPixels / 2
        w = resources.displayMetrics.widthPixels / 2
        Log.e(
            "id",
            "${dataStorage.id} ${resources.displayMetrics.widthPixels}  ${resources.displayMetrics.density}"
        )
        val rect = Rect()

        window?.decorView?.apply {
            post {
                getWindowVisibleDisplayFrame(rect)
                statusBarHeight = rect.top

                if (Build.VERSION.SDK_INT > 22) systemUiVisibility =
                    systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        Toast.makeText(this, "hi bro ", Toast.LENGTH_LONG).show()
        val viewModel = ViewModelProviders.of(this@MainActivity)[ConvertViewModel::class.java]
        motion?.apply {
            motionHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        1 -> {
                            bugDetected =
                                if (progress == 1F || progress == 0f) {
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
                                if (progress != 0F) {
                                    motionHandler.obtainMessage(1).sendToTarget()
                                }
                            }
                            return
                        }
                    }
                    return super.handleMessage(msg)
                }
            }
            progress = viewModel.motionProgress
        }
        viewModel.motionProgress = 1f
        sortValue =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5

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
            // not so good
            //for (i in viewIdArray.indices) viewSparseArray[viewIdArray[i]] = viewArray[i]
            for (i in viewIdArray.indices) viewSparseArray.append(viewIdArray[i], viewArray[i])
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
            if (descending) newArray.reversed()
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
                sortValue,
                if (descending) recentlyUsed
                else (ArrayList(recentlyUsed.reversed()))
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

        orient =
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                Configuration.ORIENTATION_PORTRAIT
            else Configuration.ORIENTATION_LANDSCAPE
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

        if (ev.actionMasked == MotionEvent.ACTION_DOWN) bugDetected = false

        if (bugDetected) return true
        if (ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            bugDetected = true
            motionHandler.obtainMessage(1).sendToTarget()
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
                BottomSheetFragment().apply {
                    show(supportFragmentManager, "dialog")
                }
                true
            }
            R.id.prefixes -> {
                Intent(this, ConvertActivity::class.java).apply {
                    putExtra(TextMessage, "Prefix")
                    putExtra(ViewIdMessage, R.id.prefixes)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(this)
                }
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
        if (::popupWindow.isInitialized) popupWindow.dismiss()
        viewArray.clear()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "restart", Toast.LENGTH_LONG).show()
    }
}

fun endAnimation(): Boolean {
    animateStart?.apply {
        if (isRunning) {
            end()
            ObjectAnimator.ofFloat(card, Y, cardY).start()
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