package com.example.unitconverter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.MyAdapter
import com.example.unitconverter.subclasses.RecyclerDataClass
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.math.round


class ConvertDialog : DialogFragment(), MyAdapter.OnRadioButtonsClickListener {

    private lateinit var cancelButton: MaterialButton
    private lateinit var searchBar: TextInputLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewModel: ConvertViewModel
    //Prefixes and their units
    private lateinit var yotta: String
    private lateinit var yottaSymbol: String
    private lateinit var zetta: String
    private lateinit var zettaSymbol: String
    private lateinit var exa: String
    private lateinit var exaSymbol: String
    private lateinit var peta: String
    private lateinit var petaSymbol: String
    private lateinit var tera: String
    private lateinit var teraSymbol: String
    private lateinit var giga: String
    private lateinit var gigaSymbol: String
    private lateinit var mega: String
    private lateinit var megaSymbol: String
    private lateinit var kilo: String
    private lateinit var kiloSymbol: String
    private lateinit var hecto: String
    private lateinit var hectoSymbol: String
    private lateinit var deca: String
    private lateinit var decaSymbol: String
    private lateinit var deci: String
    private lateinit var deciSymbol: String
    private lateinit var centi: String
    private lateinit var centiSymbol: String
    private lateinit var milli: String
    private lateinit var milliSymbol: String
    private lateinit var micro: String
    private lateinit var microSymbol: String
    private lateinit var nano: String
    private lateinit var nanoSymbol: String
    private lateinit var pico: String
    private lateinit var picoSymbol: String
    private lateinit var femto: String
    private lateinit var femtoSymbol: String
    private lateinit var atto: String
    private lateinit var attoSymbol: String
    private lateinit var zepto: String
    private lateinit var zeptoSymbol: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var yocto: String
    private lateinit var yoctoSymbol: String
    //
    private var isPrefix = false
    private var viewId: Int = -1
    private lateinit var viewName: String
    private var lastPosition = 1
    private var whichButton = -1
    private lateinit var string: String
    private lateinit var convertDialogInterface: ConvertDialogInterface
    private lateinit var positionKey: String

    //comparator for re usability
    //eg
//    private val comparator =
//        Comparator { first: RecyclerDataClass, second: RecyclerDataClass ->
//            first.unit.compareTo(second.unit)
//        }

    private val comparator =
        Comparator { first: RecyclerDataClass, second: RecyclerDataClass ->
            first.quantity.compareTo(second.quantity)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            viewId = getInt("viewId")
            viewName = getString("viewName", "")
            whichButton = getInt("whichButton")
        }
        string = if (whichButton == R.id.top_button) "topButton" else "bottomButton"
        positionKey = string.substringBefore("B") + "Position"
        isPrefix = viewId == R.id.prefixes
        initializePrefixes()
        initializeSymbols()
        activity?.run {
            viewModel = ViewModelProviders.of(this)[ConvertViewModel::class.java]
            sharedPreferences = getSharedPreferences(pkgName + viewName, Context.MODE_PRIVATE)
        }
        viewModel.dataSet = whichView(viewId)
        lastPosition = sharedPreferences.getInt(string, -1)
    }

    private fun buildPrefixes():
            MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {

            add(RecyclerDataClass(yotta, yottaSymbol, 0))

            add(RecyclerDataClass(zetta, zettaSymbol, 1))
            //
            addAll(massPrefixes("", "", 2))
            //
            add(RecyclerDataClass(zepto, zeptoSymbol, 18))
            add(RecyclerDataClass(yocto, yoctoSymbol, 19))

        }
    }

    private fun initializePrefixes() {
        exa = getString(R.string.exa)
        peta = getString(R.string.peta)
        tera = getString(R.string.tera)
        giga = getString(R.string.giga)
        mega = getString(R.string.mega)
        kilo = getString(R.string.kilo)
        hecto = getString(R.string.hecto)
        deca = getString(R.string.deca)
        deci = getString(R.string.deci)
        centi = getString(R.string.centi)
        milli = getString(R.string.milli)
        micro = getString(R.string.micro)
        nano = getString(R.string.nano)
        pico = getString(R.string.pico)
        femto = getString(R.string.femto)
        atto = getString(R.string.atto)
        if (isPrefix) {
            yotta = getString(R.string.yotta)
            zetta = getString(R.string.zetta)
            zepto = getString(R.string.zepto)
            yocto = getString(R.string.yocto)
        }
    }

    private fun initializeSymbols() {
        exaSymbol = getString(R.string.exa_symbol)
        petaSymbol = getString(R.string.peta_symbol)
        teraSymbol = getString(R.string.tera_symbol)
        gigaSymbol = getString(R.string.giga_symbol)
        megaSymbol = getString(R.string.mega_symbol)
        kiloSymbol = getString(R.string.kilo_symbol)
        hectoSymbol = getString(R.string.hecto_symbol)
        decaSymbol = getString(R.string.deca_symbol)
        deciSymbol = getString(R.string.deci_symbol)
        centiSymbol = getString(R.string.centi_symbol)
        milliSymbol = getString(R.string.milli_symbol)
        microSymbol = getString(R.string.micro_symbol)
        nanoSymbol = getString(R.string.nano_symbol)
        picoSymbol = getString(R.string.pico_symbol)
        femtoSymbol = getString(R.string.femto_symbol)
        attoSymbol = getString(R.string.atto_symbol)
        if (isPrefix) {
            yottaSymbol = getString(R.string.yotta_symbol)
            zettaSymbol = getString(R.string.zetta_symbol)
            zeptoSymbol = getString(R.string.zepto_symbol)
            yoctoSymbol = getString(R.string.yocto_symbol)
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(context!!, R.style.sortDialogStyle)
        //view
        LayoutInflater.from(context).inflate(R.layout.items_list, null).apply {

            dialog.setContentView(this)
            cancelButton = findViewById(R.id.cancel_button)
            searchBar = findViewById(R.id.search_bar)
            val params = layoutParams
            params.width =
                if (isPortrait) (screenWidth / 8) * 7 else round(screenWidth / 1.6).toInt()

            params.height =
                if (isPortrait) round(screenHeight * 0.92).toInt() else ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams = params

            recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                setHasFixedSize(true)
                viewManager = LinearLayoutManager(context)
                layoutManager = viewManager
                this@ConvertDialog.viewAdapter = // add comparator to params
                    MyAdapter(viewModel.dataSet, viewModel.randomInt, comparator)
                        .apply { setOnRadioButtonsClickListener(this@ConvertDialog) }

                adapter = (viewAdapter as MyAdapter).apply {
                    add(viewModel.dataSet)

                    lastPosition = this@ConvertDialog.lastPosition

                    if (lastPosition != -1) smoothScrollToPosition(lastPosition)

                    searchBar.findViewById<TextInputEditText>(R.id.searchEditText)?.apply {

                        addTextChangedListener(object : TextWatcher {
                            var called = false
                            override fun afterTextChanged(s: Editable?) {
                                val filteredList =
                                    filter(viewModel.dataSet, s.toString())
                                replaceAll(filteredList)
                                if (s.isNullOrEmpty()) {
                                    boolean = false
                                    called = false
                                    notifyItemRangeChanged(0, itemCount)
                                }
                                smoothScrollToPosition(0)
                            }

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                                boolean = true
                                if (!called) notifyItemRangeChanged(0, itemCount)
                                called = true
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) = Unit
                        })
                    }
                }
            }
        }

        setDialogColors(viewModel.randomInt)

        return dialog
    }

    private fun setDialogColors(colorInt: Int) {
        searchBar.boxStrokeColor = colorInt
        cancelButton.apply {
            ColorStateList.valueOf(colorInt).also {
                rippleColor = it
                setTextColor(it)
            }
            setOnClickListener {
                dismiss()
            }
        }
    }

    override fun radioButtonClicked(position: Int, text: String, unit: String) {
        lastPosition = position
        convertDialogInterface.apply {
            getOtherValues(position, positionKey)
            texts(text, unit)
        }
        GlobalScope.launch {
            delay(45)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        convertDialogInterface = context as ConvertDialogInterface
    }

    private fun whichView(id: Int): MutableList<RecyclerDataClass> {
        return when (id) {
            R.id.Mass -> buildForMass()

            R.id.prefixes -> buildPrefixes()

            R.id.Temperature -> buildForTemperature()

            else -> mutableListOf()
        }
    }

    private fun saveData() {
        sharedPreferences.apply {
            with(edit()) {
                putInt(string, lastPosition)
                apply()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        saveData()
        super.onDismiss(dialog)
    }

    private fun filter(
        dataSet: MutableList<RecyclerDataClass>,
        searchText: String
    ): MutableList<RecyclerDataClass> {
        val locale = Locale.getDefault()
        val mainText = searchText.trim().toLowerCase(locale)
        return mutableListOf<RecyclerDataClass>().apply {
            for (i in dataSet) {
                val text = i.quantity.toLowerCase(locale)
                val unit = i.correspondingUnit.toLowerCase(locale)
                if (text.contains(mainText) || unit.contains(mainText))
                    add(i)
            }
        }
    }

    interface ConvertDialogInterface {
        fun texts(text: String, unit: String)
        fun getOtherValues(position: Int, positionKey: String)
    }

    //FUNCTIONS
    private fun buildForMass(): MutableList<RecyclerDataClass> {
        val gram =
            RecyclerDataClass(getString(R.string.gram), getString(R.string.gram_unit), 0)
        val pound =
            RecyclerDataClass(getString(R.string.pound), getString(R.string.pound_unit), 17)
        val ounce =
            RecyclerDataClass(getString(R.string.ounce), getString(R.string.ounce_unit), 18)
        val metricTon =
            RecyclerDataClass(getString(R.string.metric_ton), getString(R.string.metricTonUnit), 19)
        val shortTon =
            RecyclerDataClass(getString(R.string.short_ton), getString(R.string.short_ton_unit), 20)
        val longTon =
            RecyclerDataClass(getString(R.string.long_ton), getString(R.string.long_ton_unit), 21)
        val carat =
            RecyclerDataClass(getString(R.string.carat), getString(R.string.carat_unit), 22)
        val grain =
            RecyclerDataClass(getString(R.string.grain), getString(R.string.grain_unit), 23)
        val troyPound =
            RecyclerDataClass(
                getString(R.string.troy_pound),
                getString(R.string.troy_poundUnit),
                24
            )
        val troyOunce =
            RecyclerDataClass(getString(R.string.troy_ounce), getString(R.string.troyOunceUnit), 25)
        val pennyweight =
            RecyclerDataClass(
                getString(R.string.pennyweight),
                getString(R.string.pennyweightUnit),
                26
            )
        val stone =
            RecyclerDataClass(getString(R.string.stone), getString(R.string.stone_unit), 27)
        val atomicMassUnit =
            RecyclerDataClass(
                getString(R.string.atomicMassUnit),
                getString(R.string.atomic_mass_unit_unit), 28
            )
        val slugMass =
            RecyclerDataClass(getString(R.string.slug_mass), getString(R.string.slug_unit), 29)
        val planckMass =
            RecyclerDataClass(
                getString(R.string.planck_mass),
                getString(R.string.planck_mass_unit),
                30
            )
        val solarMass =
            RecyclerDataClass(
                getString(R.string.solar_mass),
                getString(R.string.solar_mass_unit),
                31
            )

        return mutableListOf<RecyclerDataClass>().apply {
            gram.apply {
                add(this)
                quantity.toLowerCase(Locale.getDefault()).also {
                    addAll(massPrefixes(it, correspondingUnit, 1))
                }
            }
            add(pound)
            add(ounce)
            add(metricTon)
            add(shortTon)
            add(longTon)
            add(carat)
            add(grain)
            add(troyPound)
            add(troyOunce)
            add(pennyweight)
            add(stone)
            add(slugMass)
            add(atomicMassUnit)
            add(planckMass)
            add(solarMass)
        }
    }

    private fun massPrefixes(
        quantity: String,
        unit: String,
        start: Int
    ): MutableList<RecyclerDataClass> {
        var startInt = start
        return mutableListOf<RecyclerDataClass>().apply {
            add(
                RecyclerDataClass(exa + quantity, exaSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(peta + quantity, petaSymbol + unit, startInt++)
            )

            add(
                RecyclerDataClass(tera + quantity, teraSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(giga + quantity, gigaSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(mega + quantity, megaSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(kilo + quantity, kiloSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(hecto + quantity, hectoSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(deca + quantity, decaSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(deci + quantity, deciSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(centi + quantity, centiSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(milli + quantity, milliSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(micro + quantity, microSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(nano + quantity, nanoSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(pico + quantity, picoSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(femto + quantity, femtoSymbol + unit, startInt++)
            )
            add(
                RecyclerDataClass(atto + quantity, attoSymbol + unit, startInt++)
            )
            Log.e("st", "$startInt")
        }
    }

    private fun buildForTemperature(): MutableList<RecyclerDataClass> {
        val celsius =
            RecyclerDataClass(
                getString(R.string.celsius),
                getString(R.string.celsius_unit), 0
            )
        val fahrenheit =
            RecyclerDataClass(
                getString(R.string.fahrenheit),
                getString(R.string.fahrenheit_unit), 1
            )
        val kelvin =
            RecyclerDataClass(
                getString(R.string.kelvin),
                getString(R.string.kelvin_unit), 2
            )

        return mutableListOf<RecyclerDataClass>().apply {
            add(celsius)
            add(fahrenheit)
            add(kelvin)
        }
    }
}