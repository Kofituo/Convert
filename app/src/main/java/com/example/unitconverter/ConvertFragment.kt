package com.example.unitconverter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
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
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.round


class ConvertDialog : DialogFragment(), MyAdapter.OnRadioButtonsClickListener {

    private lateinit var cancelButton: MaterialButton
    private lateinit var searchBar: TextInputLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewModel: ConvertViewModel
    private lateinit var locale: Locale
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            viewId = getInt("viewId")
            viewName = getString("viewName", "")
            whichButton = getInt("whichButton")
        }
        string = if (whichButton == R.id.top_button) "topButton" else "bottomButton"
        isPrefix = viewId == R.id.prefixes
        initializePrefixes()
        initializeSymbols()
        activity?.run {
            viewModel = ViewModelProviders.of(this)[ConvertViewModel::class.java]
            sharedPreferences = getSharedPreferences(pkgName + viewName, Context.MODE_PRIVATE)
        }
        locale = Locale.getDefault()
        viewModel.dataSet = whichView(viewId)
    }

    private fun buildPrefixes(quantity: String = "", unit: String = ""):
            MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            if (isPrefix) {
                add(
                    RecyclerDataClass(yotta, yottaSymbol)
                )
                add(
                    RecyclerDataClass(zetta, zettaSymbol)
                )
            }
            //
            add(
                RecyclerDataClass(
                    exa + quantity,
                    exaSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    peta + quantity,
                    petaSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    tera + quantity,
                    teraSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    giga + quantity,
                    gigaSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    mega + quantity,
                    megaSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    kilo + quantity,
                    kiloSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    hecto + quantity,
                    hectoSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    deca + quantity,
                    decaSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    deci + quantity,
                    deciSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    centi + quantity,
                    centiSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    milli + quantity,
                    milliSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    micro + quantity,
                    microSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    nano + quantity,
                    nanoSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    pico + quantity,
                    picoSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    femto + quantity,
                    femtoSymbol + unit
                )
            )
            add(
                RecyclerDataClass(
                    atto + quantity,
                    attoSymbol + unit
                )
            )
            if (isPrefix) {
                add(
                    RecyclerDataClass(zepto, zeptoSymbol)
                )
                add(
                    RecyclerDataClass(yocto, yoctoSymbol)
                )
            }
        }
    }

    private fun initializePrefixes() {
        exa = string(R.string.exa)
        peta = string(R.string.peta)
        tera = string(R.string.tera)
        giga = string(R.string.giga)
        mega = string(R.string.mega)
        kilo = string(R.string.kilo)
        hecto = string(R.string.hecto)
        deca = string(R.string.deca)
        deci = string(R.string.deci)
        centi = string(R.string.centi)
        milli = string(R.string.milli)
        micro = string(R.string.micro)
        nano = string(R.string.nano)
        pico = string(R.string.pico)
        femto = string(R.string.femto)
        atto = string(R.string.atto)
        if (isPrefix) {
            yotta = string(R.string.yotta)
            zetta = string(R.string.zetta)
            zepto = string(R.string.zepto)
            yocto = string(R.string.yocto)
        }
    }

    private fun initializeSymbols() {
        exaSymbol = string(R.string.exa_symbol)
        petaSymbol = string(R.string.peta_symbol)
        teraSymbol = string(R.string.tera_symbol)
        gigaSymbol = string(R.string.giga_symbol)
        megaSymbol = string(R.string.mega_symbol)
        kiloSymbol = string(R.string.kilo_symbol)
        hectoSymbol = string(R.string.hecto_symbol)
        decaSymbol = string(R.string.deca_symbol)
        deciSymbol = string(R.string.deci_symbol)
        centiSymbol = string(R.string.centi_symbol)
        milliSymbol = string(R.string.milli_symbol)
        microSymbol = string(R.string.micro_symbol)
        nanoSymbol = string(R.string.nano_symbol)
        picoSymbol = string(R.string.pico_symbol)
        femtoSymbol = string(R.string.femto_symbol)
        attoSymbol = string(R.string.atto_symbol)
        if (isPrefix) {
            yottaSymbol = string(R.string.yotta_symbol)
            zettaSymbol = string(R.string.zetta_symbol)
            zeptoSymbol = string(R.string.zepto_symbol)
            yoctoSymbol = string(R.string.yocto_symbol)
        }
    }

    private fun string(stringId: Int) = resources.getString(stringId)

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(context!!, R.style.sortDialogStyle)
        val view = LayoutInflater.from(context).inflate(R.layout.items_list, null)

        dialog.setContentView(view)

        view.apply {
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
                this@ConvertDialog.viewAdapter =
                    MyAdapter(viewModel.dataSet, viewModel.randomInt).apply {
                        setOnRadioButtonsClickListener(this@ConvertDialog)
                    }
                adapter = viewAdapter

                (adapter as MyAdapter).apply {
                    lastPosition = this@ConvertDialog.lastPosition
                    if (lastPosition != -1) smoothScrollToPosition(lastPosition)
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

    override fun radioButtonClicked(position: Int, text: CharSequence, unit: CharSequence) {
        lastPosition = position
        GlobalScope.launch {
            delay(45)
            dismiss()
        }
    }

    data class CompositeQuantities(val quantity: String, val unit: String)

    private fun buildForMass(): MutableList<RecyclerDataClass> {
        lastPosition = sharedPreferences.getInt(string, -1)
        val gram =
            CompositeQuantities(string(R.string.gram), string(R.string.gram_unit))
        val pound =
            CompositeQuantities(string(R.string.pound), string(R.string.pound_unit))
        val ounce =
            CompositeQuantities(string(R.string.ounce), string(R.string.ounce_unit))
        val metricTon =
            CompositeQuantities(string(R.string.metric_ton), string(R.string.metricTonUnit))
        val shortTon =
            CompositeQuantities(string(R.string.short_ton), string(R.string.short_ton_unit))
        val longTon =
            CompositeQuantities(string(R.string.long_ton), string(R.string.long_ton_unit))
        val carat =
            CompositeQuantities(string(R.string.carat), string(R.string.carat_unit))
        val grain =
            CompositeQuantities(string(R.string.grain), string(R.string.grain_unit))
        val troyPound =
            CompositeQuantities(string(R.string.troy_pound), string(R.string.troy_poundUnit))
        val troyOunce =
            CompositeQuantities(string(R.string.troy_ounce), string(R.string.troyOunceUnit))
        val pennyweight =
            CompositeQuantities(string(R.string.pennyweight), string(R.string.pennyweightUnit))
        val stone =
            CompositeQuantities(string(R.string.stone), string(R.string.stone_unit))
        val atomicMassUnit =
            CompositeQuantities(
                string(R.string.atomicMassUnit),
                string(R.string.atomic_mass_unit_unit)
            )
        val slugMass =
            CompositeQuantities(string(R.string.slug_mass), string(R.string.slug_unit))
        val planckMass =
            CompositeQuantities(string(R.string.planck_mass), string(R.string.planck_mass_unit))
        val solarMass =
            CompositeQuantities(string(R.string.solar_mass), string(R.string.solar_mass_unit))

        return mutableListOf<RecyclerDataClass>().apply {
            gram.apply {
                add(RecyclerDataClass(quantity, unit))
                quantity.toLowerCase(locale).also {
                    addAll(buildPrefixes(it, unit))
                }
            }
            addAll(
                addMany(
                    pound,
                    ounce,
                    metricTon,
                    shortTon,
                    longTon,
                    carat,
                    grain,
                    troyPound,
                    troyOunce,
                    pennyweight,
                    stone,
                    slugMass,
                    atomicMassUnit,
                    planckMass,
                    solarMass
                )
            )
        }
    }

    private fun addMany(vararg data: CompositeQuantities):
            MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            for (i in data)
                add(RecyclerDataClass(i.quantity, i.unit))
        }
    }

    private fun whichView(id: Int): MutableList<RecyclerDataClass> {
        return when (id) {
            R.id.Mass -> {
                buildForMass()
            }
            R.id.prefixes -> {
                lastPosition = sharedPreferences.getInt(string, -1)
                buildPrefixes()
            }
            else -> {
                mutableListOf()
            }
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

}