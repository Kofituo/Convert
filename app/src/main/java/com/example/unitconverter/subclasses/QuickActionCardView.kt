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
import com.example.unitconverter.animateFinal
import com.example.unitconverter.app_context
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class QuickActionCardView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context,attributeSet) {


    init {
        setOnClickListener {
            popupWindow.dismiss()
            when (this.id) {
                R.id.firstCont -> {
                    //Toast.makeText(app_context, "Convert clicked", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        animateFinal?.apply {
                            while (isRunning) continue
                        }
                        (card as MyCardView).apply {
                            startActivity()
                            updateArray()
                        }
                    }
                }
                R.id.selectItems -> {
                    Toast.makeText(app_context, "Select clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.favourite -> {
                    Toast.makeText(app_context, "Favourites clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.info -> {
                    Toast.makeText(app_context, "Info clicked", Toast.LENGTH_SHORT).show()
                }
            }
            ///pw.dismiss()
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