package com.otuolabs.unitconverter.subclasses

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otuolabs.unitconverter.FlattenMap
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.RecyclerDataClass
import com.otuolabs.unitconverter.miscellaneous.inflate
import com.otuolabs.unitconverter.miscellaneous.isNull
import java.io.Serializable

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    SearchQuantityHolder.Quantity {

    lateinit var listData: Map<String, Collection<Serializable>>
    private lateinit var favouritesItem: FavouritesAdapter.FavouritesItem

    private val favData = 200
    override fun getItemViewType(position: Int): Int {
        val type = FlattenMap.getType(listData, position)
        if (type == FlattenMap.CHILD) {
            val data = FlattenMap.getChildData(listData, position)
            if (data is FavouritesData)
                return favData
        }
        return type
    }

    private var inflater: LayoutInflater? = null

    private class Header(view: View) : RecyclerView.ViewHolder(view) {
        val header: TextView = view.findViewById(R.id.group_header)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (inflater.isNull()) inflater = LayoutInflater.from(parent.context)
        val fastInflater = inflater!!
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            FlattenMap.GROUP -> {
                val view = fastInflater.inflate {
                    resourceId = R.layout.search_header
                    root = parent
                }
                viewHolder = Header(view)
            }
            FlattenMap.CHILD -> {
                //for recycler data class
                val view = fastInflater.inflate {
                    resourceId = R.layout.search_units
                    root = parent
                }
                viewHolder =
                    SearchUnitHolder(view)
                        .apply { setUnitClickListener(this@SearchAdapter) }
            }
            favData -> {
                //for the quantities
                val view = fastInflater.inflate {
                    resourceId = R.layout.search_quantity
                    root = parent
                }
                viewHolder =
                    SearchQuantityHolder(view)
                        .apply { setQuantityClickListener(this@SearchAdapter) }
            }
            else -> TODO()
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        var items = 0
        listData.forEach {
            items++
            items += it.value.size
        }
        return items
    }

    lateinit var activity: Activity

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (FlattenMap.getType(listData, position)) {
            FlattenMap.GROUP -> {
                //headers
                val originalGroup =
                    FlattenMap.getGroupPosition(listData, position) ?: error("should'nt be null")
                originalGroup.let {
                    if (listData[listData.keys.elementAt(it)].isNull()) return
                    (holder as Header).header.text = listData.keys.elementAt(it)
                }
            }
            FlattenMap.CHILD -> {
                val data = FlattenMap.getChildData(listData, position)
                when (holder) {
                    is SearchUnitHolder -> {
                        data as RecyclerDataClass
                        holder.apply {
                            units.text = data.bottomText
                            mainTextView.text = data.topText
                        }
                    }
                    is SearchQuantityHolder -> {
                        data as FavouritesData
                        holder.apply {
                            mainTextView.text = data.topText
                            imageView.setImageDrawable(activity.getDrawable(data.drawableId))
                        }
                    }
                    else -> TODO()
                }
            }
        }
    }

    override fun onQuantityClick(position: Int) {
        //it could be -1
        //should'nt be 0 though
        if (position >= 0) {
            val data = FlattenMap.getChildData(listData, position)
            if (data is FavouritesData)
                favouritesItem.startActivity(data)
            else unitClick.onUnitClick((data as RecyclerDataClass).view as MyCardView, data)
        }
    }

    fun itemClickedListener(favouritesItem: FavouritesAdapter.FavouritesItem, unitItem: UnitItem) {
        this.favouritesItem = favouritesItem
        this.unitClick = unitItem
    }

    private lateinit var unitClick: UnitItem

    interface UnitItem {
        fun onUnitClick(view: MyCardView, recyclerDataClass: RecyclerDataClass)
    }
}