package com.example.unitconverter

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
import androidx.core.content.getSystemService
import com.example.unitconverter.AdditionItems.MyEmail
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
import com.example.unitconverter.Utils.daysToMilliSeconds
import com.example.unitconverter.Utils.getNameToViewMap
import com.example.unitconverter.Utils.name
import com.example.unitconverter.Utils.reversed
import com.example.unitconverter.Utils.toJson
import com.example.unitconverter.Utils.values
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.builders.putAll
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.networks.DownloadCallback
import com.example.unitconverter.networks.NetworkFragment
import com.example.unitconverter.networks.Token
import com.example.unitconverter.subclasses.*
import com.example.unitconverter.subclasses.FavouritesData.Companion.favouritesBuilder
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
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
import kotlin.random.Random
import kotlin.time.ExperimentalTime

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

    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        Handler().post { measureAndLog(1234) { initializeInterAds() } }
        super.onCreate(savedInstanceState)
        //use recycler view instead
        measureAndLog(11111) { setContentView(R.layout.front_page_activity) }
        setSupportActionBar(app_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val rect = Rect()
        val isNightMode =
            resources
                ?.configuration
                ?.uiMode
                ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
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
        setCornerColors()
        //initialiseDidYouKnow()
        onCreateCalled = true
        grid setSelectionListener this
        val end = intent.getLongExtra("start", -1)
        Log.e("time", "${end - System.currentTimeMillis()}")
    }


    private fun showInterAd() {
        if (!::interstitialAd.isInitialized) return
        val shouldLoad = Random.nextBoolean()
        Log.e("load", "${interstitialAd.isLoaded}  $shouldLoad  ++")
        if (interstitialAd.isLoaded && shouldLoad)
            interstitialAd.show()
    }

    private var adFailedToLoad
            by ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)

    lateinit var interstitialAd: InterstitialAd
    private fun initializeInterAds() {
        //display the ad after some time
        // probably at on resume
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        interstitialAd.loadAd(ConvertActivity.adRequest)
        interstitialAd.adListener =
            object : AdListener() {
                override fun onAdClosed() {
                    if (!interstitialAd.isLoaded) {
                        //means it was inter that was closed
                        interstitialAd.loadAd(ConvertActivity.adRequest)
                    }
                }

                override fun onAdFailedToLoad(p0: Int) {
                    adFailedToLoad = true
                }
            }
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
            temporalMap.putAll {
                if (descending) mRecentlyUsed
                else mRecentlyUsed.reversed()
            }
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
                    Log.e("complete", "$completedOnes")
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
            } else {
                onSearchRequested()
            }
            true
        }
        R.id.feedback -> sendFeedback(this)

        R.id.upgrade -> onUpgradeClick()

        else -> super.onOptionsItemSelected(item)
    }

    override fun onSearchRequested(): Boolean {
        buildIntent<SearchActivity> {
            putExtra("MAIN_ACTIVITY", true)
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
            showInterAd()
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
        Log.e("unse", "$useDefault")
        if (useDefault)
            searchButton.apply {
                icon = getDrawable(R.drawable.add_to_search)
                title = getString(R.string.search)
                (icon as AnimationDrawable).apply {
                    setEnterFadeDuration(300)
                    setExitFadeDuration(300)
                    start()
                    Handler().postDelayed({
                        icon = getDrawable(R.drawable.ic_magnifying_glass)
                    }, 600)
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

    lateinit var didYouKnowUrls: MutableList<String>
    lateinit var completedOnes: MutableSet<String>

    private var lastUpdate = -1L

    private fun initialiseDidYouKnow() {
        /**
         * Get which did you know are up to date after 15 days
         * */
        sharedPreferences {
            get<Long>("lastCompleteUpdateTime") {
                //null is for first time app opens or when download doesn't finish
                lastUpdate =
                    if (this == -1L || timeIsMoreThanNDays(this, 15)) -1 else this
                Log.e("thi", "$this")
            }
            if (lastUpdate == -1L) {
                /**
                 * Get the ones which weren't updated
                 * */
                completedOnes = get("completedUpdates") ?: HashSet(originalMap.size)
                val onesToUpdate = originalMap.keys subtract completedOnes
                Log.e("one", "$onesToUpdate\n $completedOnes")
                didYouKnowUrls = onesToUpdate.map {
                    appendString {
                        add { "${Token.Repository}/currency_conversions/contents/didYouKnow/$it.json" }
                    }
                } as MutableList<String>
                Log.e("url", "$didYouKnowUrls")
                downloadData(didYouKnowUrls)
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun timeIsMoreThanNDays(previousTime: Long, numberOfDays: Int): Boolean {
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
                        if (adFailedToLoad) {
                            Log.e("fak,", "retr")
                            if (::interstitialAd.isInitialized)
                                launch {
                                    if (!interstitialAd.isLoaded)
                                        interstitialAd.loadAd(ConvertActivity.adRequest)
                                }
                        }
                    }

                    override fun onLost(network: Network) {
                        networkIsAvailable = false
                    }
                }
                registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
            }
        }
    }

    private var i = 0
    override fun updateFromDownload(url: String?, result: String?) {
        Log.e("i 5times", "i ${i++}")
        //if it's null did you know would be blank and inform the user there
        if (url.isNotNull() && result.isNotNull()) {
            val viewName =
                url.substringAfterLast('/')
                    .substringBefore(".json")
            Log.e("result", "$result")
            editPreferences {
                put<String> {
                    key = "did_you_know$viewName"
                    value = result
                    Log.e("vieM", viewName)
                }
                apply()
            }
            Log.e("share", "${sharedPreferences.getString("did_you_know$viewName", "nothing")}")
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
                    Log.e("chec", "delay ${didYouKnowUrls.isNotEmpty()} ")
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
            Log.e("called", "canceled")
            //remove the one i have'nt yet filled
            didYouKnowUrls.remove(url)
        }
        Log.e("url", "$exception")
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
