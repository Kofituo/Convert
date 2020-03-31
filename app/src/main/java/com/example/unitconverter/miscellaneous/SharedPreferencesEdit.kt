package com.example.unitconverter.miscellaneous

import android.content.SharedPreferences

data class SharedPreferencesEdit<T>(
    var key: String? = null,
    var value: T? = null
)

inline fun sharedPreferences(
    sharedPreferences: SharedPreferences,
    block: SharedPreferences.() -> Unit
): SharedPreferences =
    sharedPreferences.apply(block)

inline fun editPreferences(
    sharedPreferences: SharedPreferences,
    block: SharedPreferences.Editor.() -> Unit
): SharedPreferences.Editor =
    sharedPreferences.edit().apply(block)

inline fun <reified T> SharedPreferences.Editor.put(block: SharedPreferencesEdit<T>.() -> Unit): SharedPreferences.Editor =
    SharedPreferencesEdit<T>().apply(block).run {
        when (T::class) {
            Boolean::class -> putBoolean(key, value as Boolean)
            String::class -> putString(key, value as? String)
            Int::class -> putInt(key, value as Int)
            else -> TODO()
        }
    }

/**
 * Gets the value or return null for strings, false for booleans and -1 for int
 * */

//default param of nothing
inline fun <reified T> SharedPreferences.get(key: String, block: T.() -> Unit = {}) =
    when (T::class) {
        Boolean::class -> (getBoolean(key, false) as T).apply(block)
        String::class -> (getString(key, null) as T).apply(block)
        Int::class -> (getInt(key, -1) as T).apply(block)
        else -> TODO()
    }