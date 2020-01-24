package com.example.unitconverter.conversionClasses

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

interface RecyclerDataInterface {
    val yotta get() = getString(R.string.yotta)
    val zetta get() = getString(R.string.zetta)
    val zepto get() = getString(R.string.zepto)
    val yocto get() = getString(R.string.yocto)
    val exa get() = getString(R.string.exa)
    val peta get() = getString(R.string.peta)
    val tera get() = getString(R.string.tera)
    val giga get() = getString(R.string.giga)
    val mega get() = getString(R.string.mega)
    val kilo get() = getString(R.string.kilo)
    val hecto get() = getString(R.string.hecto)
    val deca get() = getString(R.string.deca)
    val deci get() = getString(R.string.deci)
    val centi get() = getString(R.string.centi)
    val milli get() = getString(R.string.milli)
    val micro get() = getString(R.string.micro)
    val nano get() = getString(R.string.nano)
    val pico get() = getString(R.string.pico)
    val femto get() = getString(R.string.femto)
    val atto get() = getString(R.string.atto)
    val exaSymbol get() = getString(R.string.exa_symbol)
    val petaSymbol get() = getString(R.string.peta_symbol)
    val teraSymbol get() = getString(R.string.tera_symbol)
    val gigaSymbol get() = getString(R.string.giga_symbol)
    val megaSymbol get() = getString(R.string.mega_symbol)
    val kiloSymbol get() = getString(R.string.kilo_symbol)
    val hectoSymbol get() = getString(R.string.hecto_symbol)
    val decaSymbol get() = getString(R.string.deca_symbol)
    val deciSymbol get() = getString(R.string.deci_symbol)
    val centiSymbol get() = getString(R.string.centi_symbol)
    val milliSymbol get() = getString(R.string.milli_symbol)
    val microSymbol get() = getString(R.string.micro_symbol)
    val nanoSymbol get() = getString(R.string.nano_symbol)
    val picoSymbol get() = getString(R.string.pico_symbol)
    val femtoSymbol get() = getString(R.string.femto_symbol)
    val attoSymbol get() = getString(R.string.atto_symbol)
    val yottaSymbol get() = getString(R.string.yotta_symbol)
    val zettaSymbol get() = getString(R.string.zetta_symbol)
    val zeptoSymbol get() = getString(R.string.zepto_symbol)
    val yoctoSymbol get() = getString(R.string.yocto_symbol)
    //
    val context: Context

    fun getList(): MutableList<RecyclerDataClass>

    fun getString(stringId: Int) = context.resources.getString(stringId)

    var start: Int

    fun massPrefixes(quantity: String = "", unit: String = ""): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(exa + quantity, exaSymbol + unit)
            add(peta + quantity, petaSymbol + unit)
            add(tera + quantity, teraSymbol + unit)
            add(giga + quantity, gigaSymbol + unit)
            add(mega + quantity, megaSymbol + unit)
            add(kilo + quantity, kiloSymbol + unit)
            add(hecto + quantity, hectoSymbol + unit)
            add(deca + quantity, decaSymbol + unit)
            add(deci + quantity, deciSymbol + unit)
            add(centi + quantity, centiSymbol + unit)
            add(milli + quantity, milliSymbol + unit)
            add(micro + quantity, microSymbol + unit)
            add(nano + quantity, nanoSymbol + unit)
            add(pico + quantity, picoSymbol + unit)
            add(femto + quantity, femtoSymbol + unit)
            add(atto + quantity, attoSymbol + unit)
        }
    }

    fun MutableList<RecyclerDataClass>.add(quantity: Int, unit: Int) =
        add(RecyclerDataClass(getString(quantity), getString(unit), start++))

    fun MutableList<RecyclerDataClass>.add(quantity: String, unit: String) =
        add(RecyclerDataClass(quantity, unit, start++))
}
