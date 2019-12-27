package com.example.unitconverter

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.unitconverter.subclasses.dpToInt
import kotlin.math.round

class BottomSheetFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dial = Dialog(context!!, R.style.sortDialogStyle)
        val screenWidth = resources.displayMetrics.widthPixels
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
        dial.setContentView(view)
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.apply {
            width =
                if (isPortrait)
                    screenWidth - 16.dpToInt() else {
                    round(screenWidth / 1.7).toInt()
                }
            bottomMargin = if (isPortrait) 15.dpToInt() else 2.dpToInt()
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        view.layoutParams = params
        dial.setCanceledOnTouchOutside(true)
        dial.window?.setGravity(if (isPortrait) Gravity.BOTTOM else Gravity.CENTER)
        return dial
    }


    private fun Int.dpToInt(): Int {
        return dpToInt(context!!)
    }
}