package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R

class GroupViewHolder(group: View, int: Int) : RecyclerView.ViewHolder(group),
    View.OnClickListener {

    private var groupClickListener: GroupClickListener? = null
    val header: TextView

    private val arrowIcon: ImageView

    init {
        group.apply {
            setOnClickListener(this@GroupViewHolder)
            header = findViewById(R.id.group_header)
            arrowIcon = findViewById(R.id.image)
            arrowIcon.drawable.setTintList(ColorStateList.valueOf(int))
        }
    }

    override fun onClick(v: View?) {
        groupClickListener?.onGroupClick(adapterPosition,this)
    }

    fun setOnGroupClickListener(groupClickListener: GroupClickListener) {
        this.groupClickListener = groupClickListener
    }

    interface GroupClickListener {
        fun onGroupClick(position: Int,holder: GroupViewHolder)
    }

    fun rotateArrow0T180() = rotateFrom0To180(arrowIcon)

    fun rotateArrow180To0()=rotateFrom180To0(arrowIcon)

    private fun rotateFrom0To180(view: View) {
        view.apply {
            RotateAnimation(
                0f,
                180f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = 300
                startAnimation(this)
                fillAfter = true
            }
        }
    }

    private fun rotateFrom180To0(view: View) {
        view.apply {
            RotateAnimation(
                180f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = 250
                startAnimation(this)
                fillAfter = true
            }
        }
    }
}