package com.otuolabs.unitconverter.miscellaneous

import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils.forEachIndexed
import com.otuolabs.unitconverter.builders.buildMutableMap
import com.otuolabs.unitconverter.functions.*
import com.otuolabs.unitconverter.subclasses.Positions

class InitializeFunction(private val viewId: Int) {
    lateinit var function: (Positions) -> String
        private set

    init {
        whichView()
    }

    private fun whichView() {
        function = when (viewId) {
            R.id.prefixes -> {
                { Prefixes(it).getText() }
            }

            R.id.Temperature -> {
                { Temperature(it).getText() }
            }

            R.id.Area -> {
                { Area(it).getText() }
            }

            R.id.Mass -> {
                { Mass(it).getText() }
            }

            R.id.Volume -> {
                { Volume(it).getText() }
            }

            R.id.Length -> {
                { Length(it).getText() }
            }

            R.id.Angle -> {
                { Angle(it).getText() }
            }

            R.id.Pressure -> {
                { Pressure(it).getText() }
            }

            R.id.Speed -> {
                { Speed(it).getText() }
            }

            R.id.time -> {
                { Time(it).getText() }
            }

            R.id.fuelEconomy -> {
                { FuelEconomy(it).getText() }
            }

            R.id.dataStorage -> {
                { DataStorage(it).apply { lazyMap = dataStorageMap }.getText() }
            }

            R.id.electric_current -> {
                { ElectricCurrent(it).getText() }
            }

            R.id.luminance -> {
                { Luminance(it).apply { lazyMap = luminanceMap }.getText() }
            }

            R.id.Illuminance -> {
                {
                    Illuminance(it).getText()
                }
            }

            R.id.energy -> {
                { Energy(it).getText() }
            }

            R.id.heatCapacity -> {
                { HeatCapacity(it).getText() }
            }

            R.id.Angular_Velocity -> {
                { Velocity(it).getText() }
            }

            R.id.angularAcceleration -> {
                { Acceleration(it).getText() }
            }

            R.id.sound -> {
                { Sound(it).getText() }
            }

            R.id.resistance -> {
                { Resistance(it).getText() }
            }

            R.id.radioactivity -> {
                { Radioactivity(it).getText() }
            }

            R.id.force -> {
                { Force(it).getText() }
            }

            R.id.power -> {
                { Power(it).getText() }
            }

            R.id.density -> {
                { Density(it).getText() }
            }

            R.id.flow -> {
                { Flow(it).getText() }
            }

            R.id.inductance -> {
                { Inductance(it).getText() }
            }

            R.id.resolution -> {
                { Resolution(it).getText() }
            }

            R.id.cooking -> {
                { Cooking(it).getText() }
            }
            R.id.number_base -> {
                { NumberBase(it).getText() }
            }
            else -> TODO()
        }
    }

    //to prevent unwanted initializations
    private val dataStorageMap = lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(35) {
            put(0, 0) //bits
            (27..34).forEachIndexed(1) { index, item ->
                // metric bits prefixes
                put(item, index)
            }
            (3..10).forEachIndexed(size) { index, item ->
                //other bits prefixes
                put(item, index)
            }
            put(1, size)//nibble
            put(2, size) // bytes
            (19..26).forEachIndexed(size) { index, item ->
                put(item, index)
            }
            (11..18).forEachIndexed(size) { index, item ->
                put(item, index)
            }
        }
    }

    private val luminanceMap = lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(20) {
            (0..7).forEachIndexed { index, item ->
                put(item, index)
            }
            (13..19).forEachIndexed(size) { index, item ->
                put(item, index)
            }
            (8..12).forEachIndexed(size) { index, item ->
                put(item, index)
            }
        }
    }
}