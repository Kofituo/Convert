package com.example.unitconverter.funtions

import android.util.Log
import com.example.unitconverter.insertCommas
import java.math.BigDecimal
import java.math.MathContext

class Mass {

    companion object {
        var top = 0x000
        var bottom = 0x000

        private fun prefix(): Int {
            return Prefixes.prefix(top, bottom)
        }

        fun prefixMultiplication(x: String): String {
            if (x.isEmpty()) return ""
            val h = BigDecimal(x).times((BigDecimal.TEN).pow(prefix(), MathContext(90)))
            if (h.compareTo(BigDecimal.ZERO) == 0) return "0.00"
            Log.e("pr", "$h  ${prefix()}")

            return h.stripTrailingZeros().insertCommas()
        }
    }
}

class Prefixes {
    // 1 to 24 for positive 1 to 24
    // 25 to 49 for negative 1 to 24
    companion object {
        var top = 0
        var bottom = 0
        fun prefix(top: Int, bottom: Int): Int {
            return top - bottom
        }

        fun prefixMultiplication(x: String): BigDecimal {
            return BigDecimal(x).pow(prefix(top, bottom))
        }
    }
}