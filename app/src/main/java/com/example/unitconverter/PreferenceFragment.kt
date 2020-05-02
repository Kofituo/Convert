package com.example.unitconverter

import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.miscellaneous.inflate
import com.example.unitconverter.miscellaneous.layoutParams
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.PreferenceData
import com.example.unitconverter.subclasses.PreferencesAdapter
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.math.round

class PreferenceFragment : DialogFragment() {

    lateinit var doneButton: MaterialButton
    private val viewModel by activityViewModels<ConvertViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(requireContext(), R.style.sortDialogStyle)
        //view
        LayoutInflater.from(context).inflate {
            resourceId = R.layout.preferences
        }.apply {
            dialog.setContentView(this)
            findViewById<ConstraintLayout>(R.id.expanded_menu)
                .apply {
                    layoutParams<ConstraintLayout.LayoutParams> {
                        matchConstraintMaxHeight = (screenHeight * 0.6).toInt()
                    }
                }
            val recyclerView =
                findViewById<ConstraintLayout>(R.id.expanded_menu).findViewById<RecyclerView>(R.id.recycler_view)
            layoutParams<ViewGroup.LayoutParams> {
                width =
                    if (isPortrait) (screenWidth / 8) * 7
                    else round(screenWidth / 1.6).toInt()
            }
            val map =
                viewModel.preferenceMap ?: buildDataMap().apply { viewModel.preferenceMap = this }
            val colour = viewModel.randomInt
            recyclerView.apply {
                //setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PreferencesAdapter(map).apply {
                    context = requireContext()
                    color = colour
                }
            }
            doneButton = findViewById(R.id.done_button)
            doneButton.apply {
                ColorStateList.valueOf(colour).also {
                    rippleColor = it
                    strokeColor = it
                }
                setOnClickListener {
                    dismiss()
                }
            }
        }
        return dialog
    }

    private fun buildDataMap() =
        buildMutableMap<String, List<PreferenceData>>(5) {
            var start = 0
            put {
                key = getString(R.string.notation).toUpperCase(Locale.getDefault())
                value = mutableListOf(
                    PreferenceData(getString(R.string.decimal_notation), start++),
                    PreferenceData(getString(R.string.scientific_notation), start++),
                    PreferenceData(getString(R.string.engineering_notation), start++)
                )
            }
            put {
                key = getString(R.string.grouping_separator)
                value = mutableListOf(
                    PreferenceData(getString(R.string._default), start++),
                    PreferenceData(getString(R.string.comma), start++),
                    PreferenceData(getString(R.string.dot), start++),
                    PreferenceData(getString(R.string.space), start++)
                )
            }
            put {
                key = getString(R.string.decimal_separator)
                value = mutableListOf(
                    PreferenceData(getString(R.string._default), start++),
                    PreferenceData(getString(R.string.comma), start++),
                    PreferenceData(getString(R.string.dot), start++),
                    PreferenceData(getString(R.string.space), start++)
                )
            }
            put {
                key = getString(R.string.exponent_separator)
                value = mutableListOf(
                    PreferenceData(getString(R.string.e_symbol), start++),
                    PreferenceData(getString(R.string.small_ten), start++)
                )
            }
            put {
                key = getString(R.string.decimal_place)
                value = mutableListOf()
            }
        }
}
