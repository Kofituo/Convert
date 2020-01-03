package com.example.unitconverter.subclasses

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.unitconverter.R
import com.google.android.material.card.MaterialCardView

@RequiresApi(Build.VERSION_CODES.Q)
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
            pw.dismiss()
        }

        setOnLongClickListener {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            when {
                Build.VERSION.SDK_INT < 26 -> vibrator.vibrate(100)
                Build.VERSION.SDK_INT < 29 -> {
                    Log.e("ye", "yess")
                    val vibrationEffect =
                        VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)
                }
                else -> {
                    val vibrationEffect =
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                    vibrator.vibrate(vibrationEffect)
                }
            }
            performClick()
        }
    }
}