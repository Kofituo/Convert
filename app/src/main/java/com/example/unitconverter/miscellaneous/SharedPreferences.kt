package com.example.unitconverter.miscellaneous

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.fragment.app.Fragment

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
) =
    sharedPreferences.edit(false, block)

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

/**
 * Delegate class to use shared preferences
 * */

private class SharedPreferencesLazy(
    activity: Any,
    private var stringFunc: (() -> String)? = null
) : Lazy<SharedPreferences> {
    private var cached: SharedPreferences? = null
    private val activity by lazy {
        when (activity) {
            is Activity -> activity
            is Fragment -> activity.requireActivity()
            else -> TODO()
        }
    }
    override val value: SharedPreferences
        get() {
            val sharedPreferences = cached
            return if (sharedPreferences.isNull()) {
                if (stringFunc.isNull())
                    activity.getPreferences(Context.MODE_PRIVATE)
                        .also {
                            cached = it
                        }
                else activity.getSharedPreferences(stringFunc!!(), Context.MODE_PRIVATE)
                    .also {
                        cached = it
                        stringFunc = null
                    }
            } else {
                sharedPreferences
            }
        }

    override fun isInitialized(): Boolean = cached != null
}

fun Activity.defaultPreferences(): Lazy<SharedPreferences> = SharedPreferencesLazy(this)

fun Fragment.sharedPreferences(string: () -> String): Lazy<SharedPreferences> =
    SharedPreferencesLazy(this, string)

fun Activity.sharedPreference(string: () -> String): Lazy<SharedPreferences> =
    SharedPreferencesLazy(this, string)