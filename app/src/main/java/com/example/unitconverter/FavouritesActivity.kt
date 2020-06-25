package com.example.unitconverter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.edit
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
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var rootGroup: ConstraintLayout
    private val adapterIsInit get() = ::favouritesAdapter.isInitialized
    private var onCreateCalled by ResetAfterNGets.resetAfterGet(
        initialValue = false,
        resetValue = false
    )

    private val viewModel by viewModels<ConvertViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootGroup = LayoutInflater.from(this).inflate {
            resourceId = R.layout.activity_favourites_empty
        } as ConstraintLayout
        intent {
            if (
                getSerializableExtra("$pkgName.favourites_list")
                    ?.apply {
                        @Suppress("UNCHECKED_CAST")
                        this as MutableList<FavouritesData>
                        Log.e("this", "$this")
                        favouritesAdapter = favouritesAdapter {
                            dataSet = this@apply//viewModel.getFavouritesData()
                            activity = this@FavouritesActivity
                            initialSize = dataSet.size
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
        if (rootGroup is MotionLayout) {
            (rootGroup as MotionLayout).progress = viewModel.favouritesProgress
        }
        viewModel.favouritesProgress // to reset the value
        setSearchBar()
        onCreateCalled = true
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

    private lateinit var hiddenSearchIcon: MenuItem
    private fun setSearchBar() {
        cover_up_toolbar.apply {
            this as Toolbar
            inflateMenu(R.menu.menu_search)
            hiddenSearchIcon =
                menu.findItem(R.id.hidden_search)
                    .apply {
                        setOnActionExpandListener(searchStatusListener)
                        icon = getDrawable(R.drawable.rounded_front)
                        (actionView as SearchView)
                            .findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                            .hint = getString(R.string.search)
                    }
        }
    }

    private inline fun recyclerView(block: RecyclerView.() -> Unit) =
        findViewById<RecyclerView>(R.id.view).apply(block)

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    private inline fun intent(block: Intent.() -> Unit) = intent.apply(block)

    private lateinit var searchButton: MenuItem

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourites_menu, menu)
        menu?.apply {
            findItem(R.id.search_button).apply {
                searchButton = this
                ///setOnActionExpandListener(searchStatusListener)
            }
        }
        return true
    }

    private inline val motionLayout
        get() =
            if (rootGroup is MotionLayout) rootGroup as MotionLayout else null

    private inline val searchStatusListener
        get() = object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                motionLayout?.viewVisibility(R.id.app_bar_text, View.INVISIBLE, app_bar_text)
                ///app_bar_text.requestLayout()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                circleReveal(R.id.cover_up_toolbar, cover_up_toolbar, false)
                motionLayout?.viewVisibility(R.id.app_bar_text, View.VISIBLE, app_bar_text)
                Log.e("here", "pop")
                ///app_bar_text.requestLayout()
                return true
            }
        }

    private fun MotionLayout.viewVisibility(viewId: Int, visibility: Int, view: View) {
        getConstraintSet(R.id.end)
            ?.setVisibility(viewId, visibility)
        getConstraintSet(R.id.start)
            ?.setVisibility(viewId, visibility)
        view.requestLayout()
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
                    searchButton.actionView = null
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
                } else {
                    /*searchButton.actionView =
                        searchView ?: SearchView(this).apply { searchView = this }*/
                    circleReveal(R.id.cover_up_toolbar, cover_up_toolbar, true)
                    hiddenSearchIcon.expandActionView()
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
                title = getString(R.string.remove)
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

    @UnstableDefault
    @OptIn(ImplicitReflectionSerializer::class)
    override fun onPause() {
        if (adapterIsInit) {
            favouritesAdapter.apply {
                val arrayHasChanged = initialSize!! != currentSize
                if (arrayHasChanged)
                    getSharedPreferences(MainActivity.FAVOURITES, Context.MODE_PRIVATE)
                        .edit {
                            val new = dataSet.map { it.cardName }
                            put<String> {
                                key = "favouritesArray"
                                @Suppress("UNCHECKED_CAST")
                                value = Json.stringify(new as ArrayList<String>)
                            }
                        }
                getMap().apply {
                    if (isNotEmpty())
                        viewModel.selectedFavourites = this
                }
            }
        }
        super.onPause()
    }

    private fun refreshFromMap(map: MutableMap<Int, FavouritesData>) {
        favouritesAdapter.apply {
            map.apply {
                if (isNotEmpty()) {
                    startSelection()
                    val max = keys.max()!! + 1
                    //dummy null filled arrays so that only the selected items would call item changed
                    val old = ArrayList<FavouritesData?>(size)
                    val new = ArrayList<FavouritesData?>(size)
                    for (i in 0 until max) {
                        old.add(null)
                        new.add(if (i in keys) FavouritesData() else null)
                    }
                    //forceChange = true
                    oldList = old
                    newList = new
                    getMap().putAll(map)
                    DiffUtil
                        .calculateDiff(diffUtil)
                        .dispatchUpdatesTo(favouritesAdapter)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (adapterIsInit && onCreateCalled) {
            //Log.e("init", "on create $onCreateCalled")
            favouritesAdapter.apply {
                viewModel.selectedFavourites.apply {
                    if (this.isNotNull()) {
                        //selection occurred
                        //Log.e("finally", "finally  ${viewModel.selectedFavourites}")
                        recyclerView.post {
                            refreshFromMap(this)
                        }
                        viewModel.selectedFavourites = null
                    }
                }
            }
        }
    }


    @Suppress("SameParameterValue")
    @SuppressLint("PrivateResource")
    private fun circleReveal(viewId: Int, view: View, isShow: Boolean) {
        var width = view.width
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
        val centerX = width
        val centerY = view.height / 2
        val anim =
            if (isShow)
                ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0f, width.toFloat())
            else
                ViewAnimationUtils.createCircularReveal(view, centerX, centerY, width.toFloat(), 0f)
        //anim.duration = 220
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow)
                    if (motionLayout?.viewVisibility(viewId, View.INVISIBLE, view).isNull())
                        view.visibility = View.INVISIBLE
            }
        })
        // make the view visible and start the animation
        if (isShow)
            if (motionLayout?.viewVisibility(viewId, View.VISIBLE, view).isNull())
                view.visibility = View.VISIBLE
        // start the animation
        anim.start()
    }
}