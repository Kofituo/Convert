package com.example.unitconverter

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.recyclerViewData.Area
import com.example.unitconverter.recyclerViewData.Length
import com.example.unitconverter.recyclerViewData.Mass
import com.example.unitconverter.recyclerViewData.Temperature
import com.google.android.material.button.MaterialButton
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random

class InfoFragment : DialogFragment() {

    private lateinit var cancelButton: MaterialButton
    private var aboutQuantity: AboutQuantity? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("viewId")
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        aboutQuantity = aboutQuantity()
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
                    if (isPortrait) round(screenHeight * 0.75).toInt()
                    else (screenHeight * 0.9).roundToInt()
            }
            (findViewById<ViewGroup>(R.id.content_scroll)[0] as TextView)
                .text = textViewTextTemplate()
            cancelButton.setOnClickListener { dismiss() }
        }
        return dialog
    }

    private inline val Int.gS get() = getString(this)

    @OptIn(ImplicitReflectionSerializer::class)
    private fun aboutQuantity(): AboutQuantity? {
        var didYouKnow: String? = null
        viewName = arguments?.getString("viewName")!!
        sharedPreferences.get<String?>("did_you_know$viewName") {
            if (this.isNotNull()) {
                /**
                 * its in a form of a map so select at random
                 */
                val map =
                    Json.parseMap<String, String>(this).mapKeys { it.key.toInt() }
                val index = Random.nextInt(0, map.size)
                didYouKnow = map[index]
            }
        }
        return when (arguments?.getInt("viewId")) {
            R.id.Mass -> {
                val list = Mass(requireContext()).getList()
                val unit = getUnitsWithPrefix(list, 17)
                AboutQuantity(
                    R.string.mass.gS,
                    R.string.mass_definition.gS,
                    R.string.mass_instrument.gS,
                    unit,
                    didYouKnow
                )
            }

            R.id.Temperature -> {
                val unit = getUnits(Temperature(requireContext()).getList())
                AboutQuantity(
                    R.string.temperature.gS,
                    R.string.temp_def.gS,
                    R.string.temp_instr.gS,
                    unit,
                    didYouKnow
                )
            }

            R.id.Area -> {
                val unit = getUnitsWithPrefix(Area(requireContext()).getList(), 7)
                AboutQuantity(
                    R.string.area.gS,
                    R.string.area_def.gS,
                    R.string.area_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Length -> {
                val unit = getUnitsWithPrefix(Length(requireContext()).getList(), 17)
                AboutQuantity(
                    R.string.length.gS,
                    R.string.length_meta.gS,
                    R.string.length_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Volume -> null

            R.id.Angle -> null

            R.id.time -> null

            R.id.Pressure -> null

            R.id.Speed -> null

            R.id.fuelEconomy -> null

            R.id.dataStorage -> null

            R.id.electric_current -> null

            R.id.luminance -> null

            R.id.Illuminance -> null

            R.id.energy -> null

            R.id.heatCapacity -> null

            R.id.Angular_Velocity -> null

            R.id.angularAcceleration -> null

            R.id.sound -> null

            R.id.resistance -> null

            R.id.radioactivity -> null

            R.id.force -> null

            R.id.power -> null

            R.id.density -> null

            R.id.flow -> null

            R.id.inductance -> null

            R.id.resolution -> null

            else -> null
        }
    }

    private fun getUnits(recyclerDataClassList: List<RecyclerDataClass>): String {
        return recyclerDataClassList.joinToString {
            appendString {
                addWithSpace { it.quantity }
                add { R.string.first_bracket.gS }
                add { it.correspondingUnit }
                add { R.string.second_bracket.gS }
            }
        }
    }

    private fun getUnitsWithPrefix(
        recyclerDataClassList: List<RecyclerDataClass>,
        changeIndex: Int
    ): String {
        recyclerDataClassList.apply {
            val first = get(0).let {
                StringBuilder().apply {
                    put {
                        addWithSpace { it.quantity }
                        add { R.string.first_bracket.gS }
                        add { it.correspondingUnit }
                        add { R.string.second_bracket.gS }
                        add { ", " }
                    }
                }
            }
            val rest = getUnits(subList(changeIndex, size - 1))
            return first.append(rest).toString()
        }
    }

    private fun textViewTextTemplate() =
        aboutQuantity?.let {
            SpannableStringBuilder().apply {
                addWithSpace { increaseSize(boldText(R.string.quantity.gS)) }
                addLn { boldText(it.quantity) }
                appendln()
                addWithSpace { increaseSize(boldText(R.string.definition.gS)) }
                addLn { it.definition }
                appendln()
                addWithSpace { increaseSize(boldText(R.string.measured_with.gS)) }
                addLn { it.instruments }
                appendln()
                addWithSpace { increaseSize(boldText(R.string.units.gS)) }
                addLn { it.units }
                appendln()
                it.didYouKnow?.apply {
                    addWithSpace { increaseSize(italicsAndBold(R.string.did_you_know.gS)) }
                    add { this }
                }
            }
        }

    private fun increaseSize(spannableString: SpannableString) =
        spannableString.apply {
            setSpan(RelativeSizeSpan(1.1f), 0, length, 0)
        }

    private inline fun SpannableStringBuilder.add(charSequence: () -> CharSequence) =
        append(charSequence())

    private inline fun SpannableStringBuilder.addWithSpace(charSequence: () -> CharSequence) =
        append(charSequence()).add { " " }

    private fun boldText(charSequence: CharSequence) =
        SpannableString(charSequence).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
        }

    private fun italicsAndBold(charSequence: CharSequence) =
        SpannableString(charSequence).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
        }
}
