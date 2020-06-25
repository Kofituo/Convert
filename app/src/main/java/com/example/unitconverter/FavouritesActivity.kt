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
import androidx.recyclerview.widget.SortedList
import androidx.transition.TransitionManager
import com.example.unitconverter.AdditionItems.FavouritesCalledIt
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.builders.buildMutableList
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.subclasses.*
import com.example.unitconverter.subclasses.FavouritesAdapter.Companion.favouritesAdapter
import kotlinx.android.synthetic.main.activity_favourites_empty.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify


@Suppress("PLUGIN_WARNING")
class FavouritesActivity : AppCompatActivity(), FavouritesAdapter.FavouritesItem,
    MotionLayout.TransitionListener, SearchView.OnQueryTextListener {

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
                        originalList = this
                        Log.e("this", "$this")
                        favouritesAdapter = favouritesAdapter {
                            unSortedList = this@apply//viewModel.getFavouritesData()
                            activity = this@FavouritesActivity
                            initialSize = unSortedList.size
                            setFavouritesItemListener(this@FavouritesActivity)
                        }
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
            setDisplayShowTitleEnabled((rootGroup !is MotionLayout))
        }
        window {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    resources.getColor(android.R.color.transparent, null)
                else resources.getColor(android.R.color.transparent)
            setBackgroundDrawable(resources.getDrawable(R.drawable.test, null))
        }
        motionLayout?.apply {
            progress = viewModel.favouritesProgress
            setTransitionListener(this@FavouritesActivity)
        }
        viewModel.favouritesProgress // to reset the value
        setSearchBar()
        initializeRecycler()
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
                        icon = getDrawable(R.drawable.near_white)
                        (actionView as SearchView).apply {
                            maxWidth = Int.MAX_VALUE
                            searchEditText =
                                findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                                    .apply { hint = getString(R.string.search) }
                            setOnQueryTextListener(this@FavouritesActivity)
                        }
                    }
        }
    }

    private lateinit var searchEditText: EditText

    private inline fun recyclerView(block: RecyclerView.() -> Unit) =
        findViewById<RecyclerView>(R.id.view).apply(block)

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    private inline fun intent(block: Intent.() -> Unit) = intent.apply(block)

    private lateinit var searchButton: MenuItem

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourites_menu, menu)
        menu?.apply {
            searchButton = findItem(R.id.search_button)
            ///setOnActionExpandListener(searchStatusListener)
        }
        return true
    }

    private inline val motionLayout
        get() =
            if (rootGroup is MotionLayout) rootGroup as MotionLayout else null

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
                            if (unSortedList.isEmpty())
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
                    //search
                    motionLayout?.apply {
                        if (progress != 1f) {
                            aboutToSearch = true
                            transitionToEnd()
                        } else {
                            showSearchBar()
                            removeConstrains(this)
                        }
                    } ?: showSearchBar()
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
        when {
            initiated -> {
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
            }
            hiddenSearchIcon.isActionViewExpanded -> hiddenSearchIcon.collapseActionView()
            else -> super.onBackPressed()
        }
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
                            val new = unSortedList.map { it.cardName }
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
        viewModel.searchIsExpanded = hiddenSearchIcon.isActionViewExpanded
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
        val createCalled = onCreateCalled
        if (adapterIsInit && createCalled) {
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
        if (createCalled && viewModel.searchIsExpanded) {
            cover_up_toolbar.post {
                circleReveal(R.id.cover_up_toolbar, cover_up_toolbar, true)
                hiddenSearchIcon.expandActionView()
                removeConstrains(motionLayout ?: return@post)
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
        anim.duration = 250
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    if (motionLayout?.viewVisibility(viewId, View.INVISIBLE, view).isNull())
                        view.visibility = View.INVISIBLE
                    motionLayout?.viewVisibility(R.id.app_bar_text, View.VISIBLE, app_bar_text)
                }
                val isCollapsed = !hiddenSearchIcon.isActionViewExpanded
                if (isCollapsed && constraintsHaveChanged)
                    addConstraints(motionLayout ?: return)
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        // make the view visible and start the animation
        if (isShow)
            if (motionLayout?.viewVisibility(viewId, View.VISIBLE, view).isNull())
                view.visibility = View.VISIBLE
        // start the animation
        anim.start()
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }

    override fun onTransitionCompleted(p0: MotionLayout, p1: Int) {
        Log.e("how", "many")
        if (aboutToSearch) {
            showSearchBar()
            //removeConstrains(p0)
            Log.e("com", "pl")
            removeConstrains(p0)
            aboutToSearch = false
        }
        //p0.removeTransitionListener(this) //doesn't work
    }

    private var constraintsHaveChanged by
    ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)

    private fun removeConstrains(motionLayout: MotionLayout) {
        motionLayout.getConstraintSet(R.id.start).apply {
            connect(R.id.toolbar, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(
                R.id.cover_up_toolbar,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            constraintsHaveChanged = true
        }
    }

    private fun addConstraints(motionLayout: MotionLayout) {
        motionLayout.apply {
            getConstraintSet(R.id.start).apply {
                connect(R.id.toolbar, ConstraintSet.TOP, R.id.app_barGuideline, ConstraintSet.TOP)
                connect(
                    R.id.cover_up_toolbar,
                    ConstraintSet.TOP,
                    R.id.app_barGuideline,
                    ConstraintSet.TOP
                )
            }
            constraintsHaveChanged = false
        }
    }

    private fun showSearchBar() {
        circleReveal(R.id.cover_up_toolbar, cover_up_toolbar, true)
        hiddenSearchIcon.expandActionView()
    }

    private var aboutToSearch = false

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    private var searchBegan = false

    private inline val searchStatusListener
        get() = object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                start = System.currentTimeMillis()
                motionLayout?.viewVisibility(R.id.app_bar_text, View.INVISIBLE, app_bar_text)
                Log.e("here", "oop")
                if (::favouritesAdapter.isInitialized) {
                    favouritesAdapter.apply {
                        disableSelection()
                        /*notifyItemRangeChanged(0, itemCount)
                        mSortedList.addAll(originalList)
                        sortedList = MySortedList(mSortedList)
                        searchBegan = true*/
                    }
                }
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                circleReveal(R.id.cover_up_toolbar, cover_up_toolbar, false)
                Log.e("here", "pop")
                if (::favouritesAdapter.isInitialized) {
                    favouritesAdapter.apply {
                        enableSelection()
                        Utils.replaceAll(originalList, mSortedList)
                        useOriginalList()
                    }
                }
                return true
            }
        }

    private var start = 0L
    private var emptyCount = 0

    private val mySortedList by lazy(LazyThreadSafetyMode.NONE) {
        MySortedList(mSortedList)
    }

    private fun initializeRecycler() {
        if (::favouritesAdapter.isInitialized) {
            mSortedList.addAll(originalList)
            favouritesAdapter.apply {
                sortedList = mySortedList
            }
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e("1", "1  $newText  ${newText?.length}")
        if (!::favouritesAdapter.isInitialized || newText.isNull())
            return false
        val filteredList = filter(originalList, newText)
        Utils.replaceAll(filteredList, mSortedList)
        if (newText.isEmpty()) {
            Log.e("2", "2")
            if (emptyCount++ == 0) {
                Log.e("3", "#")
                return false
            }
            favouritesAdapter.useOriginalList()
            Log.e("4", "$")
        } else {
            favouritesAdapter.apply {
                useFilteredList()
                recyclerView.scrollToPosition(0)
            }
            Log.e("end", "${System.currentTimeMillis() - start}")
        }
        return true
    }

    private lateinit var originalList: Collection<FavouritesData>

    private val mSortedList by lazy(LazyThreadSafetyMode.NONE) {
        SortedList(
            FavouritesData::class.java,
            FavouritesSortedList(favouritesAdapter),
            originalList.size
        )
    }

    private fun filter(
        dataSet: Collection<FavouritesData>,
        searchText: CharSequence
    ): Collection<FavouritesData> {
        if (searchText.isBlank()) return dataSet //fast return
        val mainText = searchText.trim()

        return buildMutableList {
            for (i in dataSet) {
                val text = i.cardName!!
                //val meta = i.metadata!!
                if (text.contains(mainText, ignoreCase = true) //||
                //meta.contains(mainText, ignoreCase = true)
                ) add(i)
            }
        }
    }
}