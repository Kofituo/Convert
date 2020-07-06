package com.example.unitconverter

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.miscellaneous.get
import com.example.unitconverter.miscellaneous.hasValue
import com.example.unitconverter.miscellaneous.put
import com.example.unitconverter.miscellaneous.sharedPreferences
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import kotlinx.serialization.stringify

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        private val autoSave
            get() = sharedPreferences.get<Boolean>("auto_save")
        private var retain = false

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            uiMode = sharedPreferences.get<String?>("theme")
            findPreference<ListPreference>("theme")
                ?.setOnPreferenceChangeListener { _, newValue ->
                    Log.e("new", "$newValue")
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
            findPreference<SwitchPreferenceCompat>("retain")
                ?.setOnPreferenceChangeListener { _, newValue ->
                    retain = newValue as Boolean
                    true
                }
        }

        override fun onPause() {
            super.onPause()
            sharedPreferences.edit {
                put<Boolean> {
                    key = "retain"
                    value = retain
                }
            }
            Log.e("auto", "$autoSave ${activity?.isFinishing}")
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
            if (!retain) {
                if (forNotation == "decimal" && forDecimalPlace == 5) { //highly unlikely
                    prefSharedPreference.edit().clear().apply()
                } else
                    modifyPreferences(
                        prefSharedPreference,
                        forDecimalPlace,
                        forNotation,
                        AdditionItems.originalMap.keys
                    )
            } else {
                //do not modify already modified ones
                val modifiedOnes =
                    prefSharedPreference
                        .get<Set<String>?>("modified_preferences") ?: HashSet(30)
                val listToModify = AdditionItems.originalMap.keys - modifiedOnes
                modifyPreferences(
                    prefSharedPreference,
                    forDecimalPlace,
                    forNotation,
                    listToModify
                )
            }
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