package com.example.unitconverter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.example.unitconverter.AdditionItems.TextMessage
import com.example.unitconverter.AdditionItems.ViewIdMessage
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.Utils.dpToInt
import com.example.unitconverter.Utils.filters
import com.example.unitconverter.Utils.insertCommas
import com.example.unitconverter.Utils.lengthFilter
import com.example.unitconverter.Utils.minusSign
import com.example.unitconverter.Utils.removeCommas
import com.example.unitconverter.Utils.temperatureFilters
import com.example.unitconverter.constants.Prefixes
import com.example.unitconverter.functions.Mass
import com.example.unitconverter.functions.Temperature
import com.example.unitconverter.subclasses.ConvertViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_convert.*
import java.text.DecimalFormat
import java.util.*
import kotlin.properties.Delegates

class ConvertActivity : AppCompatActivity(), ConvertFragment.ConvertDialogInterface {
    private var swap = false
    private var randomColor = -1
    private var viewId = -1
    private lateinit var dialog: ConvertFragment
    private var isPrefix = false
    private val bundle = Bundle()
    lateinit var function: (String) -> String
    private var groupingSeparator by Delegates.notNull<Char>()
    private var decimalSeparator by Delegates.notNull<Char>()
    private val isTemperature: Boolean get() = viewId == R.id.Temperature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        setSeparators()

        val isRTL =
            TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
        if (!isRTL) {
            bottom_button.setTopPadding(-3) //converts it to dp
            top_button.setTopPadding(-3)
        }
        dialog = ConvertFragment()
        // for setting the text
        intent.apply {
            getStringExtra(TextMessage)?.also {
                bundle.putString("viewName", it)
                convert_header?.text = it
                app_bar_text.text = it
            }
            viewId = getIntExtra(ViewIdMessage, -1).apply {
                isPrefix = equals(R.id.prefixes)
                bundle.putInt("viewId", this)
            }
        }

        Log.e("view", "${viewId == R.id.Temperature}")
        firstEditText.apply {
            filters =
                if (viewId == R.id.Temperature)
                    temperatureFilters(groupingSeparator, decimalSeparator, this)
                else filters(groupingSeparator, decimalSeparator, this)

            setRawInputType(Configuration.KEYBOARD_12KEY)
        }
        secondEditText.apply {
            filters =
                if (viewId == R.id.Temperature)
                    temperatureFilters(groupingSeparator, decimalSeparator, this)
                else filters(groupingSeparator, decimalSeparator, this)

            setRawInputType(Configuration.KEYBOARD_12KEY)
        }

        getLastConversions()
        ViewModelProviders.of(this)[ConvertViewModel::class.java] // for the view model
            .apply {
                settingColours(randomInt)
                randomInt = randomColor
            }

        whichView()
        getTextWhileTyping()
        top_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog.apply {
                    bundle.putInt("whichButton", it.id)
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
        bottom_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog.apply {
                    bundle.putInt("whichButton", it.id)
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
    }


    private lateinit var firstWatcher: CommonWatcher
    private lateinit var secondCommonWatcher: CommonWatcher
    private fun setSeparators() {
        groupingSeparator =
            (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat).decimalFormatSymbols.groupingSeparator
        decimalSeparator =
            (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat).decimalFormatSymbols.decimalSeparator
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setSeparators()
    }

    override fun texts(text: String, unit: String) {
        val whichButton = bundle.getInt("whichButton")
        if (whichButton == R.id.top_button) {
            if (firstBox.hint != text) {
                firstBox.hint = text
                topTextView.apply {
                    this.text = unit
                    val params = layoutParams as ViewGroup.LayoutParams
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams = params
                }
            }
        } else {
            if (secondBox.hint != text) {
                secondBox.hint = text
                bottomTextView.apply {
                    this.text = unit
                    val params = layoutParams as ViewGroup.LayoutParams
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams = params
                }
            }

        }
    }

    private val positionArray = ArrayMap<String, Int>(2)

    override fun getOtherValues(position: Int, positionKey: String) {
        val initialMap = ArrayMap(positionArray)
        positionArray[positionKey] = position
        Log.e("pos", "$positionArray")
        if (initialMap == positionArray) return
        val editTextInFocus: EditText
        val editTextNotInFocus: EditText
        val editTextInFocusBoolean: Boolean
        val editTextNotInFocusBoolean: Boolean
        val editTextInFocusWatcher: CommonWatcher
        val editTextNotInFocusWatcher: CommonWatcher
        //initialize variables
        if (firstEditText.isFocused) {
            editTextInFocus = firstEditText
            editTextInFocus.text?.apply { if (isEmpty()) return }
            editTextNotInFocus = secondEditText
            editTextInFocusBoolean = firstBoolean
            editTextNotInFocusBoolean = secondBoolean
            editTextInFocusWatcher = firstWatcher
            editTextNotInFocusWatcher = secondCommonWatcher
        } else {
            editTextInFocus = secondEditText
            editTextInFocus.text?.apply { if (isEmpty()) return }
            editTextNotInFocus = firstEditText
            editTextInFocusBoolean = secondBoolean
            editTextNotInFocusBoolean = firstBoolean
            editTextInFocusWatcher = secondCommonWatcher
            editTextNotInFocusWatcher = firstWatcher
        }
        val initialReverse = if (reverse) 1 else 0
        // keep the text in focused edit text constant
        editTextInFocus.apply {
            //initial text
            val initialText = Editable.Factory.getInstance().newEditable(text)
            text = null
            if (!editTextInFocusBoolean) {
                editTextNotInFocus.removeTextChangedListener(editTextNotInFocusWatcher)
                addTextChangedListener(editTextInFocusWatcher)
                reverse = this != firstEditText
            }
            text = initialText
            //settings thing back
            if (editTextNotInFocusBoolean)
                editTextNotInFocus.addTextChangedListener(editTextNotInFocusWatcher)
            if (!editTextInFocusBoolean) removeTextChangedListener(editTextInFocusWatcher)
            reverse = initialReverse != 0
        }
    }

    private var topPosition: Int = 0
    private var bottomPosition: Int = 0
    private fun getPositions(): Boolean? {
        if (positionArray.valueAt(0) == -1 ||
            positionArray.valueAt(1) == -1
        ) return false

        topPosition =
            if (reverse) positionArray["bottomPosition"]!! else positionArray["topPosition"]!!
        bottomPosition =
            if (reverse) positionArray["topPosition"]!! else positionArray["bottomPosition"]!!
        if (topPosition == bottomPosition) return null

        return true
    }

    private var reverse = false

    data class Positions(val topPosition: Int, val bottomPosition: Int, val input: String)

    private fun whichView() {
        when (viewId) {
            R.id.prefixes -> prefixConversions()
            R.id.Temperature -> temperatureConversions()
            R.id.Area -> {
            }
            R.id.Mass -> massConversions()
            R.id.Volume -> {
            }
            R.id.Length -> {
            }
            R.id.Angle -> {
            }
            R.id.Pressure -> {
            }
            R.id.Speed -> {
            }
            R.id.time -> {
            }
            R.id.fuelEconomy -> {
            }
            R.id.dataStorage -> {
            }
            R.id.concentration -> {
            }
            R.id.luminance -> {
            }
            R.id.cooking -> {
            }
            R.id.capacitance -> {
            }
            R.id.Currency -> {
            }
            R.id.heatCapacity -> {
            }
            R.id.Angular_Velocity -> {
            }
            R.id.angularAcceleration -> {
            }
            R.id.sound -> {
            }
            R.id.resistance -> {
            }
            R.id.radioactivity -> {
            }
            R.id.resolution -> {
            }
            R.id.Illuminance -> {
            }
            R.id.inductance -> {
            }
            R.id.flow -> {
            }
            R.id.number_base -> {
            }
        }
    }

    private fun prefixConversions() {
        function = { string ->
            val sparseIntArray = Prefixes.buildPrefix()
            topPosition = sparseIntArray[topPosition]
            bottomPosition = sparseIntArray[bottomPosition]
            Prefixes.top = topPosition
            Prefixes.bottom = bottomPosition
            Prefixes.prefixMultiplication(string)
        }
    }

    private fun massConversions() {
        function = { string ->
            /**@Deprecated
            val getPosition = getPositions()
            if (getPosition == null) string.insertCommas()
            else if (!getPosition) ""
            else {
            // get which one
            //with elvis operator
            /*amongGram(string) ?: poundConversions(string)
            ?: gramConversions(string) ?: ounceConversions(string)
            ?: metricTonConversions(string) ?: shortTonConversions(string)
            ?: longTonConversions(string) ?: caratConversions(string)
            ?: grainConversions(string) ?: troyPoundConversion(string)
            ?: troyOunceConversions(string)
            ?: ""*/
             */
            val positions = Positions(topPosition, bottomPosition, string)
            Mass(positions).getText()
        }
    }

    private fun temperatureConversions() {
        function = { string ->
            val positions = Positions(topPosition, bottomPosition, string)
            Temperature(positions).getText()
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
            R.id.prefixes -> {
                Intent(this, ConvertActivity::class.java).apply {
                    putExtra(TextMessage, "Prefix")
                    putExtra(ViewIdMessage, R.id.prefixes)
                    startActivity(this)
                }
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

    private inline fun callBack(f: (String) -> String, x: String): String {
        return if (x.isEmpty()) "" else {
            val getPosition = getPositions()
            if (getPosition == null) x.insertCommas()
            else if (!getPosition) ""
            else f(x)
        }
    }

    inner class CommonWatcher(editText: EditText, private val secondEditText: TextInputEditText) :
        SeparateThousands(editText, groupingSeparator, decimalSeparator) {
        override fun afterTextChanged(s: Editable?) {
            val start = System.currentTimeMillis()
            Log.e("came", "$s")
            super.afterTextChanged(s)
            s?.toString()?.apply {
                if (s.length == 1 && s[0] == minusSign) {
                    secondEditText.text = null // to prevent Unparseable number "-"error
                    return
                }
                this.removeCommas(decimalSeparator)?.also {
                    Log.e("may be Problem", it)
                    secondEditText.apply {
                        if (isTemperature) filters = arrayOf(lengthFilter())

                        setText(callBack(function, it))
                        if (isTemperature)
                            filters = temperatureFilters(groupingSeparator, decimalSeparator, this)
                    }
                    Log.e("finish", "${System.currentTimeMillis() - start} ${secondEditText.text}")
                }
            }
        }
    }

    private var firstBoolean = false
    private var secondBoolean = false
    private fun getTextWhileTyping() {
        firstEditText.apply {
            firstWatcher = CommonWatcher(this, secondEditText)
            setOnFocusChangeListener { _, hasFocus ->
                firstBoolean = hasFocus
                //Log.e("3", "3 firstHasFocus $firstBoolean  secondHasFocus $secondBoolean")
                if (hasFocus) {
                    ///Log.e("pp", "$firstBoolean ")
                    addTextChangedListener(firstWatcher)
                    secondEditText.removeTextChangedListener(secondCommonWatcher)
                    reverse = false
                }
            }
        }
        secondEditText.apply {
            secondCommonWatcher = CommonWatcher(this, firstEditText)
            setOnFocusChangeListener { _, hasFocus ->
                secondBoolean = hasFocus
                //Log.e("4", "4 firstHasFocus $firstBoolean  secondHasFocus $secondBoolean")
                if (hasFocus) {
                    addTextChangedListener(secondCommonWatcher)
                    firstEditText.removeTextChangedListener(firstWatcher)
                    reverse = true
                }
            }
        }
    }

    private lateinit var sharedPreferences: SharedPreferences

    private fun getLastConversions() {
        val topEditTextText: String?
        val bottomEditTextText: String?
        val topPosition: Int
        val bottomPosition: Int
        sharedPreferences = getSharedPreferences(pkgName + viewId, Context.MODE_PRIVATE)

        sharedPreferences.apply {
            topEditTextText = getString("topEditTextText", null)
            bottomEditTextText = getString("bottomEditTextText", null)
            topTextView.text = getString("topTextViewText", null)
            bottomTextView.text = getString("bottomTextViewText", null)
            secondBox.hint = bottomEditTextText?.let {
                it
            } ?: resources.getString(R.string.select_unit)
            firstBox.hint = topEditTextText?.let {
                Log.e("s1", it)
                it
            } ?: resources.getString(R.string.select_unit)

            //get last positions
            topPosition = getInt("topPosition", -1)
            bottomPosition = getInt("downPosition", -1)
            positionArray.apply {
                put("topPosition", topPosition)
                put("bottomPosition", bottomPosition)
            }
        }
    }

    private fun saveData() {
        sharedPreferences.apply {
            with(edit()) {
                putString(
                    "topEditTextText",
                    if (firstBox.hint.toString() != resources.getString(R.string.select_unit)) firstBox.hint.toString() else null
                )
                putString("topTextViewText", topTextView.text.toString())
                putString("bottomTextViewText", bottomTextView.text.toString())
                putString(
                    "bottomEditTextText",
                    if (secondBox.hint.toString() != resources.getString(R.string.select_unit)) secondBox.hint.toString() else null
                )
                putInt("topPosition", positionArray["topPosition"]!!)
                putInt("downPosition", positionArray["bottomPosition"]!!)
                apply()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }
}
/*private fun amongGram(x: String): String? {
        //val sparseArray = buildPrefixMass()
        // means its amongst the gram family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Mass.apply {
                buildPrefixMass().also {
                    topPosition = it[topPosition]
                    bottomPosition = it[bottomPosition]
                    top = topPosition
                    bottom = bottomPosition
                    return prefixMultiplication(x)
                }
            }
        }
        return null
    }

    private fun gramConversions(x: String): String? {
        // among gram
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            Mass.apply {
                val pow: Int
                if (topPosition == 18 || bottomPosition == 18) {
                    //gram to ounce (oz) or vice versa
                    constant = gramToOunceConstant
                    pow = simplifyKgConversions()
                    return gramToOunce(x, pow)
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //gram to metric ton
                    gramToMetricTonConversion()
                    return prefixMultiplication(x)
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //gram to short Ton
                    constant = shortTonToKgConstant
                    pow = simplifyKgConversions()
                    return gramToShortTon(x, pow)
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //gram to long Ton
                    constant = gramToLonTonConstant
                    pow = simplifyKgConversions()
                    return gramToLongTon(x, pow)
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //gram to carat
                    constant = gramToCaratConstant
                    pow = simplifyKgConversions()
                    return gramToCarat(x, pow)
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToGramConstant
                    pow = simplifyKgConversions()
                    return grainToGram(x, pow)
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = gramToTroyPoundConstant
                    pow = simplifyKgConversions()
                    return gramToTroyPound(x, pow)
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToGramConstant
                    pow = simplifyKgConversions()
                    return troyOunceToGram(x, pow)
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToGramConstant
                    pow = simplifyKgConversions()
                    return pennyWeightToGram(x, pow)
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToGramConstant
                    pow = simplifyKgConversions()
                    return stoneToGram(x, pow)

                }
            }
        }
        return null
    }

    private fun gramToMetricTonConversion() {
        Mass.apply {
            buildPrefixMass().also {
                val temp = it[topPosition, -2]
                val metricTonPosition = it[5]
                //
                val whichOne =
                    if (temp == -2) it[bottomPosition] else temp

                if (topPosition > bottomPosition) {
                    top = metricTonPosition
                    bottom = whichOne
                } else {
                    top = whichOne
                    bottom = metricTonPosition
                }
            }
        }
    }

    private fun simplifyKgConversions(): Int {
        //to prevent double calling
        buildPrefixMass().also {
            val temp = it[topPosition, -2]
            val kgPosition = it[6]
            //which one is not kilogram??
            val whichOne =
                if (temp == -2) it[bottomPosition] else temp

            Mass.top = whichOne
            Mass.bottom = kgPosition
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun simplifyLbConversions() = if (topPosition > bottomPosition) 1 else -1
    //it works like a charm
    private fun poundConversions(x: String): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Mass.apply {
                if (topPosition in 0..16 || bottomPosition in 0..16) {
                    // g to lb or vice versa
                    constant = gramToPoundConstant
                    return somethingGramToPound(x, simplifyKgConversions())
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    // pound to ounce
                    constant = poundToOunceConstant
                    return poundToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //pound to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    constant = gramToPoundConstant.scaleByPowerOfTen(-3)
                    return poundToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //pound to short ton
                    constant = shortTonToPoundConstant
                    return poundToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //pound to long ton
                    constant = poundToLonTonConstant
                    return poundToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //pound to carat
                    constant = poundToCaratConstant
                    return poundToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToPoundConstant
                    return poundToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = troyPoundToPoundConstant
                    return troyPoundToPound(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToPoundConstant
                    return troyOunceToPound(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26 ) {
                    //to pennyWeight
                    constant = pennyWeightToPoundConstant
                    return pennyWeightToPound(x,simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27){
                    // to stone
                    constant = stoneToPoundConstant
                    return stoneToPound(x,simplifyLbConversions())

                }
            }
        }
        return null
    }

    private fun metricTonConversions(x: String): String? {
        if (topPosition == 19 || bottomPosition == 19) {
            Mass.apply {
                if (topPosition == 20 || bottomPosition == 20) {
                    //short ton to metric ton
                    constant = shortTonToMetricTonConstant
                    return shortTonToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    constant = metricTonToLonTonConstant
                    return metricTonToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //metric ton to carat
                    constant = metricTonToCaratConstant
                    return metricTonToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    // to grain
                    constant = grainToMetricTonConstant
                    return grainToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = metricTonTroyPoundConstant
                    return troyPoundToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToMetricTonConstant
                    return troyOunceToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition ==26) {
                    //to pennyWeight
                    constant= pennyWeightToMetricTonConstant
                    return pennyWeightToMetricTon(x,simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToMetricTonConstant
                    return stoneToMetricTon(x,simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun ounceConversions(x: String): String? {
        if (topPosition == 18 || bottomPosition == 18) {
            Mass.apply {
                if (topPosition == 19 || bottomPosition == 19) {
                    //ounce to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    constant = gramToOunceConstant.scaleByPowerOfTen(-3)
                    return ounceToMetricTon(x, simplifyLbConversions())
                }
                if (bottomPosition == 20 || topPosition == 20) {
                    //ounce to short ton
                    constant = ounceToShortTonConstant
                    return ounceToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //ounce to long ton
                    constant = ounceToLongTonConstant
                    return ounceToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //ounce to carat
                    constant = ounceToCaratConstant
                    return ounceToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToOunceConstant
                    return ounceToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy Pound
                    constant = troyPoundToOunceConstant
                    return troyPoundToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToOunceConstant
                    return troyOunceToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition== 26) {
                    //to pennyWeight
                    constant = pennyWeightToOunceConstant
                    return pennyWeightToOunce(x,simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToOunceConstant
                    return stoneToOunce(x,simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun shortTonConversions(x: String): String? {
        if (topPosition == 20 || bottomPosition == 20) {
            Mass.apply {
                if (topPosition == 21 || bottomPosition == 21) {
                    //short Ton to long ton
                    constant = shortTonToLongConstant
                    return shortTonToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    constant = shortTonToCaratConstant
                    return shortTonToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToShortTonConstant
                    return grainToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = shortTonToTroyPound
                    return troyPoundToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToShortTonConstant
                    return troyOunceToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyweight
                    constant= pennyWeightToShortTonConstant
                    return pennyWeightToShortTon(x,simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToShortTonConstant
                    return basicFunction(x,simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun longTonConversions(x: String): String? {
        if (topPosition == 21 || bottomPosition == 21) {
            Mass.apply {
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    constant = longTonToCaratConstant
                    return longTonToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToLongTonConstant
                    return grainToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to troy pound
                    constant = longTonToTroyPoundConstant
                    return longTonToTroyPound(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToLongTonConstant
                    return troyOunceToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToLongTonConstant
                    return pennyWeightToLongTon(x,simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToLonTonConstant
                    return basicFunction(x,simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun caratConversions(x: String): String? {
        if (topPosition == 22 || bottomPosition == 22) {
            Mass.apply {
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToCaratConstant
                    return grainToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = caratToTroyPoundConstant
                    return caratToTroyPound(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = caratToTroyOunceConstant
                    return troyOunceToCarat(x, simplifyLbConversions())
                }
                if (topPosition ==26 || bottomPosition ==26){
                    // to pennyWeight
                    constant = pennyWeightToCaratConstant
                    return pennyWeightToCarat(x,simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone

                }
            }
        }
        return null
    }

    private fun grainConversions(x: String): String? {
        if (topPosition == 23 || bottomPosition == 23) {
            Mass.apply {
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = grainToTroyPoundConstant
                    return troyPoundToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToGrainConstant
                    return troyOunceToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToGrainConstant
                    return pennyWeightToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to stone
                    constant = stoneToGrainConstant
                    return basicFunction(x,simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun troyPoundConversion(x: String): String? {
        if (topPosition == 24 || bottomPosition == 24) {
            Mass.apply {
                //troy pound
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToTroyPoundConstant
                    return troyOunceToTroyPound(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToTroyPoundConstant
                    return pennyWeightToTroyPound(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun troyOunceConversions(x: String): String? {
        if (topPosition == 25 || bottomPosition == 25) {
            Mass.apply {
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToTroyOunceConstant
                    return pennyWeightToTroyOunce(x, simplifyLbConversions())
                }
            }
        }
        return null
    }
*/