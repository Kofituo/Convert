package com.example.unitconverter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.unitconverter.AdditionItems.FavouritesCalledIt
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.FavouritesAdapter
import com.example.unitconverter.subclasses.FavouritesAdapter.Companion.favouritesAdapter
import com.example.unitconverter.subclasses.FavouritesData
import kotlinx.android.synthetic.main.activity_favourites_empty.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

class FavouritesActivity : AppCompatActivity(), FavouritesAdapter.FavouritesItem {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var viewModel: ConvertViewModel
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var rootGroup: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootGroup = LayoutInflater.from(this).inflate {
            resourceId = R.layout.activity_favourites_empty
        } as ConstraintLayout
        viewModel = ViewModelProvider(this)[ConvertViewModel::class.java]
        intent {
            if (
                getSerializableExtra("$pkgName.favourites_list")
                    ?.apply {
                        @Suppress("UNCHECKED_CAST")
                        this as ArrayList<FavouritesData>
                        viewModel.favouritesData = this
                        favouritesAdapter = favouritesAdapter {
                            dataSet = viewModel.favouritesData
                            activity = this@FavouritesActivity
                            setFavouritesItemListener(this@FavouritesActivity)
                        }
                        Log.e("serial", "$this")
                        rootGroup
                            .addView(RecyclerView(this@FavouritesActivity)
                                .apply {
                                    id = R.id.view
                                    background = getDrawable(R.drawable.rounded_front)
                                    layoutParams = ConstraintLayout.LayoutParams(0, 0)
                                    layoutParams<ConstraintLayout.LayoutParams> {
                                        setDefaultParam()
                                    }
                                }
                            )

                    }.isNull()
            ) {
                initialiseLayout()
                rootGroup.addView(constraintLayout)
            }
            setContentView(rootGroup)
            if (!::constraintLayout.isInitialized)
                recyclerView = recyclerView {
                    layoutManager = LinearLayoutManager(this@FavouritesActivity)
                    adapter = favouritesAdapter
                    setTopPadding(this@FavouritesActivity, 20)
                }
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

    private fun ConstraintLayout.LayoutParams.setDefaultParam() {
        bottomToBottom = ConstraintSet.PARENT_ID
        endToEnd = ConstraintSet.PARENT_ID
        startToStart = ConstraintSet.PARENT_ID
        topToBottom = R.id.toolbar
    }

    private fun initialiseLayout() {
        constraintLayout = LayoutInflater
            .from(this@FavouritesActivity)
            .inflate {
                resourceId = R.layout.add_to_favourites
            } as ConstraintLayout
        constraintLayout.apply {
            id = R.id.view
            layoutParams = ConstraintLayout.LayoutParams(0, 0)
            layoutParams<ConstraintLayout.LayoutParams> {
                setDefaultParam()
            }
        }
    }

    private inline fun recyclerView(block: RecyclerView.() -> Unit): RecyclerView {
        Log.e("view", "${findViewById<View>(R.id.view)}")
        return findViewById<RecyclerView>(R.id.view).apply(block)
    }

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    private inline fun intent(block: Intent.() -> Unit) = intent.apply(block)

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
                            if (dataSet.isEmpty())
                                rootGroup.apply {
                                    TransitionManager.beginDelayedTransition(this)
                                    removeView(recyclerView)
                                    initialiseLayout()
                                    addView(constraintLayout)
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
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
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
                        //dummy null filled arrays so that only the selected items would call item changed
                        val old = ArrayList<FavouritesData?>(size)
                        val new = ArrayList<FavouritesData?>(size)
                        for (i in 0 until max) {
                            old.add(null)
                            new.add(if (i in keys) FavouritesData() else null)
                        }
                        clear()
                        forceChange = true
                        oldList = old
                        newList = new
                        DiffUtil
                            .calculateDiff(diffUtil)
                            .dispatchUpdatesTo(favouritesAdapter)
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

    @UnstableDefault
    @OptIn(ImplicitReflectionSerializer::class)
    override fun onPause() {
        if (::favouritesAdapter.isInitialized) {
            getSharedPreferences(MainActivity.FAVOURITES, Context.MODE_PRIVATE)
                .edit()
                .apply {
                    val new = favouritesAdapter.dataSet.map { it.cardName }
                    Log.e("new", "$new")
                    put<String> {
                        key = "favouritesArray"
                        @Suppress("UNCHECKED_CAST")
                        value = Json.stringify(new as ArrayList<String>)
                    }
                    apply()
                }
        }
        super.onPause()
    }
}