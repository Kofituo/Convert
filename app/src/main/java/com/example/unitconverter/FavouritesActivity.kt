package com.example.unitconverter

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.AdditionItems.FavouritesCalledIt
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.miscellaneous.isNull
import com.example.unitconverter.miscellaneous.sharedPreferences
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.FavouritesAdapter
import com.example.unitconverter.subclasses.FavouritesAdapter.Companion.favouritesAdapter
import com.example.unitconverter.subclasses.FavouritesData
import kotlinx.android.synthetic.main.activity_favourites_empty.*

class FavouritesActivity : AppCompatActivity(), FavouritesAdapter.FavouritesItem {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var viewModel: ConvertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ConvertViewModel::class.java]
        intent {
            if (getSerializableExtra("$pkgName.favourites_list")
                    ?.apply {
                        Log.e("called", "serial")
                        @Suppress("UNCHECKED_CAST")
                        this as ArrayList<FavouritesData>
                        viewModel.favouritesData = this
                        favouritesAdapter = favouritesAdapter {
                            dataSet = viewModel.favouritesData
                            activity = this@FavouritesActivity
                            comparator =
                                Comparator { first: FavouritesData, second: FavouritesData ->
                                    first.drawableId!!.compareTo(second.drawableId!!)
                                }
                            setFavouritesItemListener(this@FavouritesActivity)
                        }
                        setContentView(R.layout.activity_favourites)
                        recyclerView = recyclerView {
                            layoutManager = LinearLayoutManager(this@FavouritesActivity)
                            adapter = favouritesAdapter
                        }
                    }.isNull()
            ) setContentView(R.layout.activity_favourites_empty)
        }
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

    private inline fun recyclerView(block: RecyclerView.() -> Unit) =
        findViewById<RecyclerView>(R.id.view).apply(block)

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    private inline fun intent(block: Intent.() -> Unit) = intent.apply(block)

    private inline fun sharedPreferences(block: SharedPreferences.() -> Unit) =
        sharedPreferences(sharedPreferences, block)

    private lateinit var searchButton: MenuItem

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourites_menu, menu)
        menu?.apply {
            searchButton = findItem(R.id.search_button)
        }
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
                    putExtra(TextMessage, "Prefix")
                    putExtra(ViewIdMessage, R.id.prefixes)
                    putExtra(FavouritesCalledIt, true)
                    startActivity(this)
                }
            R.id.search_button -> {
                if (initiated) {
                    //remove items from recycler view
                    favouritesAdapter
                        .apply {
                            if (removeItems()) {
                                forceChange = true
                                endSelection()
                            }
                            if (dataSet.isEmpty()) {
                                setContentView(R.layout.activity_favourites_empty)
                            }
                        }
                    binToSearch()
                    initiated = false
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun binToSearch() = searchButton.apply {
        icon = getDrawable(R.drawable.remove_to_search)
        title = getString(R.string.search)
        (icon as AnimationDrawable).apply {
            setEnterFadeDuration(300)
            setExitFadeDuration(500)
            start()
        }
    }

    override fun startActivity(data: FavouritesData) {
        buildIntent<ConvertActivity> {
            putExtra(TextMessage, data.topText)
            putExtra(ViewIdMessage, data.cardId!!)
            startActivity(this)
        }
    }

    private var initiated = false
    override fun selectionInitiated() {
        (recyclerView.adapter as FavouritesAdapter).forceChange = false
        if (!initiated) {
            searchButton.apply {
                title = getString(R.string.add_to_favourites)
                icon = getDrawable(R.drawable.search_to_remove)
                (icon as AnimationDrawable).apply {
                    setEnterFadeDuration(300)
                    setExitFadeDuration(500)
                    start()
                }
            }
        }
        initiated = true
    }

    override fun sizeIsZero() {
        //end selection
        initiated = false
        binToSearch()
        favouritesAdapter.apply {
            forceChange = true
            endSelection()
        }

    }

    override fun onBackPressed() {
        if (initiated) {
            favouritesAdapter.apply {
                getMap().apply {
                    if (isNotEmpty()) {
                        val max = keys.max()!! + 1
                        val min = keys.min()!!
                        clear()
                        Log.e("called", "$min  $max  $this")
                        forceChange = true
                        notifyItemRangeChanged(min, max)
                        endSelection()
                    }
                }
            }
            initiated = false
            searchButton.apply {
                icon = getDrawable(R.drawable.remove_to_search)
                title = getString(R.string.search)
                (icon as AnimationDrawable).apply {
                    setEnterFadeDuration(300)
                    setExitFadeDuration(500)
                    start()
                }
            }
        } else
            super.onBackPressed()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && ::favouritesAdapter.isInitialized && favouritesAdapter.selectionInProgress) {
            recyclerView.adapter?.apply {
                Log.e("itemCount", "$itemCount")
                notifyItemRangeChanged(0, itemCount)
            }
        }
    }
}
