package com.example.unitconverter.miscellaneous

import android.util.Log
import com.example.unitconverter.R
import com.example.unitconverter.Utils.forEachIndexed
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.functions.*
import com.example.unitconverter.subclasses.Positions

class InitializeFunction(private val viewId: Int) {
    lateinit var function: (Positions) -> String
        private set

    init {
        whichView()
    }

    private fun whichView() {
        when (viewId) {
            R.id.prefixes -> prefixConversions()

            R.id.Temperature -> temperatureConversions()

            R.id.Area -> areaConversions()

            R.id.Mass -> massConversions()

            R.id.Volume -> volumeConversions()

            R.id.Length -> lengthConversions()

            R.id.Angle -> angleConversions()

            R.id.Pressure -> pressureConversions()

            R.id.Speed -> speedConversions()

            R.id.time -> timeConversions()

            R.id.fuelEconomy -> fuelEconomyConversions()

            R.id.dataStorage -> dataStorageConversion()

            R.id.electric_current -> currentConversions()

            R.id.luminance -> luminanceConversions()

            R.id.Illuminance -> illuminance()

            R.id.energy -> energyConversion()

            R.id.heatCapacity -> heatCapacityConversions()

            R.id.Angular_Velocity -> velocityConversions()

            R.id.angularAcceleration -> accelerationConversions()

            R.id.sound -> soundConversions()

            R.id.resistance -> resistanceConversions()

            R.id.radioactivity -> radioactivityConversions()

            R.id.force -> forceConversions()

            R.id.power -> {
            }
            R.id.density -> {
            }
            R.id.flow -> {
            }
            R.id.inductance -> {
            }
        }
    }

    private fun areaConversions() {
        function = {
            Area(it).getText()
        }
    }

    private fun volumeConversions() {
        function = {
            Volume(it).getText()
        }
    }

    private fun prefixConversions() {
        function = {
            Prefixes(it).getText()
        }
    }

    private fun angleConversions() {
        function = {
            Angle(it).getText()
        }
    }

    private fun fuelEconomyConversions() {
        function = {
            FuelEconomy(it).getText()
        }
    }

    private fun lengthConversions() {
        function = {
            Length(it).getText()
        }
    }

    private fun massConversions() {
        function = {
            Mass(it).getText()
        }
    }

    private fun temperatureConversions() {
        function = {
            Temperature(it).getText()
        }
    }

    private fun pressureConversions() {
        function = {
            Pressure(it).getText()
        }
    }

    private fun speedConversions() {
        function = {
            Speed(it).getText()
        }
    }

    private fun timeConversions() {
        function = {
            Time(it).getText()
        }
    }

    private fun dataStorageConversion() {
        function = {
            Log.e("called", "called")
            DataStorage(it).apply { lazyMap = dataStorageMap }.getText()
        }
    }

    //to prevent unwanted initializations
    private val dataStorageMap = lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(35) {
            put(0, 0) //bits
            (27..34).forEachIndexed(1) { index, item ->
                // metric bits prefixes
                put(item, index)
                Log.e("size", "item $item  index $index")
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

    private fun currentConversions() {
        function = {
            ElectricCurrent(it).getText()
        }
    }

    private fun luminanceConversions() {
        function = {
            Luminance(it).apply { lazyMap = luminanceMap }.getText()
        }
    }

    private val luminanceMap = lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, Int>(20) {
            (0..7).forEachIndexed { index, item ->
                put(item, index)
                Log.e("itemr", "($item  $index)")
            }
            (13..19).forEachIndexed(size) { index, item ->
                put(item, index)
            }
            (8..12).forEachIndexed(size) { index, item ->
                put(item, index)
            }
        }
    }

    private fun illuminance() {
        function = {
            Illuminance(it).getText()
        }
    }

    private fun energyConversion() {
        function = {
            Energy(it).getText()
        }
    }

    private fun heatCapacityConversions() {
        function = {
            HeatCapacity(it).getText()
        }
    }

    private fun velocityConversions() {
        function = {
            Velocity(it).getText()
        }
    }

    private fun accelerationConversions() {
        function = {
            Acceleration(it).getText()
        }
    }

    private fun soundConversions() {
        function = {
            Sound(it).getText()
        }
    }

    private fun resistanceConversions() {
        function = {
            Resistance(it).getText()
        }
    }

    private fun radioactivityConversions() {
        function = {
            Radioactivity(it).getText()
        }
    }

    private fun forceConversions() {
        function = {
            Force(it).getText()
        }
    }
}
