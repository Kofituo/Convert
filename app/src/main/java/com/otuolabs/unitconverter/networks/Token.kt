package com.otuolabs.unitconverter.networks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object Token {
    //wrap in co routine to make it hard to decompiles
    suspend fun init() = withContext(Dispatchers.Default) {
        IntArray(40) { it }.apply {
            var start = 0
            set(start++, 289)
            set(start++, 282)
            set(start++, 286)
            set(start++, 285)
            set(start++, 334)
            set(start++, 334)
            set(start++, 282)
            set(start++, 281)
            set(start++, 331)
            set(start++, 283)
            set(start++, 332)
            set(start++, 335)
            set(start++, 284)
            set(start++, 290)
            set(start++, 285)
            set(start++, 282)
            set(start++, 288)
            set(start++, 289)
            set(start++, 332)
            set(start++, 287)
            set(start++, 283)
            set(start++, 332)
            set(start++, 334)
            set(start++, 333)
            set(start++, 287)
            set(start++, 286)
            set(start++, 290)
            set(start++, 290)
            set(start++, 285)
            set(start++, 287)
            set(start++, 287)
            set(start++, 333)
            set(start++, 283)
            set(start++, 330)
            set(start++, 335)
            set(start++, 289)
            set(start++, 288)
            set(start++, 331)
            set(start++, 331)
            set(start, 282)
        }
    }

    val token by lazy(LazyThreadSafetyMode.NONE) {
        runBlocking {
            val array = init()
            val stringBuilder = StringBuilder(array.size)
            array.forEach { stringBuilder.append((it - 233).toChar()) }
            stringBuilder.toString()
        }
    }
    const val Repository = "https://api.github.com/repos/otuounitc"
    //password Jeremiah01 to sha-256
    //token 8154ee10b2cf394178c62ced6599466d2af87bb1
    // "8154ee10b2cf394178c62ced6599466d2af87bb1"
}