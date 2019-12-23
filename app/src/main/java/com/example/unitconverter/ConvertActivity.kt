package com.example.unitconverter

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_convert.*

class ConvertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar)
        window.statusBarColor = Color.GRAY
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.e("well", "$supportActionBar")
        secondEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}
