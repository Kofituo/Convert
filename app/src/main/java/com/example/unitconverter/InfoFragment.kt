package com.example.unitconverter

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.unitconverter.miscellaneous.inflate
import com.example.unitconverter.miscellaneous.layoutParams
import com.google.android.material.button.MaterialButton
import kotlin.math.round
import kotlin.math.roundToInt

class InfoFragment : DialogFragment() {

    private lateinit var cancelButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("viewId")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(requireContext(), R.style.sortDialogStyle)
        //view
        LayoutInflater.from(context).inflate {
            resourceId = R.layout.convert_info //root is by default set to null
        }.apply {
            dialog.setContentView(this)
            cancelButton = findViewById(R.id.cancel_button)
            layoutParams<ViewGroup.LayoutParams> {
                width =
                    if (isPortrait) (screenWidth / 17) * 15
                    else round(screenWidth / 1.5).toInt()
                height =
                    if (isPortrait) round(screenHeight * 0.8).toInt()
                    else (screenHeight * 0.9).roundToInt()
            }
        }
        return dialog
    }
}