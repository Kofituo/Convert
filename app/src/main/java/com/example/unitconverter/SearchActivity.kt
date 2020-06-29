package com.example.unitconverter

import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.unitconverter.AdditionItems.ToolbarColor
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private var color = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        color = intent.getIntExtra(ToolbarColor, -1)
        setColors()
        //initializeMenu()
    }

    private lateinit var hiddenSearch: MenuItem

    private fun setColors() {
        toolbar.setBackgroundColor(color)
        window.apply {
            statusBarColor = color
            decorView.apply {
                post {
                    if (Build.VERSION.SDK_INT > 22)
                        systemUiVisibility =
                            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.scale_in)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        hiddenSearch = menu.findItem(R.id.hidden_search)
            .apply {
                setOnActionExpandListener(searchStatusListener)
                icon = ColorDrawable(color)
                expandActionView()
                (actionView as SearchView).apply {
                    // Assumes current activity is the searchable activity
                    setSearchableInfo(searchManager.getSearchableInfo(componentName))
                    findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                        .apply { hint = getString(R.string.search) }
                }
            }
        return true
    }

    private inline val searchStatusListener
        get() = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                finish()
                return true
            }
        }
}
/*private fun re(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            Log.e("this", "${viewGroup[i]}")
            viewGroup[i].setOnClickListener {
                Log.e("this", "$it")
            }
            if (viewGroup[i] is ViewGroup)
                re(viewGroup[i] as ViewGroup)
        }
    }*/