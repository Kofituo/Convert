package com.otuolabs.unitconverter

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.otuolabs.unitconverter.miscellaneous.inflate
import com.otuolabs.unitconverter.miscellaneous.layoutParams
import kotlin.math.round
import kotlin.math.roundToInt

class Upgrade : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog =
            Dialog(requireContext(), R.style.Theme_MaterialComponents_Light_BottomSheetDialog)
        //view
        LayoutInflater.from(context).inflate {
            resourceId = R.layout.upgrade //root is by default set to null
        }.apply {
            dialog.setContentView(this)
            layoutParams<ViewGroup.LayoutParams> {
                width =
                    if (isPortrait) (screenWidth * 0.83).roundToInt()
                    else round(screenWidth * 0.41).toInt()
                height =
                    if (isPortrait) round(screenHeight * 0.36).toInt()
                    else (screenHeight * 0.79).roundToInt()
            }
        }
        return dialog
    }
}