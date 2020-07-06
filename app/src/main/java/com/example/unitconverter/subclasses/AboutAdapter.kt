package com.example.unitconverter.subclasses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.miscellaneous.inflate

class AboutAdapter(private val dataSet: List<RecyclerDataClass>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    SearchQuantityHolder.Quantity {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.context.getSystemService<LayoutInflater>()?.inflate {
            resourceId = R.layout.search_units
            root = parent
        } ?: error("no layout inflater")
        return SearchUnitHolder(view)
            .apply { setUnitClickListener(this@AboutAdapter) }
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as SearchUnitHolder
        val data = dataSet[position]
        holder.apply {
            units.text = data.bottomText
            mainTextView.text = data.topText
        }
    }

    override fun onQuantityClick(position: Int) {
        if (position < 0) return
        quantity?.onQuantityClick(position)
    }

    fun setOnItemClickListener(quantity: SearchQuantityHolder.Quantity) {
        this.quantity = quantity
    }

    private var quantity: SearchQuantityHolder.Quantity? = null
}