package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.builders.buildMutableList
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class RecyclerDataAbstractClass {
    protected val locale: Locale get() = Locale.getDefault()

    protected val yotta get() = getString(R.string.yotta)
    protected val zetta get() = getString(R.string.zetta)
    protected val zepto get() = getString(R.string.zepto)
    protected val yocto get() = getString(R.string.yocto)
    protected val exa get() = getString(R.string.exa)
    protected val peta get() = getString(R.string.peta)
    protected val tera get() = getString(R.string.tera)
    protected val giga get() = getString(R.string.giga)
    protected val mega get() = getString(R.string.mega)
    protected val kilo get() = getString(R.string.kilo)
    protected val hecto get() = getString(R.string.hecto)
    protected val deca get() = getString(R.string.deca)
    protected val deci get() = getString(R.string.deci)
    protected val centi get() = getString(R.string.centi)
    protected val milli get() = getString(R.string.milli)
    protected val micro get() = getString(R.string.micro)
    protected val nano get() = getString(R.string.nano)
    protected val pico get() = getString(R.string.pico)
    protected val femto get() = getString(R.string.femto)
    protected val atto get() = getString(R.string.atto)
    protected val exaSymbol get() = getString(R.string.exa_symbol)
    protected val petaSymbol get() = getString(R.string.peta_symbol)
    protected val teraSymbol get() = getString(R.string.tera_symbol)
    protected val gigaSymbol get() = getString(R.string.giga_symbol)
    protected val megaSymbol get() = getString(R.string.mega_symbol)
    protected val kiloSymbol get() = getString(R.string.kilo_symbol)
    protected val hectoSymbol get() = getString(R.string.hecto_symbol)
    protected val decaSymbol get() = getString(R.string.deca_symbol)
    protected val deciSymbol get() = getString(R.string.deci_symbol)
    protected val centiSymbol get() = getString(R.string.centi_symbol)
    protected val milliSymbol get() = getString(R.string.milli_symbol)
    protected val microSymbol get() = getString(R.string.micro_symbol)
    protected val nanoSymbol get() = getString(R.string.nano_symbol)
    protected val picoSymbol get() = getString(R.string.pico_symbol)
    protected val femtoSymbol get() = getString(R.string.femto_symbol)
    protected val attoSymbol get() = getString(R.string.atto_symbol)
    protected val yottaSymbol get() = getString(R.string.yotta_symbol)
    protected val zettaSymbol get() = getString(R.string.zetta_symbol)
    protected val zeptoSymbol get() = getString(R.string.zepto_symbol)
    protected val yoctoSymbol get() = getString(R.string.yocto_symbol)
    protected val squareSymbol get() = getString(R.string.square_symbol)
    protected val square get() = getString(R.string.square)

    //
    protected abstract val context: Context

    abstract fun getList(): MutableList<RecyclerDataClass>

    protected fun getString(stringId: Int) = context.resources.getString(stringId)

    protected fun getString(stringId: Int, block: String.() -> Unit) =
        getString(stringId).apply(block)

    protected var start: Int = 0

    protected fun massPrefixes(quantity: String = "", unit: String = "") =
        buildRecyclerList(16) {
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

    protected fun MutableList<RecyclerDataClass>.add(quantity: Int, unit: Int) =
        add(RecyclerDataClass(getString(quantity), getString(unit), start++))

    protected fun MutableList<RecyclerDataClass>.add(quantity: String, unit: CharSequence) =
        add(RecyclerDataClass(quantity, unit, start++))

    @Suppress("NOTHING_TO_INLINE")
    protected inline infix fun <T> MutableList<T>.putAll(list: MutableList<T>) =
        addAll(list)

    protected inline fun MutableList<RecyclerDataClass>.entry(values: Data.() -> Unit) =
        Data().apply(values).run {
            add(RecyclerDataClass(quantity!!, unit!!, start++))
        }

    protected inline fun buildRecyclerList(
        capacity: Int,
        block: MutableList<RecyclerDataClass>.() -> Unit
    ) = buildMutableList(capacity, block)

    @Suppress("NOTHING_TO_INLINE")
    protected inline infix fun Data.quantity(string: String) {
        quantity = string
    }

    @Suppress("NOTHING_TO_INLINE")
    protected inline infix fun Data.unit(string: String) {
        unit = string
    }

    protected data class Data(var quantity: String? = null, var unit: CharSequence? = null)
}