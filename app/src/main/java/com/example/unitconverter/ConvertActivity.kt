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
import android.text.InputFilter
import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import android.util.SparseIntArray
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.example.unitconverter.Utils.dpToInt
import com.example.unitconverter.Utils.insertCommas
import com.example.unitconverter.Utils.removeCommas
import com.example.unitconverter.funtions.Mass
import com.example.unitconverter.funtions.Mass.buildPrefixMass
import com.example.unitconverter.funtions.Prefixes
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.TextMessage
import com.example.unitconverter.subclasses.ViewIdMessage
import kotlinx.android.synthetic.main.activity_convert.*
import java.text.DecimalFormat
import java.util.*
import kotlin.properties.Delegates

class ConvertActivity : AppCompatActivity(), ConvertDialog.ConvertDialogInterface {
    private var swap = false
    private var randomColor = -1
    private var viewId = -1
    private lateinit var dialog: ConvertDialog
    private var isPrefix = false
    private val bundle = Bundle()
    lateinit var function: (String) -> String
    var groupingSeparator by Delegates.notNull<Char>()
    var decimalSeparator by Delegates.notNull<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar)
        supportActionBar?.apply {

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        setSeparators()
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val stringBuilder = StringBuilder(end - start)
            var count = 0
            for (i in start until end) {
                if (source[i].isDigit() ||
                    source[i] == groupingSeparator ||
                    source[i] == decimalSeparator
                ) {
                    if (source[i] == decimalSeparator) {
                        count++
                        if (count >= 2) continue
                    }
                    stringBuilder.append(source[i])
                }
            }
            stringBuilder
        }
        firstEditText.apply {
            setRawInputType(Configuration.KEYBOARD_12KEY)
            filters = arrayOf(filter)
        }
        secondEditText.apply {
            filters = arrayOf(filter)
            setRawInputType(Configuration.KEYBOARD_12KEY)
        }
        val isRTL =
            TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
        if (!isRTL) {
            bottom_button.setTopPadding(-3) //converts it to dp
            top_button.setTopPadding(-3)
        }
        dialog = ConvertDialog()
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

        getLastConversions()
        ViewModelProviders.of(this)[ConvertViewModel::class.java] // for the view model
            .apply {
                settingColours(randomInt)
                randomInt = randomColor
            }

        whichView()
        getTextWhileTyping()
        top_button.setOnClickListener {
            dialog.apply {
                bundle.putInt("whichButton", it.id)
                arguments = bundle
                show(supportFragmentManager, "dialog")
            }
        }
        bottom_button.setOnClickListener {
            bundle.putInt("whichButton", it.id)
            dialog.apply {
                arguments = bundle
                show(supportFragmentManager, "dialog")
            }
        }
    }

    lateinit var firstWatcher: CommonWatcher
    lateinit var secondCommonWatcher: CommonWatcher
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
        //Log.e("bool", "$firstBoolean  $secondBoolean")
        if (positionKey == "topPosition") {
            //keep the top constant
            firstEditText.apply {
                val initialText = Editable.Factory.getInstance().newEditable(text)
                text = null
                if (!firstBoolean) {
                    secondEditText.removeTextChangedListener(secondCommonWatcher)
                    addTextChangedListener(firstWatcher)
                    reverse = false
                    firstBoolean = true
                    secondBoolean = false
                }
                text = initialText
            }
        } else {
            //keep bottom constant
            secondEditText.apply {
                val initialText = Editable.Factory.getInstance().newEditable(text)
                text = null
                if (!secondBoolean) {
                    firstEditText.removeTextChangedListener(firstWatcher)
                    firstBoolean = false
                    secondBoolean = true
                    reverse = true
                    addTextChangedListener(secondCommonWatcher)
                }
                text = initialText
            }
        }
    }

    var topPosition: Int = 0
    var bottomPosition: Int = 0
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
            R.id.prefixes -> {
                function = { string ->
                    val getPosition = getPositions()

                    if (getPosition == null) string.insertCommas()
                    else if (!getPosition) ""
                    else {
                        val sparseIntArray = Prefixes.buildPrefix()
                        topPosition = sparseIntArray[topPosition]
                        bottomPosition = sparseIntArray[bottomPosition]
                        Prefixes.top = topPosition
                        Prefixes.bottom = bottomPosition
                        Prefixes.prefixMultiplication(string)
                    }
                }
            }
            R.id.Temperature -> {
            }
            R.id.Area -> {
            }
            R.id.Mass -> {
                function = { string ->
                    val getPosition = getPositions()
                    if (getPosition == null) string.insertCommas()
                    else if (!getPosition) ""
                    else {
                        val sparseArray = buildPrefixMass()
                        // get which one
                        //with elvis operator
                        amongGram(string, sparseArray) ?: poundConversions(string, sparseArray)
                        ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: ""
                        ?: "" ?: "" ?: ""
                        ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: ""
                        ?: "" ?: "" ?: "" ?: "" ?: ""
                        ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: "" ?: ""
                        ?: "" ?: "" ?: ""
                    }
                }
            }
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

    private fun amongGram(x: String, sparseArray: SparseIntArray): String? {
        //val sparseArray = buildPrefixMass()
        // means its amongst the gram family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            topPosition = sparseArray[topPosition]
            bottomPosition = sparseArray[bottomPosition]
            Log.e("top", "$topPosition   $bottomPosition")
            Mass.top = topPosition
            Mass.bottom = bottomPosition
            return Mass.prefixMultiplication(x)
        }
        return null
    }

    //it works like a charm
    private fun poundConversions(x: String, sparseArray: SparseIntArray): String? {
        //val sparseArray = buildPrefixMass()
        if (topPosition == 17 || bottomPosition == 17) {
            if (topPosition in 0..16 || bottomPosition in 0..16) {
                // g to lb or vice versa
                //Log.e("pos","$topPosition  $bottomPosition")
                //to prevent double calling
                val temp = sparseArray[topPosition, -2]
                val whichOne =
                    if (temp == -2) sparseArray[bottomPosition] else temp
                Mass.top = whichOne
                Mass.bottom = sparseArray[6]
                //Log.e("which","$whichOne  ${Mass.bottom} $reverse")
                val pow = if (topPosition > bottomPosition) 1 else -1
                //Log.e("pow","$pow   $reverse")
                return Mass.somethingGramToPound(x, pow)
            }
        }
        return null
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
        return if (x.isEmpty()) "" else f(x)
    }

    inner class CommonWatcher(editText: EditText, private val secondEditText: EditText) :
        SeparateThousands(editText, groupingSeparator, decimalSeparator) {
        private var t = ""
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)

            s?.toString()?.apply {
                this.removeCommas(decimalSeparator)?.also {
                    if (t == it && t.isNotEmpty()) return
                    secondEditText.setText(callBack(function, it))
                    t = it
                }
            }
        }
    }

    private var firstBoolean = false
    private var secondBoolean = false
    private fun getTextWhileTyping() {
        firstEditText.apply {
            firstWatcher = CommonWatcher(this, secondEditText)
            setOnTouchListener { _, event ->
                Log.e("pp", "$firstBoolean  ${event.actionMasked}")
                if (!firstBoolean && event.actionMasked == MotionEvent.ACTION_UP) {
                    secondEditText.removeTextChangedListener(secondCommonWatcher)
                    addTextChangedListener(firstWatcher)
                    reverse = false
                    firstBoolean = true
                    secondBoolean = false
                }
                super.onTouchEvent(event)
            }
        }
        secondEditText.apply {
            secondCommonWatcher = CommonWatcher(this, firstEditText)
            setOnTouchListener { _, event ->
                Log.e("hmm", "$secondBoolean   ${event.actionMasked}")
                if (!secondBoolean && event.actionMasked == MotionEvent.ACTION_UP) {
                    firstEditText.removeTextChangedListener(firstWatcher)
                    addTextChangedListener(secondCommonWatcher)
                    firstBoolean = false
                    secondBoolean = true
                    reverse = true
                }
                super.onTouchEvent(event)
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