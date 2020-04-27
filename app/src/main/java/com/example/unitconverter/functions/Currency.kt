package com.example.unitconverter.functions

import android.util.Log
import com.example.unitconverter.constants.BigDecimalsAddOns
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Currency : ConstantsAbstractClass() {

    public override lateinit var positions: Positions
    var enumerationRates: Map<Int, String>? = null
    var ratesMap: Map<String, String>? = null

    companion object {
        inline fun buildConversions(block: Currency.() -> Unit) =
            Currency().apply(block)
    }

    override fun getText(): String {
        val topCurrency = enumerationRates?.get(positions.topPosition) ?: return ""
        val bottomCurrency = enumerationRates?.get(positions.bottomPosition) ?: return ""
        val topValue = ratesMap?.get(topCurrency) ?: return ""
        val bottomValue = ratesMap?.get(bottomCurrency) ?: return ""
        //Log.e("val", "$topCurrency  $bottomCurrency")
        //Log.e("val", "$topValue  $bottomValue")
        ratio = BigDecimal(topValue).divide(BigDecimal(bottomValue), BigDecimalsAddOns.mathContext)
        //Log.e("ratio", "$ratio  ${basicFunction(1)}")
        return basicFunction(1)
    }
}