package com.example.unitconverter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.TextMessage
import com.example.unitconverter.subclasses.ViewIdMessage
import kotlinx.android.synthetic.main.activity_convert.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ConvertActivity : AppCompatActivity(), ConvertDialog.ConvertDialogInterface {
    private var swap = false
    private var randomColor = -1
    private var viewId = -1
    private lateinit var dialog: ConvertDialog


    private var isPrefix = false
    private val bundle = Bundle()

    lateinit var funtion: (String) -> String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        secondEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        firstEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        getLastConversions()
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
                    Log.e("sda", "556")
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
        positionArray[positionKey] = position
    }

    private fun whichView() {
        when (viewId) {
            R.id.Temperature -> {
            }
            R.id.Area -> {
            }
            R.id.Mass -> {
                funtion = {
                    if (positionArray.valueAt(0) == -1 || positionArray.valueAt(1) == -1) ""
                    else {
                        var topPosition = positionArray["topPosition"]
                        Log.e("sd", "$topPosition")
                        var bottomPosition = positionArray["bottomPosition"]
                        val sparseArray = SparseIntArray(31).apply {
                            append(0, 0)
                            append(1, 18)//exa
                            append(2, 15)//peta
                            append(3, 12)//tera
                            append(4, 9)//giga
                            append(5, 6)//mego
                            append(6, 3)//kilo
                            append(7, 2)//hecto
                            append(8, 1)//deca
                            append(9, -1)//deci
                            append(10, -2)//centi
                            append(11, -3)//milli
                            append(12, -6)//micro
                            append(13, -9)//nano
                            append(14, -12)//pico
                            append(15, -15)//femto
                            append(16, -18)//atto
                        }
                        if (topPosition in 0..17 && bottomPosition in 0..17) {
                            topPosition = sparseArray[topPosition!!]
                            bottomPosition = sparseArray[bottomPosition!!]
                            Log.e("top", "$topPosition   $bottomPosition")
                            com.example.unitconverter.funtions.Mass.top = topPosition
                            com.example.unitconverter.funtions.Mass.bottom = bottomPosition
                            com.example.unitconverter.funtions.Mass.prefixMultiplication(it)
                        } else
                            ""
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

    //private fun
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


    private fun callBack(f: (String) -> String, x: String): String {
        return f(x)
    }

    private fun getTextWhileTyping() {
        firstEditText.addTextChangedListener {
            Log.e("dadsd", callBack(funtion, it.toString()))
            secondEditText.setText(callBack(funtion, it.toString()))
            firstEditText.text = Editable.Factory.getInstance()
                .newEditable(it.toString().toBigDecimal().insertCommas())
        }
        secondEditText.addTextChangedListener {
        }
    }

    private fun getLastConversions() {
        val topEditTextText: String?
        val bottomEditTextText: String?
        val sharedPreferences = getSharedPreferences(pkgName + viewId, Context.MODE_PRIVATE)
        val topPosition: Int
        val bottomPosition: Int
        sharedPreferences?.apply {
            topEditTextText = getString("topEditTextText", null)
            bottomEditTextText = getString("bottomEditTextText", null)
            secondBox.hint = bottomEditTextText?.let {
                it
            } ?: resources.getString(R.string.select_unit)
            firstBox.hint = topEditTextText?.let {
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
        getSharedPreferences(pkgName + viewId, Context.MODE_PRIVATE).apply {
            with(edit()) {
                putString("topEditTextText", firstBox.hint.toString())
                putString("bottomEditTextText", secondBox.hint.toString())
                putInt("topPosition", positionArray["topPosition"]!!)
                putInt("bottomPosition", positionArray["bottomPosition"]!!)
                apply()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }
}

fun BigDecimal.insertCommas(): String {

    val decimalFormat =
        (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
            applyLocalizedPattern("#,##0.00####")
        }
    return decimalFormat.format(this)
}
