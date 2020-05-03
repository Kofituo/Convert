package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R

class ChildViewHolder(childView: View, int: Int) : RecyclerView.ViewHolder(childView),
    View.OnClickListener {

    private var childClickListener: ChildClickListener? = null

    val titleButton: MyRadioButton = childView.findViewById(R.id.decimal_notation)

    init {
        titleButton.apply {
            buttonTintList = ColorStateList.valueOf(int)
            setOnClickListener(this@ChildViewHolder)
        }
    }

    override fun onClick(v: View?) {
        childClickListener?.onChildClicked(adapterPosition, titleButton.myId, v as MyRadioButton)
    }

    fun setOnChildClickedListener(childClickListener: ChildClickListener) {
        this.childClickListener = childClickListener
    }

    interface ChildClickListener {
        fun onChildClicked(position: Int, radioId: Int, buttonView: MyRadioButton)
    }
}