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
import com.example.unitconverter.funtions.Mass.buildForMass
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.MyAdapter
import com.example.unitconverter.subclasses.RecyclerDataClass
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val buildPrefixes = fun(quantity: String, unit: String):
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
        exa = lambdaString(R.string.exa)
        peta = lambdaString(R.string.peta)
        tera = lambdaString(R.string.tera)
        giga = lambdaString(R.string.giga)
        mega = lambdaString(R.string.mega)
        kilo = lambdaString(R.string.kilo)
        hecto = lambdaString(R.string.hecto)
        deca = lambdaString(R.string.deca)
        deci = lambdaString(R.string.deci)
        centi = lambdaString(R.string.centi)
        milli = lambdaString(R.string.milli)
        micro = lambdaString(R.string.micro)
        nano = lambdaString(R.string.nano)
        pico = lambdaString(R.string.pico)
        femto = lambdaString(R.string.femto)
        atto = lambdaString(R.string.atto)
        if (isPrefix) {
            yotta = lambdaString(R.string.yotta)
            zetta = lambdaString(R.string.zetta)
            zepto = lambdaString(R.string.zepto)
            yocto = lambdaString(R.string.yocto)
        }
    }

    private fun initializeSymbols() {
        exaSymbol = lambdaString(R.string.exa_symbol)
        petaSymbol = lambdaString(R.string.peta_symbol)
        teraSymbol = lambdaString(R.string.tera_symbol)
        gigaSymbol = lambdaString(R.string.giga_symbol)
        megaSymbol = lambdaString(R.string.mega_symbol)
        kiloSymbol = lambdaString(R.string.kilo_symbol)
        hectoSymbol = lambdaString(R.string.hecto_symbol)
        decaSymbol = lambdaString(R.string.deca_symbol)
        deciSymbol = lambdaString(R.string.deci_symbol)
        centiSymbol = lambdaString(R.string.centi_symbol)
        milliSymbol = lambdaString(R.string.milli_symbol)
        microSymbol = lambdaString(R.string.micro_symbol)
        nanoSymbol = lambdaString(R.string.nano_symbol)
        picoSymbol = lambdaString(R.string.pico_symbol)
        femtoSymbol = lambdaString(R.string.femto_symbol)
        attoSymbol = lambdaString(R.string.atto_symbol)
        if (isPrefix) {
            yottaSymbol = lambdaString(R.string.yotta_symbol)
            zettaSymbol = lambdaString(R.string.zetta_symbol)
            zeptoSymbol = lambdaString(R.string.zepto_symbol)
            yoctoSymbol = lambdaString(R.string.yocto_symbol)
        }
    }

    private val lambdaString: (Int) -> String = { resources.getString(it) }

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
            R.id.Mass -> {
                buildForMass(lambdaString, buildPrefixes)
            }
            R.id.prefixes -> {
                buildPrefixes("", "")
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

    interface ConvertDialogInterface {
        fun texts(text: String, unit: String)

        fun getOtherValues(position: Int, positionKey: String)
    }
}