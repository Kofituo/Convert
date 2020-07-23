package com.otuolabs.unitconverter.miscellaneous

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.otuolabs.unitconverter.ApplicationLoader

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
        @Suppress("UNCHECKED_CAST")
        when (T::class) {
            Boolean::class -> putBoolean(key, value as Boolean)
            String::class -> putString(key, value as? String)
            Int::class -> putInt(key, value as Int)
            Long::class -> putLong(key, value as Long)
            Float::class -> putFloat(key, value as Float)
            Set::class -> putStringSet(key, value as? Set<String>)
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
        Long::class -> (getLong(key, -1) as T).apply(block)
        Float::class -> (getFloat(key, -1f) as T).apply(block)
        MutableSet::class -> (getStringSet(key, null) as T).apply(block)
        else -> TODO()
    }

/**
 * Delegate class to use shared preferences
 * */

class SharedPreferencesLazy(
    private val activity: () -> Activity, // to create a wrapper around activity and to prevent errors like not attached to window
    private var stringFunc: (() -> String)? = null
) : Lazy<SharedPreferences> {
    private var cached: SharedPreferences? = null

    override val value: SharedPreferences
        get() {
            val sharedPreferences = cached
            return if (sharedPreferences.isNull()) {
                val activity = activity()
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
            } else sharedPreferences
        }

    override fun isInitialized(): Boolean = cached != null
}

fun Activity.defaultPreferences(): Lazy<SharedPreferences> = SharedPreferencesLazy({ this })

fun Fragment.sharedPreferences(string: () -> String): Lazy<SharedPreferences> =
        SharedPreferencesLazy({ this.requireActivity() }, string)

fun Activity.sharedPreference(string: () -> String): Lazy<SharedPreferences> =
        SharedPreferencesLazy({ this }, string)

inline val Context.globalPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

inline val globalPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(ApplicationLoader.applicationContext)