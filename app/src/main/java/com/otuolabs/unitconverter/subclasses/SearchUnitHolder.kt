package com.otuolabs.unitconverter.subclasses

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otuolabs.unitconverter.R

class SearchUnitHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val units: TextView
    val mainTextView: TextView
    private var quantity: SearchQuantityHolder.Quantity? = null

    init {
        view.apply {
            units = findViewById(R.id.units)
            mainTextView = findViewById(R.id.top_text)
            setOnClickListener(this@SearchUnitHolder)
        }
    }

    fun setUnitClickListener(quantity: SearchQuantityHolder.Quantity) {
        this.quantity = quantity
    }

    override fun onClick(v: View?) {
        if (adapterPosition < 0) return
        quantity?.onQuantityClick(adapterPosition)
    }
}