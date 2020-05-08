package com.example.unitconverter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.Utils.toMap
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.PreferenceData
import com.example.unitconverter.subclasses.PreferencesAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import kotlinx.serialization.stringify
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.round

@OptIn(ImplicitReflectionSerializer::class)
class PreferenceFragment : DialogFragment() {

    private val viewModel by activityViewModels<ConvertViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PreferencesAdapter
    private lateinit var preferenceFragment: PreferenceFragment
    private var done = false
    private lateinit var viewName: String

    //to prevent unwanted saving and refreshes
    private val sharedPreferences by sharedPreferences {
        AdditionItems.pkgName + viewName + "for_preferences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewName = it.getString("viewName") ?: error("no view name ?????")
        }
        sharedPreferences.apply {
            viewModel.apply {
                if (groupToCheckedId.isNull())
                    get<String?>("preferences_for_activity") {
                        groupToCheckedId =
                            if (this.hasValue()) Json.parseMap(this)
                            else buildMutableMap(4) {
                                put(0, 0)
                                put(1, 3)
                                put(2, 7)
                                put(3, 11)
                            }
                    }
                if (sliderValue.isNull())
                    get<Float>("preference_slider_value") {
                        sliderValue = if (this == -1f) 6f else this
                    }
            }
            //get group id to enabled id
            if (viewModel.mGroupToEnabledID.isNullOrEmpty()) {
                viewModel.mGroupToEnabledID = buildMutableMap(3)
                //either all is there or none is there
                for (i in 1..3) {
                    get<String?>("mGroupToEnabledID$i") {
                        if (this.hasValue()) {
                            val sparseBooleanArray = SparseBooleanArray(2)
                            Json.parseMap<Int, Boolean>(this).forEach {
                                sparseBooleanArray.append(it.key, it.value)
                            }
                            viewModel.mGroupToEnabledID!![i] = sparseBooleanArray
                        } else {
                            viewModel.mGroupToEnabledID = defaultIntToBooleanArray()
                            //Log.e("bre", "eak")
                            return@apply //since i can't use break
                        }
                    }
                }
            }
        }
        viewModel.apply {
            if (initialMap.isNull())
                initialMap = groupToCheckedId?.toMap()
            if (initialSliderValue.isNull())
                initialSliderValue = sliderValue
            //Log.e("mgr", "$mGroupToEnabledID")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //save the options
        viewModel.dataSetCopy = adapter.dataSetCopy
        viewModel.numberOfItems = adapter.numberOfVisibleItems
        viewModel.groupsExpanded = adapter.groupsExpanded
        viewModel.visibleItemsPerGroup = adapter.visibleItemsPerGroup
        viewModel.sliderValue = adapter.sliderValue
        viewModel.groupToCheckedId = adapter.groupToCheckedId
        viewModel.mGroupToEnabledID = adapter.mGroupToEnabledID
    }

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
            recyclerView =
                findViewById<ConstraintLayout>(R.id.expanded_menu).findViewById(R.id.recycler_view)
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
                setItemViewCacheSize(20)
                layoutManager = LinearLayoutManager(context)
                adapter = PreferencesAdapter(map).apply {
                    color = colour
                    groupToCheckedId.putAll(viewModel.groupToCheckedId!!)
                    numberOfVisibleItems = viewModel.numberOfItems ?: numberOfVisibleItems
                    dataSetCopy = viewModel.dataSetCopy ?: dataSetCopy
                    visibleItemsPerGroup = viewModel.visibleItemsPerGroup ?: visibleItemsPerGroup
                    groupsExpanded = viewModel.groupsExpanded ?: groupsExpanded
                    mGroupToEnabledID = viewModel.mGroupToEnabledID!! //can't be null at this point
                    sliderValue = viewModel.sliderValue!!
                    separators = defaultSeparators ?: defaultSeparators() // load every time
                    defaultIdToIsChecked = defaultIdToIsChecked(separators, groupToCheckedId)
                    this@PreferenceFragment.adapter = this
                }
            }
            val doneButton = findViewById<MaterialButton>(R.id.done_button)
            doneButton.apply {
                ColorStateList.valueOf(colour).also {
                    rippleColor = it
                    strokeColor = it
                }
                setOnClickListener {
                    done = true
                    dismiss()
                }
            }
        }
        return dialog
    }

    private fun defaultIdToIsChecked(separators: Separators, groupToCheckedId: Map<Int, Int>) =
        buildMutableMap<Int, Boolean>(2) {
            separators.apply {
                val firstChecked = groupToCheckedId[1] ?: error("the hell??")
                val secondChecked = groupToCheckedId.getValue(2)
                groupingSeparatorId?.also {
                    if (it == firstChecked)
                        put(3, false)
                    else if (it == secondChecked)
                        put(3, false)
                }
                decimalSeparatorId?.also {
                    if (it == firstChecked)
                        put(7, false)
                    else if (it == secondChecked)
                        put(7, false)
                }
            }
        }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (done) {
            //save data and apply changes
            adapter.run {
                val checkedChange = groupToCheckedId != viewModel.initialMap
                val sliderValueChanged = viewModel.initialSliderValue != sliderValue
                if (checkedChange || sliderValueChanged) {
                    sharedPreferences.edit {
                        if (checkedChange)
                            put<String> {
                                key = "preferences_for_activity"
                                value = Json.stringify(groupToCheckedId)
                            }
                        if (sliderValueChanged)
                            put<Float> {
                                key = "preference_slider_value"
                                value = sliderValue
                            }
                        for (i in 1..3) {
                            put<String> {
                                key = "mGroupToEnabledID$i"
                                value = Json.stringify(mGroupToEnabledID[i]!!.toMap())
                            }
                        }
                    }
                    groupToCheckedId.forEach {
                        preferenceFragment.getChosenValues(it.key, it.value)
                    }
                    preferenceFragment.getSliderValue(sliderValue.toInt())
                    preferenceFragment.applyChanges() //to signify the end of the updates
                }
            }
        }
    }

    private fun buildDataMap() =
        buildMutableMap<String, List<PreferenceData>>(5) {
            var start = 0
            put {
                key = getString(R.string.notation).toUpperCase(Locale.getDefault())
                value = mutableListOf(
                    PreferenceData(getString(R.string.decimal_notation), start++, 0),
                    PreferenceData(getString(R.string.scientific_notation), start++, 0),
                    PreferenceData(getString(R.string.engineering_notation), start++, 0)
                )
            }
            put {
                key = getString(R.string.grouping_separator)
                value = mutableListOf(
                    PreferenceData(getString(R.string._default), start++, 1),
                    PreferenceData(getString(R.string.comma), start++, 1),
                    PreferenceData(getString(R.string.dot), start++, 1),
                    PreferenceData(getString(R.string.space), start++, 1)
                )
            }
            put {
                key = getString(R.string.decimal_separator)
                value = mutableListOf(
                    PreferenceData(getString(R.string._default), start++, 2),
                    PreferenceData(getString(R.string.comma), start++, 2),
                    PreferenceData(getString(R.string.dot), start++, 2),
                    PreferenceData(getString(R.string.space), start++, 2)
                )
            }
            put {
                key = getString(R.string.exponent_separator)
                value = mutableListOf(
                    PreferenceData(getString(R.string.e_symbol), start++, 3),
                    PreferenceData(getString(R.string.small_ten), start++, 3)
                )
            }
            put {
                key = getString(R.string.decimal_place)
                value = mutableListOf()
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferenceFragment = context as PreferenceFragment
    }

    private var defaultSeparators: Separators? = null //just to prevent double calling
    private fun defaultIntToBooleanArray() =
        buildMutableMap<Int, SparseBooleanArray>(3) {
            put(1, SparseBooleanArray(2).apply { append(4, true); append(8, true) })
            put(2, SparseBooleanArray(2).apply { append(5, true); append(9, true) })
            put(3, SparseBooleanArray(2).apply { append(6, true); append(10, true) })
            defaultSeparators().apply {
                //they could be null
                decimalSeparatorId?.let { get(decimalGroup)!!.put(it, false) }
                groupingSeparatorId?.let { get(groupingGroup)!!.put(it, false) }
                defaultSeparators = this
            }
        }

    private fun defaultSeparators(): Separators {
        val decimalFormatSymbols = DecimalFormatSymbols.getInstance()
        decimalFormatSymbols.apply {
            val separators = Separators()
            when (groupingSeparator) {
                '.' -> separators.apply {
                    groupingSeparatorId = 9
                    groupingGroup = 2
                }
                ',' -> separators.apply {
                    groupingSeparatorId = 8
                    groupingGroup = 1
                }
                else -> {
                    //normal space
                    when {
                        groupingSeparator.isWhitespace() -> separators.apply {
                            groupingSeparatorId = 10
                            groupingGroup = 3
                        }
                    }
                }
            }
            when (decimalSeparator) {
                '.' -> separators.apply {
                    decimalSeparatorId = 5
                    decimalGroup = 2
                }
                ',' -> separators.apply {
                    decimalSeparatorId = 4
                    decimalGroup = 1
                }
                else -> {
                    when {
                        decimalSeparator.isWhitespace() ->
                            separators.apply {
                                decimalSeparatorId = 6
                                decimalGroup = 3
                            }
                    }
                }
            }
            return separators
        }
    }

    data class Separators(
        var decimalSeparatorId: Int? = null,
        var groupingSeparatorId: Int? = null,
        var decimalGroup: Int? = null,
        var groupingGroup: Int? = null
    )

    interface PreferenceFragment {
        fun getChosenValues(group: Int, child: Int)
        fun getSliderValue(sliderValue: Int)
        fun applyChanges()
    }
}
