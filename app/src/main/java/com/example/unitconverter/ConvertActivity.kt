package com.example.unitconverter

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.RecyclerDataClass
import com.example.unitconverter.subclasses.TextMessage
import com.example.unitconverter.subclasses.ViewIdMessage
import kotlinx.android.synthetic.main.activity_convert.*
import java.util.*

class ConvertActivity : AppCompatActivity() {
    private var swap = false
    private var randomColor = -1
    private var viewId = -1
    private lateinit var dialog: ConvertDialog
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
    private lateinit var yocto: String
    private lateinit var yoctoSymbol: String
    //
    private lateinit var locale: Locale
    private var isPrefix = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        locale = Locale.getDefault()

        secondEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        firstEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        val isRTL =
            TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
        if (!isRTL) {
            bottom_button.setTopPadding(-3) //converts it to dp
            top_button.setTopPadding(-3)
        }
        // for setting the text
        intent.apply {
            getStringExtra(TextMessage)?.also {
                convert_header?.text = it
                app_bar_text.text = it
            }
            viewId = getIntExtra(ViewIdMessage, -1).apply {
                isPrefix = equals(R.id.prefixes)
            }
        }
        initializePrefixes(); initializeSymbols()

        ViewModelProviders.of(this)[ConvertViewModel::class.java] // for the view model
            .apply {
                settingColours(randomInt)
                randomInt = randomColor
                dataSet = if (dataSet.isEmpty()) whichView(viewId) else dataSet
            }

        top_button.setOnClickListener {
            ConvertDialog().apply {
                show(supportFragmentManager, "dialog")
            }
        }
        bottom_button.setOnClickListener {
            ConvertDialog().apply {
                show(supportFragmentManager, "dialog")
            }
        }
        Log.e("sdsad", "sddsad")
    }


    private fun whichView(viewId: Int): MutableList<RecyclerDataClass> {
        return when (viewId) {
            R.id.Mass -> {
                buildForMass()
            }
            R.id.prefixes -> {
                buildForPrefix()
            }
            else -> mutableListOf()
        }
    }

    private fun buildForPrefix(): MutableList<RecyclerDataClass> {

        return buildPrefixes("", "")
    }

    data class CompositeQuantities(val quantity: String, val unit: String)

    private fun buildForMass(): MutableList<RecyclerDataClass> {
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

    private fun buildPrefixes(quantity: String, unit: String):
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.swap -> {
                swap()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.convert_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (isPrefix) menu?.removeItem(R.id.prefixes)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun swap() {
        val constraintSet = ConstraintSet()
        val firstBox = if (swap) R.id.secondBox else R.id.firstBox
        val secondBox = if (swap) R.id.firstBox else R.id.secondBox
        val topButton = if (swap) R.id.bottom_button else R.id.top_button
        val bottomButton = if (swap) R.id.top_button else R.id.bottom_button

        constraintSet.apply {
            clone(convert_inner)
            // for the first box
            //clear(R.id.firstBox,ConstraintSet.TOP)
            connect(
                firstBox,
                ConstraintSet.TOP,
                secondBox,
                ConstraintSet.BOTTOM,
                40.dpToInt(this@ConvertActivity)
            )
            connect(
                firstBox,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )

            // for the second box
            //clear(R.id.secondBox,ConstraintSet.TOP)
            clear(secondBox, ConstraintSet.BOTTOM)
            connect(secondBox, ConstraintSet.TOP, R.id.topGuide, ConstraintSet.TOP, 0)

            // for the top button
            connect(topButton, ConstraintSet.TOP, firstBox, ConstraintSet.TOP)
            connect(topButton, ConstraintSet.BOTTOM, firstBox, ConstraintSet.BOTTOM)
            connect(topButton, ConstraintSet.START, firstBox, ConstraintSet.END)

            //for the down button
            connect(bottomButton, ConstraintSet.TOP, R.id.topGuide, ConstraintSet.TOP)
            connect(bottomButton, ConstraintSet.BOTTOM, secondBox, ConstraintSet.BOTTOM)
            connect(bottomButton, ConstraintSet.START, secondBox, ConstraintSet.END)

            TransitionManager.beginDelayedTransition(convert_inner)
            applyTo(convert_inner)
        }
        swap = !swap
    }

    private fun string(stringId: Int) = resources.getString(stringId)

    private fun settingColours(colorInt: Int = 0) {
        val colourArray = listOf(
            "#29B6F6", "#FFD54F", "#DCE775",
            "#D4E157", "#E1BEE7", "#E57373",
            "#EF5350", "#66BB6A", "#FFA726",
            "#5C6BC0", "#FFCA28", "#9CCC65",
            "#FFCCBC", "#7986CB", "#42A5F5",
            "#26A69A", "#03A9F4", "#00BCD4",
            "#F06292", "#FF8A65", "#FFB74D",
            "#26C6DA", "#4CAF50", "#FFC107",
            "#FFCDD2", "#4FC3F7", "#4DB6AC",
            "#FF7043", "#64B5F6", "#F8BBD0",
            "#AED581", "#FF5722", "#43A047",
            "#EC407A", "#81C784", "#4DD0E1",
            "#FFE0B2", "#7E57C2", "#9575CD",
            "#C5CAE9", "#BA68C8", "#F44336",
            "#a0793d", "#2196F3", "#c8a165",
            "#DCB579", "#ffa54f", "#cd8500",
            "#b2beb5", "#b2beb5", "#77DD9911",
            "#77DD99", "#7d9182"
        )
        //randomly get colour
        randomColor = if (colorInt == 0) Color.parseColor(colourArray.random()) else colorInt
        window?.apply {
            statusBarColor = randomColor
            decorView.apply {
                post {
                    if (Build.VERSION.SDK_INT > 22) systemUiVisibility =
                        systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
        Log.e("color", String.format("#%06X", (0xffffff and randomColor)))
        randomColor.also {
            window.statusBarColor = it
            convert_parent.setBackgroundColor(it)
            firstBox.boxStrokeColor = it
            secondBox.boxStrokeColor = it
            val colorStateList = ColorStateList.valueOf(it)
            top_button.apply {
                iconTint = colorStateList
                rippleColor = colorStateList
            }
            bottom_button.apply {
                iconTint = colorStateList
                rippleColor = colorStateList
            }
        }
    }

    private fun View.setTopPadding(padding: Int) {
        this.apply {
            setPadding(
                padding.dpToInt(this@ConvertActivity),
                paddingTop,
                paddingRight,
                paddingBottom
            )
        }
    }
}
