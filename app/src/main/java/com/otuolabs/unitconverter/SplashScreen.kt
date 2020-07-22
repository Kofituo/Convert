package com.otuolabs.unitconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.otuolabs.unitconverter.builders.buildIntent

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val start = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        @Suppress("EXPERIMENTAL_API_USAGE")
        buildIntent<MainActivity> {
            putExtra("start", start)
            startActivity(this)
            finish()
        }
    }
}