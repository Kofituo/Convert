package com.otuolabs.unitconverter

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.util.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuolabs.unitconverter.AdditionItems.ToolbarColor
import com.otuolabs.unitconverter.AdditionItems.viewsMap
import com.otuolabs.unitconverter.Utils.applyDifference
import com.otuolabs.unitconverter.Utils.name
import com.otuolabs.unitconverter.builders.add
import com.otuolabs.unitconverter.builders.buildIntent
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.builders.put
import com.otuolabs.unitconverter.miscellaneous.*
import com.otuolabs.unitconverter.recyclerViewData.*
import com.otuolabs.unitconverter.subclasses.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import java.io.Serializable
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalTime::class)
class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
        FavouritesAdapter.FavouritesItem, SearchAdapter.UnitItem {

    private var color = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        intent.apply {
            color =
                    if (getBooleanExtra("MAIN_ACTIVITY", false))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            getColor(R.color.front_page)
                        else
                            resources.getColor(R.color.front_page)
                    else intent.getIntExtra(ToolbarColor, -1)
        }
        setColors()
        setRecyclerView()
    }

    private fun setCornerColors() {
        (right_corner.background as LayerDrawable)
                .findDrawableByLayerId(R.id.top_color)
                .setTint(color)
        (left_corner.background as LayerDrawable)
                .findDrawableByLayerId(R.id.top_color)
                .setTint(color)
    }

    private lateinit var hiddenSearch: MenuItem

    private fun setColors() {
        setToolbar()
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

    private fun setToolbar() {
        val screenHeight = resources.displayMetrics.heightPixels
        toolbar.apply {
            setBackgroundColor(color)
            layoutParams = layoutParams {
                height = (screenHeight * 0.09).roundToInt()
            }
        }
        setCornerColors()
    }

    override fun onResume() {
        super.onResume()
        if (!::quantityList.isInitialized)
            getQuantityList().apply {
                //hopefully it solves the no data problem
                showToast {
                    text = "here"
                    duration = Toast.LENGTH_LONG
                }
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
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
                                .hint = getString(R.string.search)
                        setOnQueryTextListener(this@SearchActivity)
                        if (resources.configuration.orientation ==
                                Configuration.ORIENTATION_LANDSCAPE
                        ) {
                            //expand the search bar
                            maxWidth = Int.MAX_VALUE
                        }
                    }
                }
        return true
    }

    private inline val searchStatusListener
        get() = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?) = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                finish()
                return true
            }
        }

    private fun setRecyclerView() {
        SearchAdapter().apply {
            activity = this@SearchActivity
            listData = adapterMap()
            recycler_view.setHasFixedSize(true)
            recycler_view.layoutManager = LinearLayoutManager(this@SearchActivity)
            recycler_view.adapter = this
            itemClickedListener(this@SearchActivity, this@SearchActivity)
        }
    }

    //private fun CharSequence.toLowerCase():String = toString().toLowerCase()

    private lateinit var quantityList: List<FavouritesData>

    private val quantityComparator = Comparator<FavouritesData> { o1, o2 ->
        o1.topText!!.toString().compareTo(o2.topText!!.toString())
    }

    private fun getQuantityList(): SortedArray<FavouritesData> {
        val sortedArray = SortedArray(quantityComparator, viewsMap.size())
        viewsMap.forEach { key: Int, value: View ->
            sortedArray.add {
                value as MyCardView
                val textView = (value.getChildAt(1) as DataTextView)
                FavouritesData.favouritesBuilder {
                    @Suppress("EXPERIMENTAL_API_USAGE")
                    drawableId = MainActivity.drawableIds[key] ?: -1
                    topText = textView.text
                    cardId = key
                    cardName = value.name
                }
            }
        }
        quantityList = sortedArray.clone() as SortedArray<FavouritesData>
        return sortedArray
    }

    private lateinit var unitsList: List<RecyclerDataClass>

    private val unitsComparator = Comparator<RecyclerDataClass> { o1, o2 ->
        val locale = Locale.getDefault()
        o1.topText.toLowerCase(locale).compareTo(o2.topText.toLowerCase(locale))
    }

    private fun getUnitsList(): SortedArray<RecyclerDataClass> {
        val list = TreeSet(unitsComparator)
                .addAll { Temperature(this).getList().addView(R.id.Temperature) }
                .addAll { Area(this).getList().addView(R.id.Area) }
                .addAll { Mass(this).getList().addView(R.id.Mass) }
                .addAll { Volume(this).getList().addView(R.id.Volume) }
                .addAll { Length(this).getList().addView(R.id.Length) }
                .addAll { Angle(this).getList().addView(R.id.Angle) }
                .addAll { Pressure(this).getList().addView(R.id.Pressure) }
                .addAll { Speed(this).getList().addView(R.id.Speed) }
                .addAll { Time(this).getList().addView(R.id.time) }
                .addAll { FuelEconomy(this).getList().addView(R.id.fuelEconomy) }
                .addAll { DataStorage(this).getList().addView(R.id.dataStorage) }
                .addAll { ElectricCurrent(this).getList().addView(R.id.electric_current) }
                .addAll { Luminance(this).getList().addView(R.id.luminance) }
                .addAll { Energy(this).getList().addView(R.id.energy) }
                .addAll { currencyList() }
                .addAll { HeatCapacity(this).getList().addView(R.id.heatCapacity) }
                .addAll { Velocity(this).getList().addView(R.id.Angular_Velocity) }
                .addAll { Acceleration(this).getList().addView(R.id.angularAcceleration) }
                .addAll { Sound(this).getList().addView(R.id.sound) }
                .addAll { Resistance(this).getList().addView(R.id.resistance) }
                .addAll { Radioactivity(this).getList().addView(R.id.radioactivity) }
                .addAll { Force(this).getList().addView(R.id.force) }
                .addAll { Power(this).getList().addView(R.id.power) }
                .addAll { Density(this).getList().addView(R.id.density) }
                .addAll { Flow(this).getList().addView(R.id.flow) }
                .addAll { Inductance(this).getList().addView(R.id.inductance) }
                .addAll { Resolution(this).getList().addView(R.id.resolution) }
                .addAll { NumberBase(this).getList().addView(R.id.number_base) }
        return SortedArray(unitsComparator, list.size).apply {
            addAll { list }
            unitsList = clone() as List<RecyclerDataClass>
        }
    }

    private fun currencyList(): List<RecyclerDataClass> {
        val view = viewsMap[R.id.Currency]
        val sharedPreferences by sharedPreference {
            buildString {
                append(AdditionItems.pkgName)
                append(view.name)
                append(AdditionItems.Author) // to prevent name clashes with the fragment
            }
        }
        sharedPreferences.get<String?>("list_of_currencies") {
            return if (this.isNullOrEmpty()) emptyList()
            else getCurrencyList(this)
        }
        TODO()
    }

    @OptIn(ImplicitReflectionSerializer::class)
    private fun getCurrencyList(string: String): MutableList<RecyclerDataClass> {
        Json.parseMap<String, String>(string).apply {
            val view = viewsMap[R.id.Currency]
            val list = ArrayList<RecyclerDataClass>(size)
            var start = 0
            forEach {
                list.add {
                    RecyclerDataClass(it.key, it.value, start++, view)
                }
            }
            return list
        }
    }

    private lateinit var adapterMap: MutableMap<String, List<Serializable>>
    private fun adapterMap() =
            buildMutableMap<String, List<Serializable>>(2) {
                put {
                    key = getString(R.string._quantity)
                    value = getQuantityList()
                }
                adapterMap = this
            }

    inline fun <T> MutableCollection<T>.addAll(block: () -> Collection<T>) =
            apply { addAll(block()) }

    private fun MutableCollection<RecyclerDataClass>.addView(int: Int) =
            apply {
                val view = viewsMap[int]
                forEach {
                    it.view = view
                }
            }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNotNull()) {
            if (newText.isEmpty()) {
                removeUnitList()
                searchQuantity(newText)
            } else {
                searchQuantity(newText)
                searchUnit(newText)
            }
            recycler_view.scrollToPosition(0)
        }
        return true
    }

    private inline val adapter get() = recycler_view.adapter!!

    private fun removeUnitList() {
        val unit = getString(R.string.unit)
        val quantity = getString(R.string._quantity)
        val unitList = adapterMap[unit]
        val quantityList = adapterMap.getValue(quantity) as ArrayList
        if (unitList.isNotNull()) {
            //remove it
            val size = unitList.size
            //quantity list size
            val quantitySize = quantityList.size + 1 //plus the header
            val endCount = quantitySize + size
            adapterMap.remove(unit)
            adapter.notifyItemRangeRemoved(quantitySize, endCount)
        }
        //fill the quantity list
        adapter.applyDifference<FavouritesData>(1) {
            oldList = quantityList.clone() as List<FavouritesData>
            newList =
                    (this@SearchActivity.quantityList as ArrayList).clone() as List<FavouritesData>
            adapterMap[quantity] = newList!!
        }
    }

    private fun searchQuantity(newText: String) {
        adapter.applyDifference<FavouritesData>(1) {
            val quantity = getString(R.string._quantity)
            val quantityList = adapterMap.getValue(quantity) as ArrayList
            oldList = quantityList as List<FavouritesData>
            //adapter.notifyItemRangeChanged(0, adapter.itemCount)
            @Suppress("EXPERIMENTAL_API_USAGE")
            FavouritesActivity.filter(
                    this@SearchActivity.quantityList,
                    newText,
                    SortedArray(quantityComparator, this@SearchActivity.quantityList.size)
            ).apply {
                adapterMap[quantity] = this
                newList = this
            }
        }
    }

    private fun searchUnit(newText: String) {
        val unit = getString(R.string.unit)
        val unitList = adapterMap[unit]
        val quantity = getString(R.string._quantity)
        val quantityList = adapterMap.getValue(quantity) as MutableList
        if (unitList.isNull()) {
            val previousSize = adapter.itemCount
            //send filtered list instead of sending everything first then filter
            val list =
                    if (::unitsList.isInitialized)
                        (this.unitsList as ArrayList).clone() as List<RecyclerDataClass>
                    else getUnitsList()
            val sortedArray =
                    SortedArray(unitsComparator, list.size) //because it's faster when empty
            SearchTextChangeListener.filter(list, newText, sortedArray)
            //sortedArray.addAll(filteredList)
            adapterMap[unit] = sortedArray
            val sortedListSize = sortedArray.size + 1 // for the header
            adapter.notifyItemRangeInserted(previousSize, sortedListSize)
        } else {
            //it's already there so we can move
            adapter.applyDifference<RecyclerDataClass>(quantityList.size + 2) {
                oldList =
                        (unitList as ArrayList<RecyclerDataClass>).clone() as List<RecyclerDataClass>
                val sortedArray = SortedArray(unitsComparator, unitList.size)
                SearchTextChangeListener
                        .filter(
                                this@SearchActivity.unitsList as MutableList<RecyclerDataClass>,
                                newText,
                                sortedArray
                        )
                adapterMap[unit] = sortedArray
                newList = sortedArray
            }
        }
    }

    override fun startActivity(data: FavouritesData) {
        buildIntent<ConvertActivity> {
            putExtra(AdditionItems.TextMessage, data.topText)
            putExtra(AdditionItems.ViewIdMessage, data.cardId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AdditionItems.SearchActivityCalledIt, true)
            startActivity(this)
        }
    }

    override fun onUnitClick(view: MyCardView, recyclerDataClass: RecyclerDataClass) {
        buildIntent<ConvertActivity> {
            val textViewText = (view.getChildAt(1) as TextView).text
            val unit = recyclerDataClass.bottomText
            val unitIsSpans = unit is Spanned
            putExtra(AdditionItems.TextMessage, textViewText)
            putExtra(AdditionItems.SearchActivityCalledIt, true)
            putExtra(AdditionItems.ViewIdMessage, view.id)
            putExtra(
                    AdditionItems.SearchActivityExtra,
                    recyclerDataClass.also {
                        it.view = null
                        if (unitIsSpans) {
                            it.bottomText = unit.toString()
                            putExtra("IsSpans", true)
                        }
                    }) //to make it serializable
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(this)
            recyclerDataClass.view = view //reset it
            if (unitIsSpans)
                recyclerDataClass.bottomText = unit
        }
    }
}