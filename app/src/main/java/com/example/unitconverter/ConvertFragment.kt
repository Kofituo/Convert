package com.example.unitconverter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.unitconverter.subclasses.ConvertViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.round


class ConvertDialog : DialogFragment() {

    lateinit var cancelButton: MaterialButton
    lateinit var searchBar: TextInputLayout
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels

        val dialog = Dialog(context!!, R.style.sortDialogStyle)
        val view = LayoutInflater.from(context).inflate(R.layout.items_list, null)
        dialog.setContentView(view)
        view.apply {
            cancelButton = findViewById(R.id.cancel_button)
            searchBar = findViewById(R.id.search_bar)
            val params = layoutParams
            params.width =
                if (isPortrait) (screenWidth / 7) * 6 else round(screenWidth / 1.6).toInt()
            layoutParams = params
        }
        val model = activity?.run {
            ViewModelProviders.of(this)[ConvertViewModel::class.java]
        }?.apply {
            setDialogColors(randomInt)
        }
        return dialog
    }

    private fun setDialogColors(colorInt: Int) {
        searchBar.boxStrokeColor = colorInt
        cancelButton.apply {
            ColorStateList.valueOf(colorInt).also {
                rippleColor = it
                setTextColor(it)
            }
        }
    }

}