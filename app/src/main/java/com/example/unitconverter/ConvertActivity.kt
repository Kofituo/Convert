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
import android.text.Html
import android.text.SpannedString
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
import androidx.lifecycle.ViewModelProvider
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
import com.example.unitconverter.functions.*
import com.example.unitconverter.miscellaneous.isNull
import com.example.unitconverter.subclasses.ConvertViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_convert.*
import java.text.DecimalFormat
import java.util.*

class ConvertActivity : AppCompatActivity(), ConvertFragment.ConvertDialogInterface {

    private var swap = false
    private var randomColor = -1
    private var viewId = -1
    private lateinit var dialog: ConvertFragment
    private var isPrefix = false
    private val bundle = Bundle()
    private lateinit var viewName: String
    lateinit var function: (Positions) -> String


    private val groupingSeparator
        get() =
            (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat)
                .decimalFormatSymbols.groupingSeparator

    private val decimalSeparator
        get() =
            (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat)
                .decimalFormatSymbols.decimalSeparator

    private inline val isTemperature: Boolean get() = viewId == R.id.Temperature

    private lateinit var viewModel: ConvertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
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
                viewName = it
            }
            viewId = getIntExtra(ViewIdMessage, -1).apply {
                isPrefix = equals(R.id.prefixes)
                bundle.putInt("viewId", this)
            }
        }
        Log.e("id", "$viewId")

        firstEditText.apply {
            setFilters(this)
            setRawInputType(Configuration.KEYBOARD_12KEY)
        }
        secondEditText.apply {
            setFilters(this)
            setRawInputType(Configuration.KEYBOARD_12KEY)
        }

        getLastConversions()

        viewModel = ViewModelProvider(this)[ConvertViewModel::class.java] // for the view model
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
                    viewModel.whichButton = it.id
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
        bottom_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog.apply {
                    bundle.putInt("whichButton", it.id)
                    viewModel.whichButton = it.id
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
    }

    private lateinit var firstWatcher: CommonWatcher
    private lateinit var secondCommonWatcher: CommonWatcher

    private fun setFilters(editText: TextInputEditText) {
        editText.filters =
            if (viewId == R.id.Temperature)
                temperatureFilters(groupingSeparator, decimalSeparator, editText)
            else filters(groupingSeparator, decimalSeparator, editText)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        firstEditText?.text = null
        secondEditText?.text = null
        setFilters(firstEditText)
        setFilters(secondEditText)
    }

    override fun texts(text: String, unit: CharSequence) {
        val whichButton = viewModel.whichButton
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
                    this.text = if (unit is SpannedString) unit else unit
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

            R.id.Area -> areaConversions()

            R.id.Mass -> massConversions()

            R.id.Volume -> volumeConversions()

            R.id.Length -> lengthConversions()

            R.id.Angle -> angleConversions()

            R.id.Pressure -> pressureConversions()

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

    private fun areaConversions() {
        function = {
            Area(it).getText()
        }
    }

    private fun volumeConversions() {
        function = {
            Volume(it).getText()
        }
    }

    private fun prefixConversions() {
        function = {
            Prefixes(it).getText()
        }
    }

    private fun angleConversions() {
        function = {
            Angle(it).getText()
        }
    }

    private fun lengthConversions() {
        function = {
            Length(it).getText()
        }
    }

    private fun massConversions() {
        function = {
            Mass(it).getText()
        }
    }

    private fun temperatureConversions() {
        function = {
            Temperature(it).getText()
        }
    }

    private fun pressureConversions() {
        function = {
            Pressure(it).getText()
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

    private fun settingColours(colorInt: Int) {
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
            "#77DD99", "#7d9182", "#FED766",
            "#F6ABB6", "#EEC9D2", "#FE8A71",
            "#F6CD61", "#FEB2A8", "#7BC043",
            "#DFA290", "#88D8B0", "#BE9B7B",
            "#DCEDC1", "#00B159", "#FF77AA",
            "#696969"
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

    //could have used reflection
    private inline fun callBack(f: (Positions) -> String, x: String): String {
        return if (x.isEmpty()) ""
        else {
            val getPosition = getPositions()
            if (getPosition.isNull()) x.insertCommas()
            else if (!getPosition) ""
            else f(Positions(topPosition, bottomPosition, x))
        }
    }

    inner class CommonWatcher(editText: EditText, private val secondEditText: TextInputEditText) :
        SeparateThousands(editText, groupingSeparator, decimalSeparator) {
        override fun afterTextChanged(s: Editable?) {
            val start = System.currentTimeMillis()
            Log.e("came", "$s   ${secondEditText.text}")
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
                    var k = 0
                    for (i in secondEditText.text!!) if (i.isDigit()) k++
                    Log.e(
                        "finish",
                        "${System.currentTimeMillis() - start} ${secondEditText.text} ${secondEditText.text?.length}" +
                                " len $k"
                    )
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
        sharedPreferences = getSharedPreferences(pkgName + viewName, Context.MODE_PRIVATE)

        sharedPreferences.apply {
            topEditTextText = getString("topEditTextText", null)
            bottomEditTextText = getString("bottomEditTextText", null)
            getString("topTextViewText", null).apply {
                topTextView.text =
                    if (getBoolean("topIsSpans", false))
                        if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this).trim()
                        else Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).trim()
                    else this
            }

            getString("bottomTextViewText", null).apply {
                bottomTextView.text =
                    if (getBoolean("bottomIsSpans", false))
                        if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this).trim()
                        else Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).trim()
                    else this
            }
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
                val topTextViewText = topTextView.text

                putString(
                    "topTextViewText",
                    if (topTextViewText is SpannedString) {
                        if (Build.VERSION.SDK_INT < 24) Html.toHtml(topTextViewText)
                        else Html.toHtml(topTextViewText, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
                    } else topTextViewText.toString()
                )
                putBoolean("topIsSpans", topTextViewText is SpannedString)

                val bottomTextViewText = bottomTextView.text
                putString(
                    "bottomTextViewText",
                    if (bottomTextViewText is SpannedString) {
                        if (Build.VERSION.SDK_INT < 24) Html.toHtml(bottomTextViewText)
                        else
                            Html.toHtml(bottomTextViewText, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
                    } else bottomTextViewText.toString()
                )
                putBoolean("bottomIsSpans", bottomTextViewText is SpannedString)

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