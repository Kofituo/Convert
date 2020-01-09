package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R
import com.google.android.material.radiobutton.MaterialRadioButton

data class RecyclerDataClass(val quantity: String, val correspondingUnit: String)

class MyAdapter(private val dataSet: MutableList<RecyclerDataClass>, private val colorInt: Int) :

    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    // view holder class
    var lastPosition: Int = -1
    private lateinit var listener: OnRadioButtonsClickListener

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var radioButton: MaterialRadioButton
        var radioTextView: TextView
        init {

            view.apply {
                radioButton = findViewById(R.id.radioButtons)
                radioTextView = findViewById(R.id.radioText)
            }
            radioButton.setOnClickListener {
                lastPosition = adapterPosition
                notifyItemRangeChanged(0, itemCount)
                listener.radioButtonClicked(adapterPosition, radioButton.text, radioTextView.text)
            }
        }
    }

    // rest of the class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view,
                parent,
                false
            )
        view.findViewById<MaterialRadioButton>(R.id.radioButtons).buttonTintList =
            ColorStateList.valueOf(colorInt)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.radioButton.apply {
            text = dataSet[position].quantity
            isChecked = lastPosition == position
        }

        holder.radioTextView.text = dataSet[position].correspondingUnit
    }

    interface OnRadioButtonsClickListener {
        fun radioButtonClicked(position: Int, text: CharSequence, unit: CharSequence) {
        }
    }

    fun setOnRadioButtonsClickListener(listener: OnRadioButtonsClickListener) {
        this.listener = listener
    }
}