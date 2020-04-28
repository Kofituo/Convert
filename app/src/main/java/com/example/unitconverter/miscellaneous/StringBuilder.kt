@file:Suppress("NOTHING_TO_INLINE")

package com.example.unitconverter.miscellaneous

/*
append {
    this value trial
    this value trial
}
* */
data class Value(val stringBuilder: StringBuilder)

inline fun StringBuilder.put(action: Value.() -> Unit) {
    Value(this).apply(action)
}

inline infix fun Value.value(string: String) {
    stringBuilder.append(string)
}

inline infix fun Value.valueWithSpace(string: String) {
    stringBuilder.append(string)
    stringBuilder.append(" ")
}

inline fun appendString(action: Value.() -> Unit) =
    Value(StringBuilder()).apply(action).stringBuilder.toString()

inline fun appendString(capacity: Int, action: Value.() -> Unit) =
    Value(StringBuilder(capacity)).apply(action).stringBuilder.toString()
