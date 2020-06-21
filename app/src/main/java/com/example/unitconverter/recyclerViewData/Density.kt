package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.appendString
import com.example.unitconverter.miscellaneous.value
import com.example.unitconverter.miscellaneous.valueWithSpace

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

    override fun getList() =
        buildRecyclerList(0) {
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
        }
}