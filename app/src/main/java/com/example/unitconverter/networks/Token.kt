package com.example.unitconverter.networks

import com.example.unitconverter.builders.buildMutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object Token {
    //wrap in co routine to make it hard to decompiles
    suspend fun init(): MutableList<Int> = withContext(Dispatchers.Default) {
        buildMutableList<Int>(40) {
            runBlocking {
                add(290)
                add(331)
                add(281)
                add(283)
                add(290)
                add(285)
                add(331)
                add(282)
            }
            runBlocking {
                add(288)
                add(287)
                add(289)
                add(288)
                add(282)
                add(288)
                add(335)
                add(286)
            }
            runBlocking {
                add(283)
                add(281)
                add(334)
                add(332)
                add(290)
                add(286)
                add(331)
                add(331)
            }
            runBlocking {
                add(289)
                add(282)
                add(334)
                add(289)
                add(330)
                add(335)
                add(284)
                add(334)
            }
            runBlocking {
                add(289)
                add(284)
                add(332)
                add(284)
                add(334)
                add(285)
                add(286)
                add(335)
            }
        }
    }

    val token by lazy(LazyThreadSafetyMode.NONE) {
        runBlocking {
            val array = init()
            val stringBuilder = StringBuilder(array.size)
            array.forEach { stringBuilder.append(it.toChar()) }
            stringBuilder.toString()
        }
    }

    const val Repository = "https://api.github.com/repos/otuounitc"
}
