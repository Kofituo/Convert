package com.otuolabs.unitconverter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputFilter
import android.util.*
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import androidx.core.util.forEach
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.google.android.material.textfield.TextInputEditText
import com.otuolabs.unitconverter.builders.buildMutableList
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.builders.put
import com.otuolabs.unitconverter.miscellaneous.DecimalFormatFactory
import com.otuolabs.unitconverter.miscellaneous.hasValue
import com.otuolabs.unitconverter.miscellaneous.isNotNull
import com.otuolabs.unitconverter.miscellaneous.isNull
import com.otuolabs.unitconverter.subclasses.RecyclerViewUpdater
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.set
import kotlin.math.absoluteValue
import kotlin.math.round
import kotlin.time.ExperimentalTime


object Utils {
    //it is set right from the start
    var decimalFormatSymbols: DecimalFormatSymbols? = null

    val groupingSeparator get() = decimalFormatSymbols!!.groupingSeparator

    val decimalSeparator get() = decimalFormatSymbols!!.decimalSeparator

    val exponentSeparator get() = decimalFormatSymbols?.exponentSeparator!!

    val zero get() = decimalFormatSymbols!!.zeroDigit

    var isEngineering: Boolean = false

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
    /**
     * Applies the given [transform] function to each element of the original collection
     * and appends the results to the given [destination].
     */
    inline fun <K, V, T, C : MutableMap<K, V>> Iterable<T>.mapTo(destination: C, transform: (T) -> Pair<K, V>): C {
        for (item in this)
            destination.put {
                val pair = transform(item)
                key = pair.first
                value = pair.second
            }
        return destination
    }

    /**
     * return name of view
     * */
    fun getViewName(viewId: Int) =
            viewNames[viewId]
                    ?: ApplicationLoader.applicationContext?.resources?.getResourceEntryName(viewId)
                            ?.apply {
                                viewNames[viewId] = this
                            }
                    ?: ""

    /**
     * A map which holds view ids and view names
     * Used to <br>quickly</br> get name of views instead of using
     * resources.getResourceEntryName(view.id)
     * */
    private val viewNames = LinkedHashMap<Int, String>(30)

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
                            }

    inline fun <K, V> Map<K, V>.forEachItem(action: (key: K, value: V) -> Unit) {
        forEach {
            action(it.key, it.value)
        }
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
        if (!isEngineering)
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
    fun temperatureFilters(editText: TextInputEditText): Array<InputFilter> {
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

    fun lengthFilter() = InputFilter.LengthFilter(68)//for atm

/*
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
*/
/*

    */
    /**
     * Returns are new [MutableMap] where the key / values are reversed.
     * i.e. the last pair becomes the first pair
     * *//*

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
*/

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

    fun CharSequence?.containsIgnoreCase(what: CharSequence): Boolean {
        if (this.isNull()) return false
        val otherLength = what.length
        if (otherLength == 0) return true // Empty string is contained
        val firstLo = Character.toLowerCase(what[0])
        val firstUp = Character.toUpperCase(what[0])
        for (i in length - otherLength downTo 0) {
            // Quick check before calling the more expensive regionMatches()
            // method:
            val ch = get(i)
            if (ch != firstLo && ch != firstUp) continue
            if (regionMatches(i, what, 0, otherLength, ignoreCase = true)) return true
        }
        return false
    }

    inline fun <T> SortedList<T>.forEach(action: (T) -> Unit) {
        for (element in (0 until size())) action(get(element))
    }

    /**
     * Converts the given [hours] to milliseconds
     * */
    fun hoursToMilliSeconds(hours: Int) = 3_600_000 * hours

    fun daysToMilliSeconds(days: Int) = hoursToMilliSeconds(24 * days)

    /**
     * Returns the current time minus the [initialTime]
     *
     * Return value in milliseconds
     * */
    fun timeDiffFromCurrentTime(initialTime: Long) = System.currentTimeMillis() - initialTime

    @OptIn(ExperimentalTime::class)
    inline fun <T> RecyclerView.Adapter<RecyclerView.ViewHolder>.applyDifference(
            listStartIndex: Int,
            lists: RecyclerViewUpdater.RecyclerLists<T>.() -> Unit
    ) {
        RecyclerViewUpdater.RecyclerLists<T>().apply(lists).apply {
            val oldList = oldList!!
            val newList = newList!!
            val newSize = newList.size
            val oldSize = oldList.size
            val sizeDifference = newSize - oldSize
            val longerList: List<T>
            val shorterList = // use the smaller list for the iteration
                    if (newSize < oldSize) {
                        longerList = oldList
                        newList
                    } else {
                        longerList = newList
                        oldList
                    }
            //these methods update those positions so i don't have to iterate through the longest list
            if (sizeDifference < 0)
                notifyItemRangeRemoved(listStartIndex + newSize, sizeDifference.absoluteValue)
            else if (sizeDifference > 0)
                notifyItemRangeInserted(listStartIndex + oldSize, sizeDifference)
            //update affected ones
            for (i in shorterList.indices)
                if (shorterList[i] != longerList[i])
                    notifyItemChanged(listStartIndex + i)
        }
    }

    /**
     * Checks if the device is a tablet or a phone
     *
     * @return Returns true if the device is a Tablet
     */
    fun isTablet(activity: Activity): Boolean {
        if (isTablet.isNotNull()) return isTablet as Boolean

        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        val xlarge =
                activity.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge) {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                // Yes, this is a tablet!
                isTablet = true
                return true
            }
        }

        // No, this is not a tablet!
        isTablet = false
        return false
    }

    private var isTablet: Boolean? = null

    val isPortrait
        get() =
            ApplicationLoader.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

    inline val context get() = ApplicationLoader.applicationContext!!

    val isNightMode
        get() =
            context.resources
                    ?.configuration
                    ?.uiMode
                    ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    val darkModeSelected
        get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    interface Connectivity {
        fun initializeConnections() {
            setUpConnectivityManager()
        }

        fun setUpConnectivityManager(): ConnectivityManager?
        fun onNetworkAvailable() {}
        fun onNetworkLost() {}
    }

    interface DefaultConnectivity : Connectivity {

        override fun setUpConnectivityManager() =
                context.getSystemService<ConnectivityManager>()?.apply {
                    val looper = context.mainLooper ?: return@apply
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            Handler(looper).post {
                                onNetworkAvailable()
                            }
                        }

                        override fun onLost(network: Network) {
                            Handler(looper).post {
                                onNetworkLost()
                            }
                        }
                    }.let {
                        registerNetworkCallback(NetworkRequest.Builder().build(), it)
                    }
                }
    }

    inline val Boolean.inverse get() = !this
}