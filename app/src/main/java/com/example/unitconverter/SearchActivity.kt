package com.example.unitconverter

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.util.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unitconverter.AdditionItems.ToolbarColor
import com.example.unitconverter.AdditionItems.viewsMap
import com.example.unitconverter.Utils.name
import com.example.unitconverter.Utils.replaceAll
import com.example.unitconverter.builders.add
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.recyclerViewData.*
import com.example.unitconverter.subclasses.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import java.io.Serializable
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

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
        //initializeMenu()
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
        Log.e("color", "$color")
        toolbar.apply {
            setBackgroundColor(color)
            layoutParams = layoutParams {
                height = (screenHeight * 0.09).roundToInt()
            }
        }
        (right_corner.background as LayerDrawable)
            .findDrawableByLayerId(R.id.top_color)
            .setTint(color)
        (left_corner.background as LayerDrawable)
            .findDrawableByLayerId(R.id.top_color)
            .setTint(color)

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
                    setOnQueryTextListener(this@SearchActivity)
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

    private fun setRecyclerView() {
        SearchAdapter().apply {
            activity = this@SearchActivity
            recycler_view.adapter = this
            listData = adapterMap()
            recycler_view.setHasFixedSize(true)
            recycler_view.layoutManager = LinearLayoutManager(this@SearchActivity)
            itemClickedListener(this@SearchActivity, this@SearchActivity)
        }
    }

    //private fun CharSequence.toLowerCase():String = toString().toLowerCase()

    private lateinit var quantityList: List<FavouritesData>

    private fun getQuantityList(): SortedArray<FavouritesData> {

        val quantityComparator = Comparator<FavouritesData> { o1, o2 ->
            o1.topText!!.toString().compareTo(o2.topText!!.toString())
        }
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
        Log.e("ar", "${sortedArray.size}")
        quantityList = sortedArray.clone() as SortedArray<FavouritesData>
        return sortedArray
    }

    private lateinit var unitsList: List<RecyclerDataClass>

    private val unitsComparator = Comparator<RecyclerDataClass> { o1, o2 ->
        val locale = Locale.getDefault()
        o1.quantity.toLowerCase(locale).compareTo(o2.quantity.toLowerCase(locale))
    }

    private fun getUnitsList(): SortedArray<RecyclerDataClass> {
        val c = this
        val list: MutableCollection<RecyclerDataClass>
        val p = measureTime {
            list =
                Temperature(this).getList().addView(R.id.Temperature)
                    .addAll { Angle(this).getList().addView(R.id.Angle) }
                    .addAll { Area(this).getList().addView(R.id.Area) }
                    .addAll { Mass(this).getList().addView(R.id.Mass) }
                    .addAll { Volume(c).getList().addView(R.id.Volume) }
                    .addAll { Length(c).getList().addView(R.id.Length) }
                    .addAll { Angle(c).getList().addView(R.id.Angle) }
                    .addAll { Pressure(c).getList().addView(R.id.Pressure) }
                    .addAll { Speed(c).getList().addView(R.id.Speed) }
                    .addAll { Time(c).getList().addView(R.id.time) }
                    .addAll { FuelEconomy(c).getList().addView(R.id.fuelEconomy) }
                    .addAll { DataStorage(c).getList().addView(R.id.dataStorage) }
                    .addAll { ElectricCurrent(c).getList().addView(R.id.electric_current) }
                    .addAll { Luminance(c).getList().addView(R.id.luminance) }
                    .addAll { Energy(c).getList().addView(R.id.energy) }
                    .addAll { currencyList() }
                    .addAll { HeatCapacity(c).getList().addView(R.id.heatCapacity) }
                    .addAll { Velocity(c).getList().addView(R.id.Angular_Velocity) }
                    .addAll { Acceleration(c).getList().addView(R.id.angularAcceleration) }
        }
        return SortedArray(unitsComparator, list.size).apply {
            Log.e("mea", "${measureTime { unSaveAdd(list) }} p $p ")
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
            /*put {
                key = getString(R.string.unit)
                value = emptyList()
            }*/
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
                val t1 = measureTime {
                    searchQuantity(newText)
                }
                val t2 = measureTime {
                    searchUnit(newText)
                }
                Log.e("time", "$t1  t2 $t2")
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
        applyDifference<FavouritesData>(1) {
            oldList = quantityList.clone() as List<FavouritesData>
            replaceAll(this@SearchActivity.quantityList, quantityList)
            newList = quantityList as List<FavouritesData>
        }
    }

    private fun searchQuantity(newText: String) {
        applyDifference<FavouritesData>(1) {
            val quantity = getString(R.string._quantity)
            val quantityList = adapterMap.getValue(quantity) as ArrayList
            val filteredList =
                FavouritesActivity.filter(this@SearchActivity.quantityList, newText)
            oldList = quantityList.clone() as List<FavouritesData>
            //adapter.notifyItemRangeChanged(0, adapter.itemCount)
            replaceAll(filteredList, quantityList)
            newList = quantityList as List<FavouritesData>
            Log.e("olSearch d", "${oldList!!.map { it.cardName }}")
            Log.e("new", "${newList!!.map { it.cardName }}")
        }
    }

    private fun searchUnit(newText: String) {
        val unit = getString(R.string.unit)
        val unitList = adapterMap[unit]
        val quantity = getString(R.string._quantity)
        val quantityList = adapterMap.getValue(quantity) as MutableList
        Log.e("is null", "${unitList.isNull()}")
        if (unitList.isNull()) {
            val previousSize = adapter.itemCount
            //send filtered list instead of sending everything first then filter
            val list =
                if (this::unitsList.isInitialized) {
                    (this.unitsList as ArrayList).clone() as MutableList<RecyclerDataClass>
                } else getUnitsList()
            val filteredList =
                SearchTextChangeListener.filter(list, newText)
            replaceAll(filteredList, list)
            adapterMap[unit] = list
            val listSize = list.size + 1 // for the header
            Log.e("items insert", "$previousSize  $listSize  total ${previousSize + listSize} ")
            adapter.notifyItemRangeInserted(previousSize, listSize)

        } else {
            //it's already there so we can move
            Log.e("unit", "unit")
            val filteredList =
                SearchTextChangeListener
                    .filter(this.unitsList as MutableList<RecyclerDataClass>, newText)
            Log.e("qu", "${quantityList.size}")
            applyDifference<RecyclerDataClass>(quantityList.size + 2) {
                oldList =
                    (unitList as ArrayList<RecyclerDataClass>).clone() as List<RecyclerDataClass>
                replaceAll(filteredList, unitList)
                newList = unitList//.toList()
                Log.e("old", "${oldList!!.map { it.quantity }}")
                Log.e("new", "${newList!!.map { it.quantity }}")
                Log.e("fil", "${filteredList.map { it.quantity }}")
            }
        }
    }

    /**
     *
     * */
    private inline fun <T> applyDifference(
        listStartIndex: Int,
        lists: RecyclerViewUpdater.RecyclerLists<T>.() -> Unit
    ) {
        RecyclerViewUpdater.RecyclerLists<T>().apply(lists).apply {
            val oldList = oldList!!
            val newList = newList!!
            val newSize = newList.size
            val oldSize = oldList.size
            val sizeDifference = newSize - oldSize
            val longerList: List<T>
            val shorterList = // use the smaller list for the iteration
                if (newSize < oldSize) {
                    longerList = oldList
                    newList
                } else {
                    longerList = newList
                    oldList
                }
            Log.e("itemCo", "${adapter.itemCount}")
            //these methods update those positions so i don't have to iterate through the longest list
            val time = measureTimeMillis {
                if (sizeDifference < 0) {
                    Log.e("new", "$listStartIndex  $newSize  $sizeDifference")
                    adapter
                        .notifyItemRangeRemoved(
                            listStartIndex + newSize,
                            sizeDifference.absoluteValue
                        )
                } else if (sizeDifference > 0) {
                    Log.e("insert", "$oldSize  $listStartIndex $sizeDifference")
                    adapter.notifyItemRangeInserted(listStartIndex + oldSize, sizeDifference)
                }
                //update affected ones
                for (i in shorterList.indices) {
                    if (shorterList[i] != longerList[i]) {
                        adapter.notifyItemChanged(listStartIndex + i)
                    }
                }
            }
            Log.e("itQA", "${adapter.itemCount}  $time")
        }
    }

    override fun startActivity(data: FavouritesData) {
        buildIntent<ConvertActivity> {
            putExtra(AdditionItems.TextMessage, data.topText)
            putExtra(AdditionItems.ViewIdMessage, data.cardId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(this)
        }
    }

    override fun onUnitClick(view: MyCardView, recyclerDataClass: RecyclerDataClass) {
        buildIntent<ConvertActivity> {
            val textViewText = (view.getChildAt(1) as TextView).text
            putExtra(AdditionItems.TextMessage, textViewText)
            putExtra(AdditionItems.ViewIdMessage, view.id)
            putExtra(
                AdditionItems.SearchActivityExtra,
                recyclerDataClass.also { it.view = null }) //to make it serializable
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(this)
            recyclerDataClass.view = view //reset it
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

/*private fun searchQuantity(newText: String) {
    updateRecyclerView(quantityComparator) {
        val quantity = getString(R.string._quantity)
        val quantityList = adapterMap.getValue(quantity) as MutableList
        val filteredList =
            FavouritesActivity.filter(this@SearchActivity.quantityList, newText)
        oldList = quantityList.toList() as List<FavouritesData>
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        replaceAll(filteredList, quantityList)
        newList = quantityList as List<FavouritesData>
        Log.e("old", "${oldList!!.map { it.cardName }}")
        Log.e("new", "${newList!!.map { it.cardName }}")
    }
}*/
//served me well
/*
                 * simulating remove operations
                 */
/*val copy = oldList.toMutableList()

for ((index, i) in longerList.withIndex()) {
val inOtherList = longerList.indexOf(i)
val inListToIterate = shorterList.indexOf(i)
val iInListToIterate = inListToIterate > -1
if (iInListToIterate) {
if (inOtherList != inListToIterate) {
Log.e("in", "$index  $inListToIterate   $inOtherList")
adapter.notifyItemChanged(inListToIterate + listStartIndex)
}
} else {
val copyIndex = copy.indexOf(i)
Log.e("else", "else $index  ${index.plus(listStartIndex)}  $copyIndex")
require(copyIndex != -1)
adapter.notifyItemRemoved(copyIndex + listStartIndex)
copy.removeAt(copyIndex)
}
}
return*/