package com.otuolabs.unitconverter.subclasses

import android.content.res.ColorStateList
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.otuolabs.unitconverter.R

class ChildViewHolder(childView: View, private val int: Int) : RecyclerView.ViewHolder(childView),
        View.OnClickListener, View.OnTouchListener {

    private var childClickListener: ChildClickListener? = null

    val titleButton: MyRadioButton = childView.findViewById(R.id.decimal_notation)
    private var isChecked = false

    init {
        titleButton.apply {
            buttonTintList = ColorStateList.valueOf(int)
            setOnClickListener(this@ChildViewHolder)
            setOnTouchListener(this@ChildViewHolder)
            setOnCheckedChangeListener { _, isChecked ->
                if (this@ChildViewHolder.isChecked != isChecked)
                    this@ChildViewHolder.isChecked = isChecked
            }
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v?.performClick()
        return isChecked
    }
}