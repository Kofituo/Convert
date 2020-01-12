package com.example.unitconverter


import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.unitconverter.subclasses.*
import kotlinx.android.synthetic.main.front_page_activity.*
import kotlinx.android.synthetic.main.scroll.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page_activity)
        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        myConfiguration(this.resources.configuration.orientation)
        window.statusBarColor = Color.parseColor("#4DD0E1")

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

fun String.removeCommas(): String? {
    if (this.isBlank()) return ""

    (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {

        isParseBigDecimal = true
        return parse(this@removeCommas)?.toString()
    }
}
*/

fun BigDecimal.insertCommas(): String {
    val decimalFormat =
        (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
            applyLocalizedPattern("#,##0.####")
        }
    return decimalFormat.format(this)
}

open class SeparateThousands(val editText: EditText , val groupingSeparator: String, val decimalSeparator: String) :
    TextWatcher {

    private var busy = false
    private var lastPosition = 0
    override fun afterTextChanged(s: Editable?) {

        if (s != null && !busy) {

            busy = true
            var place = 0

            var decimalPointIndex = s.indexOf(decimalSeparator)

            if (decimalPointIndex != s.lastIndexOf(decimalSeparator)) {
                val subString = s.subSequence(decimalPointIndex + 1, s.lastIndex + 1)

                val sString = s.subSequence(0, decimalPointIndex + 1)

                val index = subString.indexOf(decimalSeparator)
                Log.e("sub", "$subString $sString  dpI ->$decimalPointIndex  sel -> ${editText.selectionStart} $index   $$lastPosition")
                if (editText.selectionStart > lastPosition) {
                    s.delete(lastPosition,lastPosition+1)
                    Log.e("as","called")
                    if (s.length >4)decimalPointIndex = editText.selectionStart-1
                }
                else {
                    //for example 123.654 ->  1.234
                    s.delete(decimalPointIndex + index + 1, decimalPointIndex + index + 2)
                    Log.e("whf", "${decimalSeparator in subString}")
                    /*while (groupingSeparator in subString) {
                        val newIndex = subString.indexOf(groupingSeparator)
                        s.delete(decimalPointIndex+newIndex,decimalPointIndex+newIndex+1)
                    }*/
                }
            }
            val groupingSeparatorIndex = s.indexOf(groupingSeparator,decimalPointIndex)
            if (groupingSeparatorIndex != -1 && decimalPointIndex != -1) {
                var start = groupingSeparatorIndex
                Log.e("start","$start   $s" )
                while (groupingSeparator in s.subSequence(start,s.lastIndex+1) ) {
                    Log.e("0","$start")
                    s.delete(start,start+1)
                    start = s.indexOf(groupingSeparator,start)
                    Log.e("1","$start")
                    if (start == -1) break
                }
            }
            var i = if (decimalPointIndex == -1) {
                s.length - 1
            } else {
                decimalPointIndex - 1
            }

            while (i >= 0) {
                val c = s[i]
                if (c == groupingSeparator[0]) {
                    s.delete(i, i + 1)

                } else {
                    if (place % 3 == 0 && place != 0) {
                        //Log.e("called","else")
                        // insert a comma to the left of every 3rd digit (counting from right to
                        // left) unless it's the leftmost digit
                        s.insert(i + 1, groupingSeparator)
                    }
                    place++
                }

                i--
            }
            busy = false
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if(s != null)lastPosition = s.indexOf(decimalSeparator)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}