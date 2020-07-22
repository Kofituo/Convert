package com.otuolabs.unitconverter

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.otuolabs.unitconverter.ads.AdsManager
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.miscellaneous.get
import com.otuolabs.unitconverter.miscellaneous.hasValue
import com.otuolabs.unitconverter.miscellaneous.put
import com.otuolabs.unitconverter.miscellaneous.sharedPreferences
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import kotlinx.serialization.stringify

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        if (Utils.isPortrait)
            AdsManager.initializeBannerAd(settings_parent)
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

    class SettingsFragment : PreferenceFragmentCompat() {
        private var uiMode: Any? = null
        private val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
            PreferenceManager.getDefaultSharedPreferences(context)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            uiMode = sharedPreferences.get<String?>("theme")
            findPreference<ListPreference>("theme")
                    ?.setOnPreferenceChangeListener { _, newValue ->
                        newValue as CharSequence?
                        val shouldSave: Boolean
                        if (uiMode != newValue) {
                            shouldSave = true
                            when (newValue) {
                                "default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            }
                            uiMode = newValue
                        } else shouldSave = false
                        shouldSave
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
            modifyPreferences(
                    prefSharedPreference,
                    forDecimalPlace,
                    forNotation,
                    AdditionItems.originalMap.keys
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
    }
}