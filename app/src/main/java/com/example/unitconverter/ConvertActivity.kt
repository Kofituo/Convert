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
import android.view.*
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
import com.example.unitconverter.Utils.forEachIndexed
import com.example.unitconverter.Utils.insertCommas
import com.example.unitconverter.Utils.lengthFilter
import com.example.unitconverter.Utils.minusSign
import com.example.unitconverter.Utils.removeCommas
import com.example.unitconverter.Utils.temperatureFilters
import com.example.unitconverter.builders.buildConstraintSet
import com.example.unitconverter.builders.buildIntent
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.functions.*
import com.example.unitconverter.miscellaneous.*
import com.example.unitconverter.subclasses.Constraints
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.Positions
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
        intent {
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

        viewModel = viewModel {
            settingColours(randomInt)
            randomInt = randomColor
        }

        whichView()
        getTextWhileTyping()
        top_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog {
                    bundle.putInt("whichButton", it.id)
                    viewModel.whichButton = it.id
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
        bottom_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog {
                    bundle.putInt("whichButton", it.id)
                    viewModel.whichButton = it.id
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
    }

    private inline fun dialog(block: ConvertFragment.() -> Unit) =
        dialog.apply(block)

    private inline fun intent(block: Intent.() -> Unit) = intent.apply(block)

    private inline fun viewModel(block: ConvertViewModel.() -> Unit) =
        ViewModelProvider(this)[ConvertViewModel::class.java] // for the view model
            .apply(block)

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

    override fun texts(text: CharSequence, unit: CharSequence) {
        val whichButton = viewModel.whichButton
        if (whichButton == R.id.top_button) {
            if (firstBox.hint != text) {
                firstBox.hint = text
                topTextView.apply {
                    this.text = unit
                    layoutParams<ViewGroup.LayoutParams> {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        } else {
            if (secondBox.hint != text) {
                secondBox.hint = text
                bottomTextView.apply {
                    this.text = if (unit is SpannedString) unit else unit
                    layoutParams<ViewGroup.LayoutParams> {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
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
        // keep the edit text in focus text's constant

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

            R.id.Speed -> speedConversions()

            R.id.time -> timeConversions()

            R.id.fuelEconomy -> fuelEconomyConversions()

            R.id.dataStorage -> dataStorageConversion()

            R.id.electric_current -> currentConversions()

            R.id.luminance -> luminanceConversions()

            R.id.Illuminance -> illuminance()

            R.id.energy -> {
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
            R.id.cooking -> {
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

    private fun fuelEconomyConversions() {
        function = {
            FuelEconomy(it).getText()
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

    private fun speedConversions() {
        function = {
            Speed(it).getText()
        }
    }

    private fun timeConversions() {
        function = {
            Time(it).getText()
        }
    }

    private fun dataStorageConversion() {
        function = {
            Log.e("called", "called")
            DataStorage(it).apply { lazyMap = dataStorageMap }.getText()
        }
    }

    //to prevent unwanted initializations
    private val dataStorageMap = lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(35) {
            put(0, 0) //bits
            (27..34).forEachIndexed(1) { index, item -> // metric bits prefixes
                put(item, index)
                Log.e("size", "item $item  index $index")
            }
            (3..10).forEachIndexed(size) { index, item -> //other bits prefixes
                put(item, index)
            }
            put(1, size)//nibble
            put(2, size) // bytes
            (19..26).forEachIndexed(size) { index, item ->
                put(item, index)
            }
            (11..18).forEachIndexed(size) { index, item ->
                put(item, index)
            }
        }
    }

    private fun currentConversions() {
        function = {
            ElectricCurrent(it).getText()
        }
    }

    private fun luminanceConversions() {
        function = {
            Luminance(it).apply { lazyMap = luminanceMap }.getText()
        }
    }

    private val luminanceMap = lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(20) {
            (0..7).forEachIndexed { index, item ->
                put(item, index)
                Log.e("itemr", "($item  $index}")
            }
            (13..19).forEachIndexed(size) { index, item ->
                put(item, index)
            }
            (8..12).forEachIndexed(size) { index, item ->
                put(item, index)
            }
        }
    }

    private fun illuminance() {
        function = {
            Illuminance(it).getText()
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
                buildIntent<ConvertActivity>(this) {
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

    val layout = this
    private fun swap() {
        val firstBox = if (swap) R.id.secondBox else R.id.firstBox
        val secondBox = if (swap) R.id.firstBox else R.id.secondBox
        val topButton = if (swap) R.id.bottom_button else R.id.top_button
        val bottomButton = if (swap) R.id.top_button else R.id.bottom_button
        buildConstraintSet {
            this clones convert_inner
            constraint(firstBox) {
                val value = 40.dpToInt(this@ConvertActivity)
                margin(value) and it topToBottomOf secondBox
                this margin 0 and it bottomToBottomOf ConstraintSet.PARENT_ID
            }
            // for the second box
            constraint(secondBox) {
                it clear Constraints.BOTTOM
                this margin 0 and it topToTopOf R.id.topGuide
            }
            // for the top button
            constraint(topButton) {
                it topToTopOf firstBox
                it bottomToBottomOf firstBox
                it startToEndOf firstBox
            }
            //for the down button
            constraint(bottomButton) {
                it topToTopOf R.id.topGuide
                it bottomToBottomOf secondBox
                it startToEndOf secondBox
            }
            TransitionManager.beginDelayedTransition(convert_inner)
            this appliesTo convert_inner
        }
        swap = !swap
    }

    private fun settingColours(colorInt: Int) {
        //randomly get colour
        randomColor = if (colorInt == 0) Color.parseColor(colors.random()) else colorInt
        window {
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

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    private fun View.setTopPadding(padding: Int) {
        apply {
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
        sharedPreferences = getSharedPreferences(pkgName + viewName, Context.MODE_PRIVATE)
        sharedPreferences {
            get<String?>("topTextViewText") {
                topTextView.text =
                    if (get("topIsSpans"))
                        if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this).trim()
                        else Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).trim()
                    else this
            }

            get<String?>("bottomTextViewText") {
                bottomTextView.text =
                    if (get("bottomIsSpans"))
                        if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this).trim()
                        else Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).trim()
                    else this
            }
            secondBox.hint =
                get<String?>("bottomEditTextText") ?: resources.getString(R.string.select_unit)
            firstBox.hint =
                get<String?>("topEditTextText") ?: resources.getString(R.string.select_unit)

            //get last positions
            val topPosition = get<Int>("topPosition")
            val bottomPosition = get<Int>("downPosition")
            positionArray.apply {
                put("topPosition", topPosition)
                put("bottomPosition", bottomPosition)
            }
        }
    }

    private fun saveData() {
        editPreferences {
            put<String> {
                key = "topEditTextText"
                value = firstBox.hint.toString().let {
                    if (it != resources.getString(R.string.select_unit)) it
                    else null
                }
            }
            val topTextViewText = topTextView.text
            put<String> {
                key = "topTextViewText"
                value = topTextViewText.let {
                    if (it is SpannedString) {
                        if (Build.VERSION.SDK_INT < 24) Html.toHtml(it)
                        else
                            Html.toHtml(it, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
                    } else it.toString()
                }
            }
            put<Boolean> {
                key = "topIsSpans"
                value = topTextViewText is SpannedString
            }

            val bottomTextViewText = bottomTextView.text
            put<String> {
                key = "bottomTextViewText"
                value = bottomTextViewText.let {
                    if (it is SpannedString) {
                        if (Build.VERSION.SDK_INT < 24) Html.toHtml(it)
                        else
                            Html.toHtml(it, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
                    } else it.toString()
                }
            }
            put<Boolean> {
                key = "bottomIsSpans"
                value = bottomTextViewText is SpannedString
            }
            put<String> {
                key = "bottomEditTextText"
                value = secondBox.hint.toString().let {
                    if (it != resources.getString(R.string.select_unit)) it
                    else null
                }
            }
            put<Int> {
                key = "topPosition"
                value = positionArray["topPosition"]!!
            }
            put<Int> {
                key = "downPosition"
                value = positionArray["bottomPosition"]!!
            }
            apply()
        }
    }

    private inline fun sharedPreferences(block: SharedPreferences.() -> Unit): SharedPreferences =
        sharedPreferences(sharedPreferences, block)

    private inline fun editPreferences(block: SharedPreferences.Editor.() -> Unit) =
        editPreferences(sharedPreferences, block)

    override fun onPause() {
        super.onPause()
        saveData()
    }
}