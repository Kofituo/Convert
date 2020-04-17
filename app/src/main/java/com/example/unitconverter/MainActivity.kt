package com.example.unitconverter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
import com.example.unitconverter.AdditionItems.originalMap
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.AdditionItems.popupWindow
import com.example.unitconverter.AdditionItems.statusBarHeight
import com.example.unitconverter.AdditionItems.viewsMap
import com.example.unitconverter.Utils.app_bar_bottom
import com.example.unitconverter.Utils.getNameToViewMap
import com.example.unitconverter.Utils.name
import com.example.unitconverter.Utils.reversed
import com.example.unitconverter.Utils.toJson
import com.example.unitconverter.Utils.values
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.subclasses.*
import com.example.unitconverter.subclasses.FavouritesData.Companion.favouritesBuilder
import kotlinx.android.synthetic.main.front_page_activity.*
import kotlinx.android.synthetic.main.scroll.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

//change manifest setting to backup allow true
@UnstableDefault
class MainActivity : AppCompatActivity(), BottomSheetFragment.SortDialogInterface,
    GridConstraintLayout.Selection {

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

    private var h = 0
    private var w = 0

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var mSelectedOrderArray: Map<String, Int>

    companion object {
        const val FAVOURITES = 0
    }

    @OptIn(ImplicitReflectionSerializer::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page_activity)
        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        window.statusBarColor = Color.parseColor("#4DD0E1")
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
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

        val viewModel = ViewModelProvider(this@MainActivity)[ConvertViewModel::class.java]
        motion {
            motionHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        1 -> {
                            bugDetected =
                                if (progress == 1F || progress == 0f) {
                                    false
                                } else {
                                    scrollable.apply {
                                        dispatchTouchEvent(motionEventDown)
                                        dispatchTouchEvent(motionEventMove)
                                        dispatchTouchEvent(motionEventMove)
                                        dispatchTouchEvent(motionEventUp)
                                    }
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

        sharedPreferences {
            editPreferences {
                //rethinking almost everything to make sure it works well always
                /**
                 * recently used would also be a map : String -> Int
                 * when some ids have changed it would get updated ...keeping the order intact
                 * */

                get<String?>("mRecentlyUsed") {
                    mRecentlyUsed =
                        if (this.hasValue())
                            Json.parse(DeserializeStringIntMap, this)
                        else originalMap.apply {
                            put<String> {
                                key = "mRecentlyUsed"
                                value = toJson()
                            }
                        }
                    Log.e("recent", "res  $mRecentlyUsed")
                }
                get<String?>("mSelectedOrder") {
                    mSelectedOrderArray =
                        if (this.hasValue()) {
                            //sorting occurred
                            //so we have to keep it like that
                            Log.e("this", "$this ")
                            val selectedOrder =
                                Json.parse(DeserializeStringIntMap, this)
                            grid.sort(selectedOrder)
                            selectedOrder
                        } else mapOf()
                }
                Log.e("select", "$mSelectedOrderArray ko")
                descending = get("descending")
                recentlyUsedBool = get("recentlyUsedBoolean")
                apply()
            }
        }
        onCreateCalled = true

        grid setSelectionListener this
    }


    private inline fun motion(block: MyMotionLayout.() -> Unit) =
        motion?.apply(block)

    override fun selection(firstSelection: Int, secondSelection: Int) {
        recentlyUsedBool = false /// reset the value
        if (firstSelection == -1) {
            // use default
            grid.sort(originalMap)
            mSelectedOrderArray = originalMap
            Log.e("1", "1")
            return
        }
        val temporalMap = LinkedHashMap<String, Int>(30)
        descending = secondSelection == R.id.descending
        Log.e("des", "$descending  $secondSelection  ${R.id.descending}")
        if (firstSelection == R.id.titleButton)
        //sort by title
            viewsMap.values {
                sortWith(
                    if (descending)
                        compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }
                    else compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                )
                for (i in this)
                    temporalMap[i.name] = i.id
                Log.e("2", "2  $temporalMap  $this")
            }
        else {
            recentlyUsedBool = true
            Log.e(
                "-90",
                "-0 $descending ${mRecentlyUsed == originalMap} ${mRecentlyUsed.values == originalMap.values}"
            )
            Log.e("ori", "$mRecentlyUsed pp  $originalMap")
            temporalMap.putAll(
                if (descending)
                    mRecentlyUsed
                else mRecentlyUsed.reversed()
            )
            Log.e("3", "3  $temporalMap")
        }
        temporalMap.also {
            grid.sort(it)
            mSelectedOrderArray = it
        }
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
            Log.e("res", "res  $descending  ${mRecentlyUsed.reversed()}")
            if (descending)
                grid.sort(mRecentlyUsed)
            //since the problem of different ids is corrected right from onCreate
            //it's safe to do the following
            else grid.sort(mRecentlyUsed.reversed())
        }
        onCreateCalled = false

        /**
         * case where prefixes was selected during sorting or any
         * of those activities from the menu at the right
         * */

        grid {
            selectionInProgress {
                endSelection()
            }
        }
    }

    private inline fun grid(block: GridConstraintLayout.() -> Unit) =
        grid.apply(block)

    override fun onPause() {
        super.onPause()
        editPreferences {
            val recentlyUsed = mRecentlyUsed.toJson()
            put<String> {
                key = "mRecentlyUsed"
                value = recentlyUsed
            }
            val arrayHasChanged = mRecentlyUsed.values != originalMap.values
            put<String> {
                key = "mSelectedOrder"
                value = if (arrayHasChanged && recentlyUsedBool) {
                    if (descending) recentlyUsed
                    else mRecentlyUsed.reversed().toJson()
                } else mSelectedOrderArray.toJson()
            }
            put<Boolean> {
                key = "descending"
                value = descending
            }
            put<Boolean> {
                key = "recentlyUsedBoolean"
                value = recentlyUsedBool
            }
            apply()
        }
    }

    private inline fun editPreferences(block: SharedPreferences.Editor.() -> Unit) =
        editPreferences(sharedPreferences, block)

    private inline fun sharedPreferences(block: SharedPreferences.() -> Unit) =
        sharedPreferences(sharedPreferences, block)

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

    private lateinit var searchButton: MenuItem

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.front_menu, menu)
        menu?.apply {
            searchButton = findItem(R.id.search_button)
        }
        return true
    }

    override fun onBackPressed() {
        grid {
            selectionInProgress {
                return endSelection()
            }
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.sort -> {
            BottomSheetFragment().apply {
                show(supportFragmentManager, "dialog")
            }
            true
        }
        R.id.prefixes -> {
            buildIntent<ConvertActivity> {
                putExtra(TextMessage, "Prefix")
                putExtra(ViewIdMessage, R.id.prefixes)
                startActivity(this)
            }
        }
        R.id.favourite -> {
            buildIntent<FavouritesActivity> {
                sharedPreferences {
                    get<String?>("favouritesArray") {
                        if (this.hasValue()) {
                            Log.e("has ", "value")
                            //send the list to the activity
                            val map = Json.parse(DeserializeStringStringMap, this)
                                .apply {
                                    if (isEmpty()) {
                                        startActivity(this@buildIntent)
                                        return true
                                    }
                                }
                            val favouritesList: ArrayList<FavouritesData>
                            getNameToViewMap().apply {
                                favouritesList = map.map {
                                    val view = this[it.key] //shouldn't be null though
                                    view?.run {
                                        this as MyCardView
                                        val textView = (this@run.getChildAt(1) as DataTextView)
                                        favouritesBuilder {
                                            drawableId = drawableIds[this@run.id]
                                            topText = textView.text
                                            metadata = textView.metadata
                                            cardId = this@run.id
                                            Log.e("item", "$this ${textView.text}  ")
                                        }
                                    }
                                }.run {
                                    filterNotNullTo(ArrayList(size)) // none should be null though
                                }
                            }
                            this@buildIntent.putExtra("$pkgName.favourites_list", favouritesList)
                        }
                    }
                }
                startActivity(this)
            }
            true
        }
        R.id.search_button -> {
            if (!useDefault) {
                // store selected items
                grid {
                    val map = getMap()
                    if (map.isEmpty()) {
                        endSelection()
                        return true
                    }
                    sharedPreferences {
                        editPreferences {
                            get<String?>("favouritesArray") {
                                if (this.hasValue()) {
                                    val previousMap =
                                        Json.parse(DeserializeStringStringMap, this)
                                    val updatedMap = map.reversed() + previousMap
                                    put<String> {
                                        key = "favouritesArray"
                                        value = updatedMap.toJson()
                                    }
                                } else {
                                    put<String> {
                                        key = "favouritesArray"
                                        value = map.reversed().toJson()
                                    }
                                }
                                apply()
                            }
                        }
                        grid {
                            endSelection()
                        }
                        showToast {
                            resId = R.string.added_favourites
                            duration = Toast.LENGTH_LONG
                        }
                    }
                }
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        app_bar_bottom = app_bar.bottom - app_bar.top
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isInitialized) popupWindow.dismiss()
    }

    private var useDefault = true

    override fun changeSearchButton(useDefault: Boolean) {
        this.useDefault = useDefault
        if (useDefault)
            searchButton.apply {
                icon = getDrawable(R.drawable.add_to_search)
                title = getString(R.string.search)
                (icon as AnimationDrawable).apply {
                    setEnterFadeDuration(300)
                    setExitFadeDuration(500)
                    start()
                }
            } else
            searchButton.apply {
                title = getString(R.string.add_to_favourites)
                icon = getDrawable(R.drawable.search_to_add)
                (icon as AnimationDrawable).apply {
                    setEnterFadeDuration(300)
                    setExitFadeDuration(500)
                    start()
                }
            }
    }

    override fun addOneToFavourites(viewName: String) {
        sharedPreferences {
            editPreferences {
                get<String?>("favouritesArray") {
                    val updatedMap: MutableMap<String, String>
                    if (this.hasValue()) {
                        val previousMap = Json.parse(DeserializeStringStringMap, this)
                        updatedMap = buildMutableMap(previousMap.size + 1) {
                            put {
                                key = viewName
                                value = viewName
                            }
                            putAll(previousMap)
                        }
                    } else {
                        updatedMap = mutableMapOf(viewName to viewName)
                    }
                    put<String> {
                        key = "favouritesArray"
                        value = updatedMap.toJson()
                    }
                    apply()
                    showToast {
                        resId = R.string.added_favourites
                        duration = Toast.LENGTH_SHORT
                    }
                }
            }
        }
    }

    private val drawableIds by lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(30) {
            put {
                key = R.id.Temperature
                value = R.drawable.ic_temperature
            }
            put {
                key = R.id.Area
                value = R.drawable.ic_area
            }
            put {
                key = R.id.Mass
                value = R.drawable.ic_dumb_bell
            }
            put {
                key = R.id.Volume
                value = R.drawable.ic_cylinder
            }
            put {
                key = R.id.Length
                value = R.drawable.ic_ruler
            }
            put {
                key = R.id.Angle
                value = R.drawable.ic_angle1
            }
            put {
                key = R.id.Pressure
                value = R.drawable.ic_blood_pressure1
            }
            put {
                key = R.id.Speed
                value = R.drawable.ic_fast
            }
            put {
                key = R.id.time
                value = R.drawable.ic_circular_clock
            }
            put {
                key = R.id.fuelEconomy
                value = R.drawable.ic_gas_station
            }
            put {
                key = R.id.dataStorage
                value = R.drawable.ic_hard_drive
            }
            put {
                key = R.id.electric_current
                value = R.drawable.ic_lab
            }
            put {
                key = R.id.luminance
                value = R.drawable.ic_light_bulb
            }
            put {
                key = R.id.Illuminance
                value = R.drawable.ic_light_bulb_illuminance
            }
            put {
                key = R.id.energy
                value = R.drawable.ic_science
            }
            put {
                key = R.id.Currency
                value = R.drawable.ic_exchange
            }
            put {
                key = R.id.heatCapacity
                value = R.drawable.ic_flame
            }
            put {
                key = R.id.Angular_Velocity
                value = R.drawable.ic_angular_velocity
            }
            put {
                key = R.id.angularAcceleration
                value = R.drawable.ic_angular_acceleration
            }
            put {
                key = R.id.sound
                value = R.drawable.ic_speaker
            }
            put {
                key = R.id.resistance
                value = R.drawable.ic_resistor
            }
            put {
                key = R.id.radioactivity
                value = R.drawable.ic_radiation
            }
            put {
                key = R.id.resolution
                value = R.drawable.ic_monitor
            }
            put {
                key = R.id.cooking
                value = R.drawable.ic_cooking
            }
            put {
                key = R.id.inductance
                value = R.drawable.ic_inductor
            }
            put {
                key = R.id.flow
                value = R.drawable.ic_sea
            }
            put {
                key = R.id.number_base
                value = R.drawable.ic_ten
            }
        }
    }
}

/*

    fun test(v: View) =
        Toast.makeText(this, "well", Toast.LENGTH_SHORT).show()

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
*/

/*
private var mVelocityTracker: VelocityTracker? = null
private var callAgain = 2
 */
