package com.example.unitconverter

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.miscellaneous.inflate
import com.example.unitconverter.miscellaneous.layoutParams
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.MyExpandableList
import com.example.unitconverter.subclasses.PreferencesAdapter
import java.util.*
import kotlin.math.round

class PreferencesFragment : DialogFragment() {

    val viewModel by activityViewModels<ConvertViewModel>()

    lateinit var expandableList: MyExpandableList

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(requireContext(), R.style.sortDialogStyle)
        LayoutInflater.from(context).inflate {
            resourceId = R.layout.preferences
        }.apply {
            dialog.setContentView(this)
            layoutParams<ViewGroup.LayoutParams> {
                width =
                    if (isPortrait) (screenWidth / 8) * 7
                    else round(screenWidth / 1.6).toInt()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            findViewById<View>(R.id.preferences)
                .layoutParams<ViewGroup.LayoutParams> {
                    height = if (isPortrait) round(screenHeight * 0.06).toInt()
                    else round(screenHeight * 0.11).toInt()
                }
            expandableList = findViewById(R.id.expanded_menu)
            expandableList.maxHeight =
                if (isPortrait)
                    (screenHeight * 0.7).toInt()
                else round(screenHeight * 0.60).toInt()
            expandableList.apply {
                dividerHeight = 0
            }
            PreferencesAdapter().run {
                setColor(viewModel.randomInt)
                headers = mutableListOf(
                    getString(R.string.notation).toUpperCase(Locale.getDefault()),
                    getString(R.string.grouping_separator),
                    getString(R.string.decimal_separator),
                    getString(R.string.exponent_separator),
                    getString(R.string.decimal_place)
                )
                map = buildMutableMap(4) {
                    put {
                        key = 0
                        value = mutableListOf(
                            getString(R.string.decimal_notation),
                            getString(R.string.scientific_notation),
                            getString(R.string.engineering_notation)
                        )
                    }
                    put {
                        key = 1
                        value = mutableListOf(
                            getString(R.string.comma),
                            getString(R.string.dot),
                            getString(R.string.space)
                        )
                    }
                    put {
                        key = 2
                        value = mutableListOf(
                            getString(R.string.comma),
                            getString(R.string.dot),
                            getString(R.string.space)
                        )
                    }
                    put {
                        key = 3
                        value = mutableListOf(
                            getString(R.string.e_symbol),
                            getString(R.string.small_ten)
                        )
                    }
                }
                mutableMapOf<Int, List<Int>>().apply {
                }
                expandableList.setAdapter(this)
            }
        }
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        val adapter = (expandableList.expandableListAdapter as PreferencesAdapter)
        Log.e("map","${adapter.getSelectedItems()}")
        super.onDismiss(dialog)
    }
}
/**
 * ///
 * notation
 * scientific
 * engineering
 * default
 * ///
 * decimal places
 *
 * */