package com.example.unitconverter.functions

import android.util.Log
import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.constants.Temperature
import java.math.BigDecimal

class Temperature(positions: Positions) : ConstantsAbstractClass() {
    override val topPosition = positions.topPosition

    override val bottomPosition = positions.bottomPosition
    override val inputString = positions.input

    override fun getText(): String {
        Log.e("cel", "${celsiusConversions()}  $inputString")
        return celsiusConversions() ?: ""
    }

    private fun celsiusConversions(): String? {
        if (topPosition == 0 || bottomPosition == 0) {
            Temperature.apply {
                val pow = swapConversions()
                val powIsPositive = pow > -1
                if (topPosition == 1 || bottomPosition == 1) {
                    // to fahrenheit
                    val fixedValue =
                        if (powIsPositive) "-$celsiusToFahrenheitFixedValue" else celsiusToFahrenheitFixedValue
                    constant = celsiusToFahrenheitConstant
                    return complexConversionFunction(BigDecimal(inputString), fixedValue, pow)
                }
            }
        }
        return null
    }

    /*private fun celsiusToRankine(): String {
        val pow = swapConversions()
        //.//Temperature.
        val fixedValue =
            if (pow == -1) "-${Temperature.celsiusToDelisleFixedValue}" else fixedValueInt
        return complexConversionFunction("-$x", fixedValue, pow)
    }*/
}