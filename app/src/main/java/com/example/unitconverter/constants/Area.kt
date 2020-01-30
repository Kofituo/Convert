package com.example.unitconverter.constants

import android.util.SparseIntArray

object Area : ConstantsInterface {

    fun amongSquareMetreMap(): SparseIntArray =
        SparseIntArray(8).apply {
            //from square metre perspective
            append(0, 0)
            append(1, 3 * 2)//for kilo
            append(2, 2 * 2) //for hecto
            append(3, 1 * 2)//for deka
            append(4, -1 * 2)//for deci
            append(5, -2 * 2)//for centi
            append(6, -3 * 2)//for milli
            append(7, -6 * 2)//for micro
        }

}