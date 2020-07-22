package com.otuolabs.unitconverter.networks

import com.otuolabs.unitconverter.builders.buildMutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object Token {
    //wrap in co routine to make it hard to decompiles
    suspend fun init() = withContext(Dispatchers.Default) {
        buildMutableList<Int>(40) {
            runBlocking {
                add(289)
                add(282)
                add(286)
                add(285)
                add(334)
                add(334)
                add(282)
                add(281)
            }
            runBlocking {
                add(331)
                add(283)
                add(332)
                add(335)
                add(284)
                add(290)
                add(285)
                add(282)
            }
            runBlocking {
                add(288)
                add(289)
                add(332)
                add(287)
                add(283)
                add(332)
                add(334)
                add(333)
                add(287)
            }
            runBlocking {
                add(286)
                add(290)
                add(290)
                add(285)
                add(287)
                add(287)
                add(333)
                add(283)
                add(330)
            }
            runBlocking {
                add(335)
                add(289)
                add(288)
                add(331)
                add(331)
                add(282)
            }
        }
    }

    val token by lazy(LazyThreadSafetyMode.NONE) {
        runBlocking {
            val array = init()
            val stringBuilder = StringBuilder(array.size)
            array.forEach { stringBuilder.append((it - 233).toChar()) }
            "8154ee10b2cf394178c62ced6599466d2af87bb1"
        }
    }
    const val Repository = "https://api.github.com/repos/otuounitc"
    //password Jeremiah01 to sha-256
    //token 8154ee10b2cf394178c62ced6599466d2af87bb1
}
