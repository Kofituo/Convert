package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.example.unitconverter.R
import com.google.android.material.card.MaterialCardView

class QuickActionCardView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context,attributeSet) {
    init {
        setOnClickListener {
            when (this.id) {
                R.id.firstCont -> {
                    Toast.makeText(context,"Convert clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.selectItems -> {
                    Toast.makeText(context,"Select clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.favourite -> {
                    Toast.makeText(context,"Favourites clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.info -> {
                    Toast.makeText(context,"Info clicked",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}