package com.example.unitconverter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.CallSuper
import java.text.DecimalFormat
import java.util.*

open class SeparateThousands(
    private val editText: EditText,
    private val groupingSeparator: Char,
    private val decimalSeparator: Char
) :
    TextWatcher {

    private var busy = false
    private var lastPosition = 0
    private lateinit var prevString: CharSequence
    private val zeroDigit =
        (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat).decimalFormatSymbols.zeroDigit

    @CallSuper
    override fun afterTextChanged(s: Editable?) {
        if (s != null && !busy) {
            busy = true
            editText.removeTextChangedListener(this)
            var place = 0
            val isInitialized = this::prevString.isInitialized
            var decimalPointIndex = s.indexOf(decimalSeparator)
            val lastIndex = s.lastIndexOf(decimalSeparator)

            var editTextSelectionIndex: Int = -1
            if (isInitialized) {
                if (editText.selectionEnd < prevString.length && editText.selectionStart < s.length) {
                    if ((s.length < prevString.length && prevString[editText.selectionStart] == groupingSeparator) ||
                        (s[editText.selectionStart] == groupingSeparator)
                    ) editTextSelectionIndex = editText.selectionStart
                }
            }

            if (decimalPointIndex != -1 && lastPosition != -1 && decimalPointIndex != lastIndex) {
                val subString = s.subSequence(decimalPointIndex + 1, s.lastIndex + 1)
                val index = subString.indexOf(decimalSeparator)
                if (editText.selectionStart > lastPosition) {
                    //12.23456 -> 12345.6
                    s.delete(lastPosition, lastPosition + 1)
                    if (lastIndex - decimalPointIndex > 1) decimalPointIndex =
                        editText.selectionStart - 1
                } else {
                    //for example 123.654 ->  1.234//
                    s.delete(decimalPointIndex + index + 1, decimalPointIndex + index + 2)
                }
            }
            val groupingSeparatorIndex = s.indexOf(groupingSeparator, decimalPointIndex)
            if (groupingSeparatorIndex != -1 && decimalPointIndex != -1) {
                var start = groupingSeparatorIndex
                while (groupingSeparator in s.subSequence(start, s.lastIndex + 1)) {
                    s.delete(start, start + 1)
                    start = s.indexOf(groupingSeparator, start)
                    if (start == -1) break
                }
            }

            var i = if (decimalPointIndex == -1) s.length - 1 else decimalPointIndex - 1
            while (i >= 0) {
                val c = s[i]
                if (c == groupingSeparator) s.delete(i, i + 1)
                else {
                    if (place % 3 == 0 && place != 0) {
                        // insert a comma to the left of every 3rd digit (counting from right to
                        // left) unless it's the leftmost digit
                        s.insert(i + 1, groupingSeparator.toString())
                    }
                    place++
                }
                i--
            }
            busy = false
            if (editText.selectionStart != 0 && editTextSelectionIndex != -1) editText.setSelection(
                if (editTextSelectionIndex == editText.selectionStart)
                    editTextSelectionIndex - 1
                else editTextSelectionIndex
            )
            //0.0000003 -> 00000000.03
            //0.03
            //0.030001 -> 0030.01
            //30.01

            if (s.length == 2 && s[0] == zeroDigit && s[1] == zeroDigit) s.delete(1, 2)
            if (s.length > 2) {
                val f = StringBuilder(s).lastIndex
                for (x in 0 until f) {
                    if (s[0] == decimalSeparator) {
                        s.insert(0, zeroDigit.toString())
                        break
                    }
                    if (s[0].isDigit() && s[0] != zeroDigit) break
                    s.delete(0, 0 + 1)
                }
            }
            editText.addTextChangedListener(this)

        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (s != null && s.toString().isNotEmpty()) {
            prevString = s.subSequence(0, s.length)
            lastPosition = s.indexOf(decimalSeparator)
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}