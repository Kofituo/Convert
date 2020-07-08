package com.otuolabs.unitconverter.subclasses

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.otuolabs.unitconverter.R

class DecimalPlaceHolder(view: View, int: Int) : RecyclerView.ViewHolder(view) {

    private var sliderListener: SliderListener? = null
    private val slider: Slider

    init {
        slider = view.findViewById<Slider>(R.id.slider)
            .apply {
                addOnChangeListener { _: Slider, value: Float, _: Boolean ->
                    sliderListener?.onTrackChanged(value)
                }
                val originalColor = ColorStateList.valueOf(int)
                val lighterColor = ColorUtils.blendARGB(int, Color.WHITE, 0.55f)
                trackActiveTintList = originalColor
                trackInactiveTintList =
                    ColorStateList.valueOf(ColorUtils.setAlphaComponent(lighterColor, 122))
                thumbTintList = originalColor
                tickInactiveTintList = originalColor
                haloTintList = ColorStateList.valueOf(lighterColor)
                tickActiveTintList = ColorStateList.valueOf(Color.WHITE)
            }
    }

    fun setSliderInitialValue(float: Float) {
        slider.value = float
    }

    fun setSliderListener(sliderListener: SliderListener) {
        this.sliderListener = sliderListener
    }

    interface SliderListener {
        fun onTrackChanged(value: Float)
    }
}