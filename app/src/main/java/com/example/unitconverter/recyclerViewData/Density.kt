package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.*

class Density(override val context: Context) : RecyclerDataAbstractClass() {

    private val _gram = getString(R.string.gram)
    private val gram = _gram.toLC()
    private val gramUnit = getString(R.string.gram_unit)
    private val metre = getString(R.string.metre).toLC()
    private val metreUnit = getString(R.string.metre_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val cube = getString(R.string.cubic).toLC()
    private val cubeUnit = getString(R.string.cubic_unit)
    private val lcKilo = kilo.toLC()
    private val lcMilli = milli.toLC()
    private val lcDeci = deci.toLC()
    private val lcDeca = deca.toLC()
    private val lcCenti = centi.toLC()
    private val pound = getString(R.string.pound)
    private val poundUnit = getString(R.string.pound_unit)
    private val ounce = getString(R.string.ounce)
    private val ounceUnit = getString(R.string.ounce_unit)
    private val litre = getString(R.string.litre).toLC()
    private val litreUnit = getString(R.string.litre_unit)
    private val inch = getString(R.string.inch).toLC()
    private val inchUnit = getString(R.string.inch_unit)
    private val usFluidOunce = getString(R.string.fluid_ounce_us)
    private val ukFluidOunce = getString(R.string.fluid_ounce_uk)
    private val fluidOunceUnit = getString(R.string.fluid_ounce)
    private val yard = getString(R.string.yard).toLC()
    private val yardUnit = getString(R.string.yard_unit)
    private val foot = getString(R.string.foot).toLC()
    private val footUnit = getString(R.string.foot_unit)
    private val usGal = getString(R.string.gal_us)
    private val ukGal = getString(R.string.imp_gal)
    private val galUnit = getString(R.string.gall_unit)

    override fun getList() =
        buildRecyclerList(30) {
            putEntry {
                quantity = appendString {
                    this valueWithSpace _gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcCenti
                    this value metre
                }
                unit = appendString {
                    this value gramUnit
                    this value perUnit
                    this value centiSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace _gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcMilli
                    this value metre
                }
                unit = appendString {
                    this value gramUnit
                    this value perUnit
                    this value milliSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace _gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcDeci
                    this value metre
                }
                unit = appendString {
                    this value gramUnit
                    this value perUnit
                    this value deciSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace _gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcDeca
                    this value metre
                }
                unit = appendString {
                    this value gramUnit
                    this value perUnit
                    this value decaSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace _gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcKilo
                    this value metre
                }
                unit = appendString {
                    this value gramUnit
                    this value perUnit
                    this value kiloSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace _gram
                    this valueWithSpace per
                    add { lcMilli }
                    this value litre
                }
                unit = appendString {
                    this value gramUnit
                    this value perUnit
                    add { milliSymbol }
                    this value litreUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this value litre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value litreUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value micro.toLC()
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value microSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcMilli
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value milliSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcCenti
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value centiSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcDeci
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value deciSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcDeca
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value decaSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace gram
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value lcKilo
                    this value metre
                }
                unit = appendString {
                    this value kiloSymbol
                    this value gramUnit
                    this value perUnit
                    this value kiloSymbol
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace getString(R.string.metric_ton)
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value metre
                }
                unit = appendString {
                    this value getString(R.string.metricTonUnit)
                    this value perUnit
                    this value metreUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace ounce
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value inch
                }
                unit = appendString {
                    this value ounceUnit
                    this value perUnit
                    this value inchUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace ounce
                    this valueWithSpace per
                    this value usFluidOunce
                }
                unit = appendString {
                    this value ounceUnit
                    this value perUnit
                    this value fluidOunceUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace ounce
                    this valueWithSpace per
                    this value ukFluidOunce
                }
                unit = appendString {
                    this value ounceUnit
                    this value perUnit
                    this value fluidOunceUnit
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { ounce }
                    addWithSpace { per }
                    addWithSpace { cube }
                    add { foot }
                }
                unit = appendString {
                    add { ounceUnit }
                    add { perUnit }
                    add { footUnit }
                    add { cubeUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { ounce }
                    addWithSpace { per }
                    addWithSpace { cube }
                    add { yard }
                }
                unit = appendString {
                    add { ounceUnit }
                    add { perUnit }
                    add { yardUnit }
                    add { cubeUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { ounce }
                    addWithSpace { per }
                    add { usGal }
                }
                unit = appendString {
                    add { ounceUnit }
                    add { perUnit }
                    add { galUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { ounce }
                    addWithSpace { per }
                    add { ukGal }
                }
                unit = appendString {
                    add { ounceUnit }
                    add { perUnit }
                    add { galUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace pound
                    this valueWithSpace per
                    this valueWithSpace cube
                    this value inch
                }
                unit = appendString {
                    this value poundUnit
                    this value perUnit
                    this value inchUnit
                    this value cubeUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace pound
                    this valueWithSpace per
                    this value usFluidOunce
                }
                unit = appendString {
                    this value poundUnit
                    this value perUnit
                    this value fluidOunceUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace pound
                    this valueWithSpace per
                    this value ukFluidOunce
                }
                unit = appendString {
                    this value poundUnit
                    this value perUnit
                    this value fluidOunceUnit
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pound }
                    addWithSpace { per }
                    addWithSpace { cube }
                    add { foot }
                }
                unit = appendString {
                    add { poundUnit }
                    add { perUnit }
                    add { footUnit }
                    add { cubeUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pound }
                    addWithSpace { per }
                    addWithSpace { cube }
                    add { yard }
                }
                unit = appendString {
                    add { poundUnit }
                    add { perUnit }
                    add { yardUnit }
                    add { cubeUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pound }
                    addWithSpace { per }
                    add { usGal }
                }
                unit = appendString {
                    add { poundUnit }
                    add { perUnit }
                    add { galUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pound }
                    addWithSpace { per }
                    add { ukGal }
                }
                unit = appendString {
                    add { poundUnit }
                    add { perUnit }
                    add { galUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { getString(R.string.slug_mass) }
                    addWithSpace { per }
                    addWithSpace { cube }
                    add { foot }
                }
                unit = appendString {
                    add { getString(R.string.slug_unit) }
                    add { perUnit }
                    add { footUnit }
                    add { cubeUnit }
                }
            }
        }
}