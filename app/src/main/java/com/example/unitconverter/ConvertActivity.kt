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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.example.unitconverter.subclasses.MESSAGE
import kotlinx.android.synthetic.main.activity_convert.*
import java.util.*

class ConvertActivity : AppCompatActivity() {

    private var swap = false

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

        val isRTL =
            TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
        if (!isRTL) {
            bottom_button.apply {
                setPadding(
                    (-3).dpToInt(this@ConvertActivity),
                    paddingTop,
                    paddingRight,
                    paddingBottom
                )
            }
            top_button.apply {
                setPadding(
                    (-3).dpToInt(this@ConvertActivity),
                    paddingTop,
                    paddingRight,
                    paddingBottom
                )
            }
        }
        // for setting the text
        intent.getStringExtra(MESSAGE)?.also {
            convert_header?.text = it
            app_bar_text.text = it
        }
        settingColours()
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

    fun test1(view: View) {
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.convert_menu, menu)
        return true
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

    private fun settingColours() {
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
        val randomColor = Color.parseColor(colourArray.random())
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

    private fun createPop() {

    }

    /*private fun isRtl (locale: Locale) : Boolean {
        val a = Character.getDirectionality(locale.displayName.toCharArray()[0])
        return a == Character.DIRECTIONALITY_RIGHT_TO_LEFT || a == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
        030303
    }*/
}
