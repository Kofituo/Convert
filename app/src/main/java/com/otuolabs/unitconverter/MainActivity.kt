package com.otuolabs.unitconverter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import com.otuolabs.unitconverter.AdditionItems.MyEmail
import com.otuolabs.unitconverter.AdditionItems.TextMessage
import com.otuolabs.unitconverter.AdditionItems.ViewIdMessage
import com.otuolabs.unitconverter.AdditionItems.bugDetected
import com.otuolabs.unitconverter.AdditionItems.endAnimation
import com.otuolabs.unitconverter.AdditionItems.isInitialized
import com.otuolabs.unitconverter.AdditionItems.mRecentlyUsed
import com.otuolabs.unitconverter.AdditionItems.motionHandler
import com.otuolabs.unitconverter.AdditionItems.originalMap
import com.otuolabs.unitconverter.AdditionItems.pkgName
import com.otuolabs.unitconverter.AdditionItems.popupWindow
import com.otuolabs.unitconverter.AdditionItems.statusBarHeight
import com.otuolabs.unitconverter.AdditionItems.viewsMap
import com.otuolabs.unitconverter.Utils.app_bar_bottom
import com.otuolabs.unitconverter.Utils.daysToMilliSeconds
import com.otuolabs.unitconverter.Utils.getNameToViewMap
import com.otuolabs.unitconverter.Utils.name
import com.otuolabs.unitconverter.Utils.reversed
import com.otuolabs.unitconverter.Utils.toJson
import com.otuolabs.unitconverter.Utils.values
import com.otuolabs.unitconverter.ads.AdsManager
import com.otuolabs.unitconverter.builders.buildIntent
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.builders.put
import com.otuolabs.unitconverter.builders.putAll
import com.otuolabs.unitconverter.miscellaneous.*
import com.otuolabs.unitconverter.networks.DownloadCallback
import com.otuolabs.unitconverter.networks.NetworkFragment
import com.otuolabs.unitconverter.networks.Token
import com.otuolabs.unitconverter.subclasses.*
import com.otuolabs.unitconverter.subclasses.FavouritesData.Companion.favouritesBuilder
import kotlinx.android.synthetic.main.front_page_activity.*
import kotlinx.android.synthetic.main.scroll.*
import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import java.io.FileNotFoundException
import java.io.Serializable
import java.util.*
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashMap
import kotlin.collections.set

//change manifest setting to backup allow true
@UnstableDefault
@OptIn(ImplicitReflectionSerializer::class)
class MainActivity : AppCompatActivity(), BottomSheetFragment.SortDialogInterface,
        GridConstraintLayout.Selection, CoroutineScope by MainScope(), DownloadCallback<String> {

    private val downTime get() = SystemClock.uptimeMillis()
    private val eventTime get() = SystemClock.uptimeMillis() + 10
    private val xPoint = 0f
    private val yPoint = 0f
    private val metaState = 0
    private val motionEventDown
        get() = MotionEvent
                .obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xPoint, yPoint, metaState)
    private val motionEventUp: MotionEvent
        get() = MotionEvent
                .obtain(downTime, eventTime, MotionEvent.ACTION_UP, xPoint, yPoint, metaState)

    private val motionEventMove: MotionEvent
        get() = MotionEvent
                .obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xPoint, yPoint, metaState)

    private val sharedPreferences by defaultPreferences()

    private lateinit var mSelectedOrderArray: Map<String, Int>

    private val waitingArrayDeque by lazy(LazyThreadSafetyMode.NONE) {
        ArrayDeque<String>(30)
    }

    private val isNightMode
        get() =
            resources
                    ?.configuration
                    ?.uiMode
                    ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreUiMode()
        //use recycler view instead
        setContentView(R.layout.front_page_activity)
        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val rect = Rect()
        window?.decorView?.apply {
            post {
                getWindowVisibleDisplayFrame(rect)
                statusBarHeight = rect.top
                if (Build.VERSION.SDK_INT > 22)
                    systemUiVisibility =
                            if (isNightMode) systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            else systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        savedInstanceState?.putFloat("motion_progress", 1f)
        motionLayoutTuning(savedInstanceState)
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
                }
                get<String?>("mSelectedOrder") {
                    mSelectedOrderArray =
                            if (this.hasValue()) {
                                //sorting occurred
                                //so we have to keep it like that
                                val selectedOrder =
                                        Json.parse(DeserializeStringIntMap, this)
                                grid.sort(selectedOrder)
                                selectedOrder
                            } else mapOf()
                }
                descending = get("descending")
                recentlyUsedBool = get("recentlyUsedBoolean")
                apply()
            }
        }
        setCornerColors()
        initialiseDidYouKnow()
        onCreateCalled = true
        AdsManager.initializeInterstitialAd()
        grid setSelectionListener this
    }

    private fun setCornerColors() {
        val color =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    getColor(R.color.front_page)
                else
                    resources.getColor(R.color.front_page)
        (right_corner.background as LayerDrawable)
                .findDrawableByLayerId(R.id.top_color)
                .setTint(color)
        (left_corner.background as LayerDrawable)
                .findDrawableByLayerId(R.id.top_color)
                .setTint(color)
    }

    private fun motionLayoutTuning(savedInstanceState: Bundle?) {
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
                            launch {
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
            progress = savedInstanceState?.getFloat("motion_progress") ?: 0f
        }
    }

    private inline fun motion(block: MyMotionLayout.() -> Unit) =
            motion?.apply(block)

    override fun selection(firstSelection: Int, secondSelection: Int) {
        recentlyUsedBool = false /// reset the value
        if (firstSelection == -1) {
            // use default
            grid.sort(originalMap)
            mSelectedOrderArray = originalMap
            return
        }
        val temporalMap = LinkedHashMap<String, Int>(30)
        descending = secondSelection == R.id.descending
        if (firstSelection == R.id.titleButton) {
            //sort by title
            viewsMap.values {
                sortWith(
                        if (descending)
                            compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }
                        else compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                )
                for (i in this)
                    temporalMap[i.name] = i.id
            }
        } else {
            recentlyUsedBool = true
            temporalMap.putAll {
                if (descending) mRecentlyUsed
                else mRecentlyUsed.reversed()
            }
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
            waitingArrayDeque.clear()
            favouritesCalled = false
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
            if (::completedOnes.isInitialized)
                put<Set<String>> {
                    key = "completedUpdates"
                    value = completedOnes
                }
            if (waitingArrayDeque.isNotEmpty()) {
                //Log.e("fav", "$favouritesCalled")
                if (!favouritesCalled)
                    sharedPreferences(favouritesPreferences) {
                        get<String?>("favouritesArray") {
                            if (this.hasValue()) {
                                val previous = Json.parse(DeserializeStringDeque, this)
                                //Log.e("prev", "$previous")
                                waitingArrayDeque.offerLast(previous)
                            }
                        }
                        favouritesCalled = !favouritesCalled
                    }
                editPreferences(favouritesPreferences) {
                    put<String> {
                        //Log.e("pause", "$waitingArrayDeque")
                        key = "favouritesArray"
                        value = Json.stringify(waitingArrayDeque.toMutableList())
                    }
                    apply()
                }
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

    /*fun allViews(viewGroup: ViewGroup, int: Int = 0) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup[i]
            Log.e("view ${if (int == 0) i else "$int.$i"}", "$view")
            if (view is ViewGroup)
                allViews(view, i)
        }
    }*/

    override fun onBackPressed() {
        grid {
            selectionInProgress {
                return endSelection()
            }
            super.onBackPressed()
        }
    }

    private val favouritesPreferences
        get() = getSharedPreferences(FAVOURITES, Context.MODE_PRIVATE)

    private var favouritesCalled = false //to prevent double getting of shared preference

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
        R.id.favourite -> onFavouritesClicked()
        R.id.search_button -> {
            if (!useDefault) {
                // store selected item
                storeSelectedItems()
            } else onSearchRequested()

            true
        }
        R.id.feedback -> sendFeedback(this)

        R.id.upgrade -> onUpgradeClick()

        R.id.settings -> buildIntent<SettingsActivity> { startActivity(this) }


        else -> super.onOptionsItemSelected(item)
    }

    override fun onSearchRequested(): Boolean {
        buildIntent<SearchActivity> {
            putExtra("MAIN_ACTIVITY", true)
            AdsManager.showInterstitialAd()
            startActivity(this)
        }
        return true
    }

    private fun onUpgradeClick(): Boolean {
        Upgrade().show(supportFragmentManager, "UPGRADE")
        return true
    }

    private fun storeSelectedItems() {
        grid {
            val map = getArray()
            if (map.isEmpty()) {
                endSelection()
                return
            }
            waitingArrayDeque.offerFirst(map)
            grid {
                endSelection()
            }
            showToast {
                stringId = R.string.added_favourites
                duration = Toast.LENGTH_LONG
            }
            map.clear() //for efficiency
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        app_bar_bottom = app_bar.bottom - app_bar.top
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isInitialized) popupWindow.dismiss()
        cancel()
    }

    private var useDefault = true

    private fun onFavouritesClicked() =
            buildIntent<FavouritesActivity> {
                AdsManager.showInterstitialAd()
                sharedPreferences(favouritesPreferences) {
                    get<String?>("favouritesArray") {
                        favouritesCalled = true
                        if (this.hasValue()) {
                            //Log.e("has ", "value  $waitingArrayDeque")
                            //already sorted in reverse order
                            val deque = Json.parse(DeserializeStringDeque, this)
                            waitingArrayDeque.offerLast(deque)
                                    .apply {
                                        if (isEmpty()) {
                                            startActivity(this@buildIntent)
                                            return true
                                        }
                                        //Log.e("called Linked", "$this  \n$deque $waitingArrayDeque")
                                    }
                        }
                    }
                }
                if (waitingArrayDeque.isNotEmpty()) {
                    //send the list to the activity
                    val favouritesList: List<FavouritesData?>
                    getNameToViewMap().apply {
                        favouritesList = waitingArrayDeque.map {
                            val view = this[it] //shouldn't be null though
                            view?.run {
                                this as MyCardView
                                val textView = (this@run.getChildAt(1) as DataTextView)
                                favouritesBuilder {
                                    drawableId = drawableIds[this@run.id] ?: -1
                                    topText = textView.text
                                    metadata = textView.metadata
                                    cardId = this@run.id
                                    cardName = this@run.name
                                }
                            }
                        }
                    }
                    putExtra("$pkgName.favourites_list", favouritesList as Serializable)
                }
                startActivity(this)
            }

    override fun changeSearchButton(useDefault: Boolean) {
        this.useDefault = useDefault
        if (useDefault)
            searchButton.apply {
                icon = getDrawable(R.drawable.add_to_search)
                title = getString(R.string.search)
                (icon as AnimationDrawable).apply {
                    setEnterFadeDuration(300)
                    setExitFadeDuration(300)
                    start()
                    launch {
                        delay(600)
                        icon = getDrawable(R.drawable.ic_magnifying_glass)
                    }
                }
            } else searchButton.apply {
            title = getString(R.string.add_to_favourites)
            icon = getDrawable(R.drawable.search_to_add)
            (icon as AnimationDrawable).apply {
                setEnterFadeDuration(300)
                setExitFadeDuration(300)
                start()
            }
        }
    }

    override fun addOneToFavourites(viewName: String) {
        //Log.e("array one before", "$waitingArrayDeque")
        waitingArrayDeque.apply {
            remove(viewName)
            offerFirst(viewName)
            //Log.e("array one", "$this")
        }
        showToast {
            stringId = R.string.added_favourites
            duration = Toast.LENGTH_SHORT
        }
    }

    override fun convertInfo(viewId: Int, viewName: String) {
        InfoFragment().apply {
            arguments = Bundle(2).apply {
                putInt("viewId", viewId)
                putString("viewName", viewName)
            }
            show(supportFragmentManager, "CONVERT_INFO")
        }
    }

    private lateinit var didYouKnowUrls: MutableList<String>
    private lateinit var completedOnes: MutableSet<String>

    private var lastUpdate = -1L

    private fun initialiseDidYouKnow() {
        /**
         * Get which did you know are up to date after 15 days
         * */
        sharedPreferences {
            get<Long>("lastCompleteUpdateTime") {
                //null is for first time app opens or when download doesn't finish
                lastUpdate =
                        if (this == -1L || timeIsMoreThanNDays(this)) -1 else this
            }
            if (lastUpdate == -1L) {
                /**
                 * Get the ones which weren't updated
                 * */
                completedOnes =
                        get<Set<String>?>("completedUpdates")?.toMutableSet()
                                ?: HashSet(originalMap.size)
                val onesToUpdate = originalMap.keys subtract completedOnes
                didYouKnowUrls = onesToUpdate.map {
                    appendString {
                        add { "${Token.Repository}/currency_conversions/contents/didYouKnow/$it.json" }
                    }
                } as MutableList<String>
                downloadData(didYouKnowUrls)
            }
        }
    }

    private fun timeIsMoreThanNDays(previousTime: Long, numberOfDays: Int = 15): Boolean {
        val difference = System.currentTimeMillis() - previousTime
        return difference > daysToMilliSeconds(numberOfDays)
    }

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private var networkIsAvailable = false

    private lateinit var networkFragment: NetworkFragment

    //private var retry = false

    private fun downloadData(list: List<String>) {
        setNetworkCallback()
        networkFragment =
                NetworkFragment.createFragment(supportFragmentManager) { urls = list }
        networkFragment.startDownload()
    }

    private fun setNetworkCallback() {
        if (!::networkCallback.isInitialized) {
            getSystemService<ConnectivityManager>()?.apply {
                networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        networkIsAvailable = true
                        if (didYouKnowUrls.isNotEmpty() && ::networkFragment.isInitialized)
                            networkFragment.startDownload()
                    }

                    override fun onLost(network: Network) {
                        networkIsAvailable = false
                    }
                }
                registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
            }
        }
    }

    override fun updateFromDownload(url: String?, result: String?) {
        //if it's null did you know would be blank and inform the user there
        if (url.isNotNull() && result.isNotNull()) {
            val viewName =
                    url.substringAfterLast('/')
                            .substringBefore(".json")
            editPreferences {
                put<String> {
                    key = "did_you_know$viewName"
                    value = result
                }
                apply()
            }
            didYouKnowUrls.remove(url)
            completedOnes.add(viewName)
        }
    }

    override fun networkAvailable(): Boolean = networkIsAvailable

    override fun finishDownloading() {
        if (completedOnes.size == originalMap.size) {
            //means all has been updated
            editPreferences {
                put<Set<String>> {
                    key = "completedUpdates"
                    value = null //so that next time it would start at fresh
                }
                put<Long> {
                    key = "lastCompleteUpdateTime"
                    value = System.currentTimeMillis()
                }
                apply()
            }
            networkFragment.cancelDownload()
        } else
            if (networkIsAvailable)
                launch {
                    //wait for sometime before we retry
                    delay(4500)
                    if (didYouKnowUrls.isNotEmpty())
                        networkFragment.startDownload()
                    else networkFragment.cancelDownload()
                }
    }

    override fun passException(url: String?, exception: Exception) {
        //retry = true
        if (exception is FileNotFoundException) {
            //networkFragment.cancelDownload()
            //remove the one i have'nt yet filled
            didYouKnowUrls.remove(url)
        }
        Log.e("excep", "$url $exception")
    }

    private fun restoreUiMode() {
        //the ad may prevent the ui from updating
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            val mode =
                    when (get<String?>("theme")) {
                        "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                        "default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        "light" -> AppCompatDelegate.MODE_NIGHT_NO
                        else -> return
                    }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    companion object {
        const val FAVOURITES = "$pkgName.favourites_activity_list"
        val drawableIds by lazy(LazyThreadSafetyMode.NONE) {
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
                    key = R.id.force
                    value = R.drawable.force
                }
                put {
                    key = R.id.power
                    value = R.drawable.ic_fuse_box
                }
                put {
                    key = R.id.density
                    value = R.drawable.ic_ice
                }
                put {
                    key = R.id.flow
                    value = R.drawable.ic_sea
                }
                put {
                    key = R.id.inductance
                    value = R.drawable.ic_inductor
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
                    key = R.id.number_base
                    value = R.drawable.ic_ten
                }
            }
        }

        fun sendFeedback(context: Context) =
                Intent(Intent.ACTION_SENDTO).run {
                    data = Uri.parse("mailto:") //for only email apps
                    putExtra(Intent.EXTRA_EMAIL, MyEmail)
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                    resolveActivity(context.packageManager)?.let {
                        context.startActivity(this)
                    } ?: context.showToast {
                        stringId = R.string.no_email_app
                        duration = Toast.LENGTH_SHORT
                    }
                    true
                }
    }
}