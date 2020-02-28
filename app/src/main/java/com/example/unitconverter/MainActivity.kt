package com.example.unitconverter


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.bugDetected
import com.example.unitconverter.AdditionItems.endAnimation
import com.example.unitconverter.AdditionItems.isInitialized
import com.example.unitconverter.AdditionItems.mRecentlyUsed
import com.example.unitconverter.AdditionItems.motionHandler
import com.example.unitconverter.AdditionItems.orient
import com.example.unitconverter.AdditionItems.originalMap
import com.example.unitconverter.AdditionItems.popupWindow
import com.example.unitconverter.AdditionItems.statusBarHeight
import com.example.unitconverter.AdditionItems.viewsMap
import com.example.unitconverter.Utils.app_bar_bottom
import com.example.unitconverter.Utils.name
import com.example.unitconverter.Utils.reversed
import com.example.unitconverter.Utils.toJson
import com.example.unitconverter.Utils.values
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.miscellaneous.DeserializeMap
import com.example.unitconverter.miscellaneous.isNeitherNullNorEmpty
import com.example.unitconverter.subclasses.ConvertViewModel
import kotlinx.android.synthetic.main.front_page_activity.*
import kotlinx.android.synthetic.main.scroll.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

//change manifest setting to backup allow true
@UnstableDefault
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

    private var sortValue = -1

    private var h = 0
    private var w = 0

    private lateinit var mSelectedOrderArray: Map<String, Int>

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
        val viewModel = ViewModelProvider(this@MainActivity)[ConvertViewModel::class.java]
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
            //rethinking almost everything to make sure it works well always
            /**
             * recently used would also be a map : String -> Int
             * when some ids have changed it would get updated ...keeping the order intact
             * */
            sharedPreferences.getString("mRecentlyUsed", "").apply {
                mRecentlyUsed =
                    if (this.isNeitherNullNorEmpty()) {
                        Json.parse(DeserializeMap, this).toMutableMap()
                    } else originalMap.apply { putString("mRecentlyUsed", toJson()) }
                Log.e("recent", "res  $mRecentlyUsed")
            }

            sharedPreferences.getString("mSelectedOrder", "").apply {
                mSelectedOrderArray =
                    if (this.isNeitherNullNorEmpty()) {
                        //sorting occurred
                        //so we have to keep it like that
                        Log.e("this", "$this ")
                        val selectedOrder =
                            Json.parse(DeserializeMap, this)// as ArrayMap<String, Int>
                        grid.sort(sortValue, selectedOrder)
                        selectedOrder
                    } else mapOf()
            }
            Log.e("select", "$mSelectedOrderArray ko")
            descending = sharedPreferences.getBoolean("descending", false)
            recentlyUsedBool = sharedPreferences.getBoolean("recentlyUsedBoolean", false)
            apply()
        }
        onCreateCalled = true
    }

    override fun selection(firstSelection: Int, secondSelection: Int) {
        recentlyUsedBool = false
        var temporalMap = mutableMapOf<String, Int>()
        if (firstSelection == -1) {
            // use default
            grid.sort(sortValue, originalMap)
            mSelectedOrderArray = originalMap
            Log.e("1", "1")
            return
        }
        descending = secondSelection == R.id.descending
        Log.e("des", "$descending  $secondSelection  ${R.id.descending}")
        if (firstSelection == R.id.titleButton) {
            //sort by title
            val newArray =
                viewsMap.values.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            for (i in
            if (descending) newArray.reversed()
            else newArray
            ) temporalMap[i.name] = i.id
            Log.e("2", "2  $temporalMap  $newArray  ")

        } else {
            recentlyUsedBool = true
            Log.e(
                "-90",
                "-0 $descending  ${mRecentlyUsed == originalMap}  ${mRecentlyUsed.values == originalMap.values}"
            )
            Log.e("ori", "$mRecentlyUsed pp  $originalMap")
            temporalMap =
                if (descending || mRecentlyUsed.values == originalMap.values)
                    mRecentlyUsed.toMutableMap()
                else {
                    mRecentlyUsed.reversed()
                }
            Log.e("3", "3  $temporalMap ")
        }
        grid.sort(sortValue, temporalMap)
        mSelectedOrderArray = temporalMap.toMutableMap()
        //bufferArray.clear()
    }

    private var descending = false

    /**
     * True means recently used was selected
     * */
    private var recentlyUsedBool = false

    private var onCreateCalled = false

    override fun onResume() {
        super.onResume()
        /**
         * called when from convert activity
         * */
        if (recentlyUsedBool && !onCreateCalled && mRecentlyUsed.values != originalMap.values) {
            //Log.e("res","res  $descending")
            if (descending) grid.sort(sortValue, mRecentlyUsed)
            //since the problem of different ids is corrected right from onCreate
            //it's safe to do the following
            else grid.sort(sortValue, mRecentlyUsed.reversed())
        }
        onCreateCalled = false
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            //putIntegerArrayList("recentlyUsed", recentlyUsed)
            /*Log.e("pause","$descending  $onCreateCalled  $recentlyUsedBool")
            Log.e("json","${mRecentlyUsed.toJson()} " )*/
            Log.e(
                "resce",
                "${mSelectedOrderArray.toJson()} AAAA${mRecentlyUsed.reversed().toJson()} BBBB ${mRecentlyUsed.toJson()}"
            )
            putString("mRecentlyUsed", mRecentlyUsed.toJson())
            val arrayHasChanged = mRecentlyUsed.values != originalMap.values
            putString(
                "mSelectedOrder", if (arrayHasChanged && recentlyUsedBool) {
                    if (descending) mRecentlyUsed.toJson()
                    else mRecentlyUsed.reversed().toJson()

                } else mSelectedOrderArray.toJson()
            )
            putBoolean("descending", descending)
            putBoolean("recentlyUsedBoolean", recentlyUsedBool)
            apply()
        }
    }

    fun test(v: View) =
        Toast.makeText(this, "well", Toast.LENGTH_SHORT).show()

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

    /*
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
                buildIntent(this, ConvertActivity::class.java) {
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
        if (isInitialized) popupWindow.dismiss()
    }
}

