package com.example.unitconverter.constants

object Force {
    val amongNewton
        get() = Mass.buildPrefixMass().apply { append(size(), -5) }
}