package com.otuolabs.unitconverter.miscellaneous

class MutableLazy<T>(private val initializer: () -> T) : Lazy<T> {
    private var cached: T? = null
    override val value: T
        get() {
            if (cached.isNull()) {
                cached = initializer()
            }
            @Suppress("UNCHECKED_CAST")
            return cached as T
        }

    fun reset() {
        cached = null
    }

    override fun isInitialized(): Boolean = cached != null

    companion object {
        fun <T> resettableLazy(value: () -> T) = MutableLazy(value)
    }
}