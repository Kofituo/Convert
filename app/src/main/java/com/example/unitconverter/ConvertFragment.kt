package com.example.unitconverter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.recyclerViewData.*
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.MyAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round

class ConvertFragment : DialogFragment(), MyAdapter.OnRadioButtonsClickListener {

    private lateinit var cancelButton: MaterialButton
    private lateinit var searchBar: TextInputLayout
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ConvertViewModel by activityViewModels()
    private val sharedPreferences by sharedPreferences {
        pkgName + viewName
    }

    //
    private lateinit var viewName: String
    private var lastPosition = -1
    private var whichButton = -1
    private lateinit var string: String
    private lateinit var convertDialogInterface: ConvertDialogInterface
    private lateinit var positionKey: String
    private val comparator =
        Comparator { first: RecyclerDataClass, second: RecyclerDataClass ->
            first.quantity.compareTo(second.quantity)
        }
    private var start = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        start = System.nanoTime()
        super.onCreate(savedInstanceState)
        var viewId: Int = -1
        arguments?.apply {
            viewId = getInt("viewId")
            viewName = getString("viewName", "")
            whichButton = getInt("whichButton")
        }
        string = if (whichButton == R.id.top_button) "topButton" else "bottomButton"
        positionKey = string.substringBefore("B") + "Position"
        viewModel.apply {
            if (!dataSetIsInit || dataSet.isEmpty()) {
                dataSet =
                    arguments
                        ?.getSerializable("for_currency") as? MutableList<RecyclerDataClass>
                        ?: whichView(viewId)
                Log.e("what","$dataSet")
            }
            Log.e("inini", "init  ${arguments?.getSerializable("for_currency")}")
        }
        lastPosition = sharedPreferences.get(string)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(requireContext(), R.style.sortDialogStyle)
        //view
        LayoutInflater.from(context).inflate {
            resourceId = R.layout.items_list //root is by default set to null
        }.apply {
            dialog.setContentView(this)
            cancelButton = findViewById(R.id.cancel_button)
            searchBar = findViewById(R.id.search_bar)

            layoutParams<ViewGroup.LayoutParams> {
                width =
                    if (isPortrait) (screenWidth / 8) * 7
                    else round(screenWidth / 1.6).toInt()
                height =
                    if (isPortrait) round(screenHeight * 0.92).toInt()
                    else ViewGroup.LayoutParams.WRAP_CONTENT
            }
            recyclerView = recyclerView {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = adapter(viewModel.dataSet, viewModel.randomInt, comparator) {
                    setOnRadioButtonsClickListener(this@ConvertFragment)
                    add(viewModel.dataSet) // adds tom the sorted list
                    lastPosition = this@ConvertFragment.lastPosition
                    if (lastPosition != -1)
                        smoothScrollToPosition(lastPosition)
                    //Log.e("last", "$lastPosition ")
                }
            }
            searchBar.findViewById<TextInputEditText>(R.id.searchEditText)
                ?.addTextChangedListener(
                    SearchTextChangeListener(
                        recyclerView,
                        recyclerView.adapter as MyAdapter,
                        viewModel.dataSet
                    )
                )
        }
        setDialogColors(viewModel.randomInt)
        Log.e("time", "${(System.nanoTime() - start) / 1000_000.0}") //to get it in milli seconds
        return dialog
    }

    private inline fun View.recyclerView(block: RecyclerView.() -> Unit) =
        findViewById<RecyclerView>(R.id.recycler_view).apply(block)

    private inline fun adapter(
        dataSet: MutableList<RecyclerDataClass>,
        colorInt: Int,
        comparator: Comparator<RecyclerDataClass>,
        block: MyAdapter.() -> Unit
    ) = MyAdapter(dataSet, colorInt, comparator).apply(block)

    private fun setDialogColors(colorInt: Int) {
        searchBar.boxStrokeColor = colorInt
        cancelButton.apply {
            ColorStateList.valueOf(colorInt).also {
                rippleColor = it
                //setTextColor(it)
                strokeColor = it
            }
            setOnClickListener {
                dismiss()
            }
        }
    }

    override fun radioButtonClicked(position: Int, text: CharSequence, unit: CharSequence) {
        lastPosition = position
        convertDialogInterface.apply {
            getOtherValues(position, positionKey)
            texts(text, unit)
        }
        GlobalScope.launch {
            delay(25)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        convertDialogInterface = context as ConvertDialogInterface
    }

    private fun whichView(id: Int): MutableList<RecyclerDataClass> {
        return when (id) {
            R.id.Mass -> Mass(requireContext()).getList()

            R.id.prefixes -> Prefix(requireContext()).getList()

            R.id.Temperature -> Temperature(requireContext()).getList()

            R.id.Area -> Area(requireContext()).getList()

            R.id.Length -> Length(requireContext()).getList()

            R.id.Volume -> Volume(requireContext()).getList()

            R.id.Angle -> Angle(requireContext()).getList()

            R.id.time -> Time(requireContext()).getList()

            R.id.Pressure -> Pressure(requireContext()).getList()

            R.id.Speed -> Speed(requireContext()).getList()

            R.id.fuelEconomy -> FuelEconomy(requireContext()).getList()

            R.id.dataStorage -> DataStorage(requireContext()).getList()

            R.id.electric_current -> ElectricCurrent(requireContext()).getList()

            R.id.luminance -> Luminance(requireContext()).getList()

            R.id.Illuminance -> Illuminance(requireContext()).getList()

            R.id.energy -> Energy(requireContext()).getList()

            else -> mutableListOf()
        }
    }

    private fun saveData() {
        editPreferences(sharedPreferences) {
            put<Int> {
                key = string
                value = lastPosition
            }
            apply()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        saveData()
        super.onDismiss(dialog)
    }

    interface ConvertDialogInterface {
        fun texts(text: CharSequence, unit: CharSequence)
        fun getOtherValues(position: Int, positionKey: String)
    }
}

//comparator for re usability
//eg
//    private val comparator =
//        Comparator { first: RecyclerDataClass, second: RecyclerDataClass ->
//            first.unit.compareTo(second.unit)
//        }
