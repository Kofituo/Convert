package com.otuolabs.unitconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.otuolabs.unitconverter.builders.buildIntent
import com.otuolabs.unitconverter.miscellaneous.get

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val start = System.currentTimeMillis()
        restoreUiMode()
        super.onCreate(savedInstanceState)
        @Suppress("EXPERIMENTAL_API_USAGE")
        buildIntent<MainActivity> {
            putExtra("start", start)
            startActivity(this)
            finish()
        }
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
}