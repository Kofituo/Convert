package com.example.unitconverter.subclasses

import android.content.res.Configuration
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.layoutParams
import kotlin.math.roundToInt

class SearchQuantityHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val imageView: ImageView
    val mainTextView: TextView
    private var quantity: Quantity? = null

    override fun toString(): String {
        return super.toString() + " adap $adapterPosition  ${this.layoutPosition} ${mainTextView.text}"
    }

    init {
        view.apply {
            val screenHeight = resources.displayMetrics.heightPixels
            val isPortrait =
                resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            layoutParams = layoutParams {
                height =
                    if (isPortrait) (screenHeight * 0.075).roundToInt()
                    else (screenHeight * 0.15).roundToInt()
            }
            imageView = findViewById(R.id.image)
            mainTextView = findViewById(R.id.top_text)
            setOnClickListener(this@SearchQuantityHolder)
        }
    }

    fun setQuantityClickListener(quantity: Quantity) {
        this.quantity = quantity
    }

    override fun onClick(v: View?) {
        quantity?.onQuantityClick(adapterPosition)
    }

    interface Quantity {
        fun onQuantityClick(positions: Int)
    }
}