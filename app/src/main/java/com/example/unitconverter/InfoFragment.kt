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
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.recyclerViewData.*
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
        Log.e("viewName", viewName)
        sharedPreferences.get<String?>("did_you_know$viewName") {
            Log.e("imfo ", "val $this")
            if (this.isNotNull()) {
                /**
                 * its in a form of a map so select at random
                 */
                Log.e("here n", this)
                val map =
                    Json.parseMap<String, String>(this).mapKeys { it.key.toInt() }
                val index = Random.nextInt(0, map.size)
                didYouKnow = map[index]
            }
        }
        val list: List<RecyclerDataClass>
        val unit: CharSequence
        return when (arguments?.getInt("viewId")) {
            R.id.Mass -> {
                list = Mass(requireContext()).getList()
                unit = getUnitsWithPrefix(list, 17)
                AboutQuantity(
                    R.string.mass.gS,
                    R.string.mass_definition.gS,
                    R.string.mass_instrument.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Temperature -> {
                unit = getUnits(Temperature(requireContext()).getList())
                AboutQuantity(
                    R.string.temperature.gS,
                    R.string.temp_def.gS,
                    R.string.temp_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Area -> {
                unit = getUnitsWithPrefix(Area(requireContext()).getList(), 7)
                AboutQuantity(
                    R.string.area.gS,
                    R.string.area_def.gS,
                    R.string.area_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Length -> {
                unit = getUnitsWithPrefix(Length(requireContext()).getList(), 17)
                AboutQuantity(
                    R.string.length.gS,
                    R.string.length_meta.gS,
                    R.string.length_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Volume -> {
                list = Volume(requireContext()).getList()
                unit = appendString {
                    add { getUnitsWithPrefix(list.subList(0, 1), 1) }
                    add { getUnitsWithPrefix(list.subList(8, list.size), 8) }
                }
                AboutQuantity(
                    R.string.volume.gS,
                    R.string.volume_def.gS,
                    R.string.volume_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Angle -> {
                unit = getUnits(Angle(requireContext()).getList())
                AboutQuantity(
                    R.string.angle.gS,
                    R.string.angle_def.gS,
                    R.string.angle_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.time -> {
                unit = getUnitsWithPrefix(Time(requireContext()).getList(), 17)
                AboutQuantity(
                    R.string.time.gS,
                    R.string.time_def.gS,
                    R.string.time_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Pressure -> {
                list = Pressure(requireContext()).getList()
                unit = appendString {
                    add { addOne(list[0]) }
                    add { addOne(list[17]) }
                    add { addOne(list[18]) }
                    add { getUnitsWithPrefix(list.subList(21, list.size), 9) }
                }
                AboutQuantity(
                    R.string.pressure.gS,
                    R.string.pressure_def.gS,
                    R.string.pressure_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Speed -> {
                list = Speed(requireContext()).getList()
                unit = appendString {
                    add { getUnitsWithPrefix(list.subList(0, 23), 17) }
                    add { ", " }
                    add { getUnits(list.subList(28, list.size)) }
                }
                AboutQuantity(
                    R.string.speed.gS,
                    R.string.speed_def.gS,
                    R.string.speed_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.fuelEconomy -> {
                list = FuelEconomy(requireContext()).getList()
                unit = appendString {
                    add { addOne(list[0]) }
                    add { getUnits(list.subList(5, 12)) }
                    add { addOne(list[15]) }
                    add { addOne(list[list.size - 1]) }
                }
                AboutQuantity(
                    R.string.fuel_economy.gS,
                    R.string.fuel_def.gS,
                    R.string.fuel_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.dataStorage -> {
                list = DataStorage(requireContext()).getList()
                unit = getUnits(list.subList(0, 3))
                AboutQuantity(
                    R.string.data_storage.gS,
                    R.string.data_meta.gS,
                    R.string.data_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.electric_current -> {
                list = ElectricCurrent(requireContext()).getList()
                unit = addOne(list[0]).append(addOne(list[list.size - 1], true))
                AboutQuantity(
                    R.string.electric_current.gS,
                    R.string.current_def.gS,
                    R.string.current_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.luminance -> {
                list = Luminance(requireContext()).getList()
                unit = getUnitsWithPrefix(list.subList(0, 13), 6)
                AboutQuantity(
                    R.string.luminance.gS,
                    R.string.luminance_meta.gS,
                    R.string.luminance_instr.gS,
                    unit,
                    didYouKnow
                )
            }
            R.id.Illuminance -> {
                list = Illuminance(requireContext()).getList()
                unit = appendString {
                    add { getUnitsWithPrefix(list.subList(0, 5), 3) }
                    add { ", " }
                    add { addOne(list[9]).append(addOne(list[10], true)) }
                }
                AboutQuantity(
                    R.string.illuminance.gS,
                    R.string.illuminance_meta.gS,
                    R.string.luminance_instr.gS,
                    unit, didYouKnow
                )
            }
            R.id.energy -> {
                list = Energy(requireContext()).getList()
                unit = appendString {
                    add { getUnitsWithPrefix(list.subList(0, 23), 17) }
                    add { ", " }
                    add { getUnits(list.subList(27, 31)) }
                }
                AboutQuantity(
                    R.string.energy.gS,
                    R.string.energy_def.gS,
                    R.string.energy_instr.gS,
                    unit, didYouKnow
                )
            }
            R.id.Currency -> {
                val sharedPreferences by sharedPreferences {
                    buildString {
                        append(AdditionItems.pkgName)
                        append(viewName)
                        append(AdditionItems.Author) // to prevent name clashes with the fragment
                    }
                }
                var units: String? = null
                sharedPreferences.get<String?>("list_of_currencies") {
                    if (this.isNotNull()) {
                        units = getUnits(ConvertActivity.getCurrencyList(this))
                    }
                }
                AboutQuantity(
                    R.string.currency.gS,
                    R.string.currency_meta.gS,
                    R.string.currency_instr.gS,
                    units, didYouKnow
                )
            }
            R.id.heatCapacity -> {
                list = HeatCapacity(requireContext()).getList()
                unit = getUnitsWithPrefix(list, 4)
                AboutQuantity(
                    R.string.heatCapacity.gS,
                    R.string.heat_cap_def.gS,
                    R.string.heat_cap_instr.gS,
                    unit, didYouKnow
                )
            }

            R.id.Angular_Velocity -> {
                unit = getUnits(Velocity(requireContext()).getList())
                AboutQuantity(
                    R.string.angularVelocity.gS,
                    R.string.velocity_def.gS,
                    R.string.velocity_instr.gS,
                    unit, didYouKnow
                )
            }

            R.id.angularAcceleration -> {
                unit = getUnits(Acceleration(requireContext()).getList())
                AboutQuantity(
                    R.string.angularAcceleration.gS,
                    R.string.angularAcceleration_meta.gS,
                    R.string.velocity_instr.gS,
                    unit, didYouKnow
                )
            }
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
                addWithSpace { it.topText }
                add { R.string.first_bracket.gS }
                add { it.bottomText }
                add { R.string.second_bracket.gS }
            }
        }
    }

    private fun addOne(recyclerDataClass: RecyclerDataClass, isEnd: Boolean = false) =
        recyclerDataClass.run {
            StringBuilder().apply {
                put {
                    addWithSpace { topText }
                    add { R.string.first_bracket.gS }
                    add { bottomText }
                    add { R.string.second_bracket.gS }
                    if (!isEnd)
                        add { ", " }
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
                        addWithSpace { it.topText }
                        add { R.string.first_bracket.gS }
                        add { it.bottomText }
                        add { R.string.second_bracket.gS }
                        add { ", " }
                    }
                }
            }
            val rest = getUnits(subList(changeIndex, size))
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
                it.units?.let {
                    addWithSpace { increaseSize(boldText(R.string.units.gS)) }
                    addLn { it }
                }
                it.didYouKnow?.apply {
                    appendln()
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
            setSpan(StyleSpan(Typeface.BOLD_ITALIC), 0, length, 0)
        }
}
