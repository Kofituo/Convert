package com.example.unitconverter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputFilter
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.TypedValue
import android.view.View
import androidx.core.util.forEach
import androidx.recyclerview.widget.SortedList
import com.example.unitconverter.builders.buildMutableList
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.miscellaneous.DecimalFormatFactory
import com.example.unitconverter.miscellaneous.hasValue
import com.example.unitconverter.miscellaneous.isNotNull
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.math.round

object Utils {
    //it is set right from the start
    var decimalFormatSymbols: DecimalFormatSymbols? = null

    val groupingSeparator get() = decimalFormatSymbols!!.groupingSeparator

    val decimalSeparator get() = decimalFormatSymbols!!.decimalSeparator

    val exponentSeparator get() = decimalFormatSymbols?.exponentSeparator!!

    val zero get() = decimalFormatSymbols!!.zeroDigit

    var isEngineering: Boolean? = null

    var pattern: String? = null

    var numberOfDecimalPlace = -1

    var app_bar_bottom = 0
    val minusSign get() = decimalFormatSymbols!!.minusSign

    val <V>SparseArray<V>.values: MutableList<V>
        get() =
            buildMutableList(size()) {
                this@values.forEach { _, value ->
                    add(value)
                }
            }

    inline fun <V> SparseArray<V>.values(action: MutableList<V>.() -> Unit) =
        values.apply(action)

    fun Int.dpToInt(context: Context): Int = round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            context.resources.displayMetrics
        )
    ).toInt()

    /*fun Int.toDp(context: Context): Int {
        return this.div((context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT)
    }*/

    private val nameToViews = LinkedHashMap<String, View>(30) // what i should have done a long time

    /**
     * A map which holds view ids and view names
     * Used to <br>quickly</br> get name of views instead of using
     * resources.getResourceEntryName(view.id)
     * */
    private val viewNames = LinkedHashMap<Int, String>(30)

    fun getNameToViewMap(): Map<String, View> = nameToViews

    /**
     * Used to get name from id
     *
     * First checks from a map is its there //for fast access
     * */
    val View.name: String
        get() =
            if (id == -0x1) throw Resources.NotFoundException("Invalid ID '-1' $this")
            else viewNames[this.id]
                ?: resources.getResourceEntryName(this.id) //returns a string
                    .apply {
                        viewNames[this@name.id] = this //updates the map
                        nameToViews[this] = this@name
                    }

    fun CharSequence.removeCommas(decimalSeparator: Char): String? {
        if (this.isBlank()) return ""
        val checkString = StringBuilder()
        when {
            this.startsWith(decimalSeparator) ->
                checkString.append(zero).append(this)
            this.endsWith(decimalSeparator) ->
                checkString.append(this).append(zero)
            else -> checkString.append(this)
        }
        (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
            decimalFormatSymbols = this@Utils.decimalFormatSymbols
            isParseBigDecimal = true
            return parse(checkString.toString())?.toString()
        }
    }

    private val decimalFormat
        get() =
            (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat)
                .apply {
                    decimalFormatSymbols = this@Utils.decimalFormatSymbols
                    applyPattern(pattern)
                }

    fun String.insertCommas(): String = toBigDecimal().insertCommas()

    fun BigDecimal.insertCommas(): String {
        if (isEngineering != true)
            return decimalFormat.format(this).run {
                if (endsWith(decimalSeparator)) {
                    substringBeforeLast(decimalSeparator)
                } else this
            }
        return DecimalFormatFactory().format(decimalFormat, this, numberOfDecimalPlace)
    }

    //filters
    fun filters(editText: TextInputEditText): Array<InputFilter> {
        val filter = InputFilter { source, start, end, _, _, _ ->
            Log.e("fil", "fil $source c $groupingSeparator  f $decimalSeparator")
            val stringBuilder = StringBuilder(end - start)
            var count = 0
            for (i in start until end) {
                if (source[i].isDigit() ||
                    source[i] == groupingSeparator ||
                    source[i] == decimalSeparator
                ) {
                    if (source[i] == decimalSeparator) {
                        // ensures only one decimal separator is in the text
                        count++
                        if (count >= 2) continue
                        if (source.length > 1 && editText.isFocused) {
                            val text = editText.text
                            if (text.isNotNull()
                                && text.contains(decimalSeparator)
                            ) continue
                        }
                    }
                    if (source[0] == groupingSeparator && editText.text.isNullOrEmpty())
                        continue //to prevent unparseable number ,
                    stringBuilder.append(source[i])
                }
            }
            stringBuilder
        }
        return arrayOf(filter, lengthFilter())
    }

    // temperature filter
    fun temperatureFilters(
        editText: TextInputEditText
    ): Array<InputFilter> {
        val filter = InputFilter { source, start, end, _, _, _ ->
            val stringBuilder = StringBuilder(end - start)
            var count = 0
            for (i in start until end) {
                val text = editText.text
                val editTextSelectionStart = editText.selectionStart
                if (text.isNotNull() &&
                    text.indexOf(minusSign) == editTextSelectionStart
                ) continue // prevent things like 4-5.0

                if (source[i] == minusSign) {
                    if (i != 0 || text.hasValue()) continue
                    if (
                        editTextSelectionStart != 0 ||
                        editTextSelectionStart != editText.selectionEnd
                    ) continue

                    if (text.isNotNull() && text.indexOf(minusSign) != -1) continue
                    stringBuilder.append(source[i])
                }
                if (text.isNullOrEmpty() && source[0] == groupingSeparator) continue //to prevent unparseable number ,
                if (source[i].isDigit() ||
                    source[i] == groupingSeparator ||
                    source[i] == decimalSeparator
                ) {
                    if (source[i] == decimalSeparator) {
                        // ensures only one decimal separator is in the text
                        count++
                        if (count >= 2) continue
                        if (source.length > 1 && editText.isFocused) {
                            if (text.isNotNull()
                                && text.contains(decimalSeparator)
                            ) continue
                        }
                    }
                    stringBuilder.append(source[i])
                }
            }
            stringBuilder
        }
        return arrayOf(filter, lengthFilter())
    }

    fun lengthFilter() = InputFilter.LengthFilter(77)//for atm

    fun <K, V> Map<K, V>.toJson(): String {
        if (isEmpty()) return "[]"
        return buildString {
            append("[")
            this@toJson.forEach { (k, v) ->
                append("""{"$k":"$v"}""")
                append(",")
            }
            delete(length - 1, length)// deletes the last comma
            append("]")
        }
    }

    /**
     * Returns are new [MutableMap] where the key / values are reversed.
     * i.e. the last pair becomes the first pair
     * */
    fun <K, V> Map<K, V>.reversed() =
        if (size < 2) toMap() // fast return
        else ArrayList(entries).run {
            val reversedMap = LinkedHashMap<K, V>(size)
            for (i in this.size - 1 downTo 0)
                this[i].apply {
                    reversedMap[key] = value
                }
            reversedMap
        }

    @Suppress("NOTHING_TO_INLINE")
    inline infix fun StringBuilder.appendWithSpace(string: String): StringBuilder {
        append(string)
        append(" ")
        return this
    }

    /**
     * Performs the given [action] on each element, providing sequential index with the element.
     * @param [action] function that takes the index of an element and the element itself
     * and performs the desired action on the element.
     */

    inline fun IntRange.forEachIndexed(start: Int, action: (index: Int, Int) -> Unit) {
        var index = start
        for (item in this) action(index++, item)
    }

    @SuppressLint("NewApi")
    fun performVibration(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        when {
            Build.VERSION.SDK_INT < 26 -> vibrator.vibrate(100)
            Build.VERSION.SDK_INT < 29 -> {
                val vibrationEffect =
                    VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            }
            else -> {
                val vibrationEffect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                vibrator.vibrate(vibrationEffect)
            }
        }
    }

    fun SparseBooleanArray.toMap() =
        buildMutableMap<Int, Boolean>(size()) {
            this@toMap.forEach { key, value ->
                put {
                    this.key = key
                    this.value = value
                }
            }
        }

    fun <T> replaceAll(dataSet: Collection<T>, listToAddItemsTo: MutableList<T>) {
        listToAddItemsTo.apply {
            //its in reverse so the indexing isn't affected
            //if it were 0 to size when we remove 0 1 now becomes original 2 and so on
            for (i in size - 1 downTo 0)
                if (!dataSet.contains(this[i]))
                    this.remove(this[i])
            //Log.e("data","${dataSet.map {(it as FavouritesData ).cardName}}  ")
            //Log.e("before","${map {(it as FavouritesData ).cardName}}  ")
            addAll(dataSet)
        }
    }

    fun <T> replaceAll(dataSet: Collection<T>, sortedList: SortedList<T>) {
        sortedList.apply {
            beginBatchedUpdates()
            //its in reverse so the indexing isn't affected
            //if it were 0 to size when we remove 0 1 now becomes original 2 and so on
            for (i in size() - 1 downTo 0)
                if (!dataSet.contains(this[i]))
                    this.remove(this[i])
            addAll(dataSet)
            endBatchedUpdates()
        }
    }

    inline fun <T> SortedList<T>.forEach(action: (T) -> Unit) {
        for (element in (0 until size())) action(get(element))
    }

    fun hoursToMilliSeconds(hours: Int) = 3_600_000 * hours

    fun daysToMilliSeconds(days: Int) = hoursToMilliSeconds(24 * days)
}
/**
 * Use cover up toolbar for view
 * */
/*fun Context.circleReveal(
    view: View,
    containsOverflow: Boolean,
    isShow: Boolean
) {
    var width = view.width
    width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
    if (containsOverflow) width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
    val centerX = width
    val centerY = view.height / 2
    val anim =
        if (isShow)
            ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0f, width.toFloat())
        else
            ViewAnimationUtils.createCircularReveal(view, centerX, centerY, width.toFloat(), 0f)
    //anim.duration = 220
    // make the view invisible when the animation is done
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            if (!isShow) {
                super.onAnimationEnd(animation)
                view.visibility = View.INVISIBLE
            }
        }
    })
    // make the view visible and start the animation
    if (isShow) view.visibility = View.VISIBLE
    // start the animation
    Log.e("called","here g")
    anim.start()
}*/