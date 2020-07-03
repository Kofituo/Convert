package com.example.unitconverter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.unitconverter.AdditionItems.FavouritesCalledIt
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.Utils.containsIgnoreCase
import com.example.unitconverter.builders.addAll
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.subclasses.*
import com.example.unitconverter.subclasses.FavouritesAdapter.Companion.favouritesAdapter
import kotlinx.android.synthetic.main.activity_favourites_empty.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


@Suppress("PLUGIN_WARNING")
class FavouritesActivity : AppCompatActivity(), FavouritesAdapter.FavouritesItem,
    MotionLayout.TransitionListener, SearchView.OnQueryTextListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var rootGroup: ConstraintLayout
    private val adapterIsInit get() = ::favouritesAdapter.isInitialized
    private var onCreateCalled
            by ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)

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
            setDisplayShowTitleEnabled(rootGroup !is MotionLayout)
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
                            findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                                .apply { hint = getString(R.string.search) }
                            setOnQueryTextListener(this@FavouritesActivity)
                        }
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

    @Suppress("EXPERIMENTAL_API_USAGE")
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
            R.id.feedback -> MainActivity.sendFeedback(this)

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
            putExtra(ViewIdMessage, data.cardId)
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
                    getSelectedItemsMap().apply {
                        if (isNotEmpty()) {
                            val keys = IntArray(keys.size)
                            this.keys.forEachIndexed { index, i -> keys[index] = i }
                            Log.e("keys", "$keys")
                            clear()
                            forceChange = true
                            keys.forEach { notifyItemChanged(it) }
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
                val arrayHasChanged = initialSize != currentSize
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
                getSelectedItemsMap().apply {
                    if (isNotEmpty())
                        viewModel.selectedFavourites = this
                }
            }
        }
        viewModel.searchIsExpanded = hiddenSearchIcon.isActionViewExpanded
        super.onPause()
    }

    private fun refreshRecyclerView() {
        favouritesAdapter.also {
            viewModel.selectedFavourites.apply {
                if (!this.isNullOrEmpty()) {
                    //selection occurred
                    it.startSelection()
                    val keys = IntArray(keys.size)
                    this.keys.forEachIndexed { index, i -> keys[index] = i }
                    it.getSelectedItemsMap().putAll(this)
                    keys.forEach { pos: Int ->
                        it.notifyItemChanged(pos)
                    }
                }
                viewModel.selectedFavourites = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val createCalled = onCreateCalled
        if (adapterIsInit && createCalled)
            recyclerView.post { refreshRecyclerView() }

        if (createCalled && viewModel.searchIsExpanded) {
            cover_up_toolbar.post {
                circleReveal(cover_up_toolbar, true)
                hiddenSearchIcon.expandActionView()
                removeConstrains(motionLayout ?: return@post)
            }
        }
    }

    private fun circleReveal(view: View, isShow: Boolean) {
        val viewId = R.id.cover_up_toolbar
        /*var width = view.width
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)*/
        val location = determinePosition()
        /*val centerX = width
        val centerY = view.height / 2*/
        val centerX = location.centerX
        val centerY = location.centerY
        Log.e("x", "${determinePosition()}  $centerX  $centerY")
        val anim =
            if (isShow)
                ViewAnimationUtils
                    .createCircularReveal(view, centerX, centerY, 0f, centerX.toFloat())
            else
                ViewAnimationUtils
                    .createCircularReveal(view, centerX, centerY, centerX.toFloat(), 0f)
        anim.duration = 250
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    if (motionLayout?.viewVisibility(viewId, View.INVISIBLE, view).isNull())
                        view.visibility = View.INVISIBLE
                    motionLayout?.viewVisibility(
                        R.id.app_bar_text,
                        View.VISIBLE,
                        app_bar_text
                    )
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
            connect(
                R.id.toolbar,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
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
                connect(
                    R.id.toolbar,
                    ConstraintSet.TOP,
                    R.id.app_barGuideline,
                    ConstraintSet.TOP
                )
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
        circleReveal(cover_up_toolbar, true)
        hiddenSearchIcon.expandActionView()
    }

    private var aboutToSearch = false

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    private inline val searchStatusListener
        get() = object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                motionLayout?.viewVisibility(R.id.app_bar_text, View.INVISIBLE, app_bar_text)
                Log.e("here", "oop")
                if (::favouritesAdapter.isInitialized)
                    favouritesAdapter.disableSelection()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                circleReveal(cover_up_toolbar, false)
                if (::favouritesAdapter.isInitialized)
                    favouritesAdapter.enableSelection()
                return true
            }
        }

    private var start = 0L
    private var emptyCount = 0

    private lateinit var sortedArray: SortedArray<FavouritesData>

    private inline val String.lowerCase get() = toLowerCase(Locale.getDefault())

    private fun initializeRecycler() {
        if (favouritesIsInit) {
            val comparator = Comparator<FavouritesData> { o1, o2 ->
                o1.cardName!!.lowerCase.compareTo(o2.cardName!!.lowerCase)
            }
            sortedArray = SortedArray(comparator, originalList.size)
            //.apply { addAll(originalList) }
            favouritesAdapter.sortedList = sortedArray
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        start = System.currentTimeMillis()
        Log.e(
            "1",
            "1  $newText  ${newText?.length}  ${hiddenSearchIcon.isActionViewExpanded}"
        )
        if (!::favouritesAdapter.isInitialized || newText.isNull())
            return true
        Log.e("sort", "${(0 until sortedArray.size).map { sortedArray[it].cardName }}")
        if (newText.isEmpty()) {
            //Utils.replaceAll(filteredList, sortedArray)
            Log.e("2", "2")
            if (emptyCount++ == 0) {
                Log.e("3", "#")
                return true
            }
            favouritesAdapter.apply {
                useOriginalList()
                //when changing the data structure used in the recycler view call notify item range change
                notifyItemRangeChanged(0, itemCount)
            }
            Log.e("4", "$")
        } else {
            val filteredList =
                filter<MutableCollection<FavouritesData>>(originalList, newText)
            updateRecyclerView<FavouritesData> {
                oldList = favouritesAdapter.dataSet.toList()
                favouritesAdapter.useFilteredList()
                Utils.replaceAll(filteredList, sortedArray)
                newList = sortedArray
            }
            recyclerView.scrollToPosition(0)
        }
        Log.e("end", "${System.currentTimeMillis() - start}")
        return true
    }

    private lateinit var originalList: List<FavouritesData>

    companion object {
        inline fun <reified T : MutableCollection<FavouritesData>> filter(
            dataSet: List<FavouritesData>,
            searchText: CharSequence,
            returnList: T = ArrayList<RecyclerDataClass>(dataSet.size) as T
        ): T {
            if (searchText.isBlank())
                return returnList.apply { addAll { dataSet } } //fast return
            val mainText = searchText.trim()

            return returnList.apply {
                for (i in dataSet) {
                    val text = i.cardName!!
                    val meta = i.metadata
                    if (text.containsIgnoreCase(mainText)
                        || meta.containsIgnoreCase(mainText)
                    ) add(i)
                }
            }
        }
    }

    private fun determinePosition(): ViewLocation {
        val itemWindowLocation = IntArray(2)
        val menuItemView = findViewById<View>(R.id.search_button)
        menuItemView.getLocationInWindow(itemWindowLocation)
        val toolbarWindowLocation = IntArray(2)
        toolbar.getLocationInWindow(toolbarWindowLocation)
        val itemX = itemWindowLocation[0] - toolbarWindowLocation[0]
        val itemY = itemWindowLocation[1] - toolbarWindowLocation[1]
        val centerX = itemX + menuItemView.width / 2
        val centerY = itemY + menuItemView.height / 2
        return ViewLocation(centerX, centerY)
    }

    private data class ViewLocation(val centerX: Int, val centerY: Int)

    private val favouritesIsInit get() = ::favouritesAdapter.isInitialized

    private inline fun <T> updateRecyclerView(lists: RecyclerViewUpdater.RecyclerLists<T>.() -> Unit) {
        if (favouritesIsInit)
            RecyclerViewUpdater<T>().apply {
                RecyclerViewUpdater.RecyclerLists<T>().apply(lists).also {
                    this.oldList = it.oldList!!
                    this.newList = it.newList!!
                    Log.e("up", "date")
                }
                apply(favouritesAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
            }
        else error("no adapter")
    }
}