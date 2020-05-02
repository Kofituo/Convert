package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R
import com.google.android.material.slider.Slider

class DecimalPlaceHolder(view: View, int: Int) : RecyclerView.ViewHolder(view) {

    private var sliderListener: SliderListener? = null

    init {
        view.findViewById<Slider>(R.id.slider)
            .apply {
                addOnChangeListener { slider: Slider, value: Float, fromUser: Boolean ->
                    sliderListener?.onTrackChanged(value)
                }
                val originalColor = ColorStateList.valueOf(int)
                val lighterColor = ColorUtils.blendARGB(int, Color.WHITE, 0.55f)
                trackColorActive = originalColor
                trackColorInactive =
                    ColorStateList.valueOf(ColorUtils.setAlphaComponent(lighterColor, 122))
                thumbColor = originalColor
                tickColorInactive = originalColor
                haloColor = ColorStateList.valueOf(lighterColor)
                tickColorActive = ColorStateList.valueOf(Color.WHITE)
            }
    }

    fun setSliderListener(sliderListener: SliderListener) {
        this.sliderListener = sliderListener
    }

    interface SliderListener {
        fun onTrackChanged(value: Float)
    }
}