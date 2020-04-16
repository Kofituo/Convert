package com.example.unitconverter

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.miscellaneous.sharedPreferences
import kotlinx.android.synthetic.main.activity_favourites_empty.*

class FavouritesActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences {
        }
        setContentView(R.layout.activity_favourites_empty)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        window {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    resources.getColor(android.R.color.transparent, null)
                else resources.getColor(android.R.color.transparent)
            setBackgroundDrawable(resources.getDrawable(R.drawable.test, null))
        }
    }

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    private inline fun sharedPreferences(block: SharedPreferences.() -> Unit) =
        sharedPreferences(sharedPreferences, block)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourites_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.prefixes ->
                buildIntent<ConvertActivity> {
                    putExtra(AdditionItems.TextMessage, "Prefix")
                    putExtra(AdditionItems.ViewIdMessage, R.id.prefixes)
                    startActivity(this)
                }
            else -> super.onOptionsItemSelected(item)
        }
}
