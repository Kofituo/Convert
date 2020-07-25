package com.otuolabs.unitconverter

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.get
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.otuolabs.unitconverter.MainActivity.Companion.restoreUiMode
import com.otuolabs.unitconverter.ads.AdsManager
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.miscellaneous.*
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import kotlinx.serialization.stringify
import kotlin.collections.set

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsFragment: SettingsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        restoreUiMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (shouldWatchVideo()) AdsManager.loadRewardedAd()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment().also { settingsFragment = it })
                .commit()
        if (Utils.isPortrait)
            AdsManager.initializeBannerAd(settings_parent)
    }

    private var disabled = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && !disabled && shouldWatchVideo()) {
            settingsFragment.disableUiModeChange()
            disabled = true
        }
    }

    private fun shouldWatchVideo(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            //load the ad if not already loaded
            return globalPreferences.get("adLoadedBefore")
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        AdsManager.bannerAdCallbackListener.onPause()
    }

    override fun onResume() {
        super.onResume()
        AdsManager.bannerAdCallbackListener.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        AdsManager.bannerAdCallbackListener.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat(), View.OnTouchListener, Utils.DefaultConnectivity {
        private var uiMode: Any? = null
        private val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
            context?.globalPreferences ?: globalPreferences // don't like crashes
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            initializeConnections()
            retainInstance = true
        }

        private var disable = false

        fun disableUiModeChange() {
            listView[listPreference.order + 1].setOnTouchListener(this)
            disable = true
        }

        private lateinit var listPreference: ListPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            uiMode = sharedPreferences.get<String?>("theme")
            findPreference<ListPreference>("theme")
                    ?.apply {
                        listPreference = this
                        setOnPreferenceChangeListener { _, newValue ->
                            newValue as CharSequence?
                            val shouldSave: Boolean
                            if (uiMode != newValue && !disable) {
                                shouldSave = true
                                when (newValue) {
                                    "default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                }
                                uiMode = newValue
                            } else shouldSave = false
                            shouldSave && !disable
                        }
                    }
        }

        override fun onPause() {
            super.onPause()
            saveData()
        }

        private fun saveData() {
            //save the data
            val forNotation = //preferences for notation
                    sharedPreferences.get("notation") ?: "decimal"
            val forDecimalPlace =
                    sharedPreferences.get("sliderValue") ?: 5
            val prefSharedPreference by sharedPreferences {
                AdditionItems.pkgName + "for_preferences"
            }
            //quickly clear if retain is false and the rest are the default values
            @Suppress("EXPERIMENTAL_API_USAGE")
            modifyPreferences(
                    prefSharedPreference,
                    forDecimalPlace,
                    forNotation,
                    MainActivity.viewNameToViewData.keys
            )
        }

        @OptIn(ImplicitReflectionSerializer::class)
        private fun modifyPreferences(
                sharedPreferences: SharedPreferences,
                sliderValue: Int,
                notation: String,
                collection: Collection<String>
        ) {
            //for the individual preference put the data
            for (viewName in collection) {
                val activitySharedPreferences by sharedPreferences {
                    buildString {
                        append(AdditionItems.pkgName)
                        append(viewName)
                        append(AdditionItems.Author) // to prevent name clashes with the fragment
                    }
                }
                //modify activity pref
                activitySharedPreferences.edit {
                    put<Int> {
                        key = "sliderValue"
                        value = sliderValue
                    }
                    put<Boolean> {
                        key = "isEngineering"
                        value = getIndex(notation) == 2
                    }
                    activitySharedPreferences
                            .get<String?>("preferences_selections") {
                                val map =
                                        if (this.hasValue()) Json.parseMap<Int, Int>(this)
                                        else buildMutableMap(4) {
                                            put(0, 0)
                                            put(1, 3)
                                            put(2, 7)
                                            put(3, 11)
                                        }
                                map as MutableMap
                                map[0] = getIndex(notation)
                                put<String> {
                                    key = "preferences_selections"
                                    value = Json.stringify(map)
                                }
                            }
                }
                sharedPreferences.edit {
                    put<Float> {
                        key = "$viewName.preference_slider_value"
                        value = sliderValue.toFloat()
                    }
                    sharedPreferences.get<String?>("$viewName.preferences_for_activity") {
                        val map =
                                if (this.hasValue()) Json.parseMap<Int, Int>(this)
                                else
                                    buildMutableMap(4) {
                                        put(0, 0)
                                        put(1, 3)
                                        put(2, 7)
                                        put(3, 11)
                                    }
                        map as MutableMap
                        map[0] = getIndex(notation)
                        put<String> {
                            key = "$viewName.preferences_for_activity"
                            value = Json.stringify(map)
                        }
                    }
                }
            }
        }

        private fun getIndex(string: String) =
                when (string) {
                    "decimal" -> 0
                    "scientific" -> 1
                    "eng" -> 2
                    else -> TODO()
                }

        private var rewardedAdFailedToLoad
                by ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)

        override fun onNetworkAvailable() {
            if (rewardedAdFailedToLoad)
                AdsManager.loadRewardedAd()
        }

        private val simpleOnGestureListener by lazy(LazyThreadSafetyMode.NONE) {
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    AlertDialog
                            .Builder(requireContext(), R.style.sortDialogStyle)
                            .setPositiveButton(R.string.watch) { _, _ ->
                                val shown = AdsManager.showRewardedAd(requireActivity(), rewardedAdCallback)
                                if (!shown) {
                                    rewardedAdFailedToLoad = true
                                    when (AdsManager.rewardedAdError?.code) {
                                        AdRequest.ERROR_CODE_NETWORK_ERROR ->
                                            context?.showToast {
                                                stringId = R.string.no_connection
                                                duration = Toast.LENGTH_LONG
                                            }
                                        else -> context?.showToast {
                                            stringId = R.string.unable_to_get_video
                                            duration = Toast.LENGTH_LONG
                                        }
                                    }
                                }
                            }
                            .setMessage(R.string.watch_to_reward)
                            .show()
                    return true
                }

                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }
            }.let {
                GestureDetectorCompat(context, it)
            }
        }

        private val rewardedAdCallback
            get() = object : RewardedAdCallback() {
                override fun onUserEarnedReward(rewardItem: RewardItem) {
                    disable = false
                    sharedPreferences.edit {
                        put<Boolean> {
                            key = "adLoadedBefore"
                            value = true
                        }
                    }
                }

                override fun onRewardedAdFailedToShow(error: AdError?) {
                    context?.showToast {
                        stringId = R.string.unable_to_get_video
                        duration = Toast.LENGTH_LONG
                    }
                }
            }

        override fun onTouch(p0: View?, motionEvent: MotionEvent): Boolean {
            if (disable)
                simpleOnGestureListener.onTouchEvent(motionEvent)
            return disable
        }
    }
}