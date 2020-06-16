package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.appendString
import com.example.unitconverter.miscellaneous.value

class Radioactivity(override val context: Context) : RecyclerDataAbstractClass() {

    private val _becquerel = getString(R.string.becquerel)
    private val becquerel = _becquerel.toLC()
    private val _curie = getString(R.string.curie)
    private val curie = _curie.toLC()
    private val _rutherford = getString(R.string.rutherford)
    private val rutherford = _rutherford.toLC()
    private val becUnit = getString(R.string.becquerel_unit)
    private val rutherfordUnit = getString(R.string.rutherford_unit)
    private val curieUnit = getString(R.string.curie_unit)

    override fun getList() = buildRecyclerList(27) {
        putEntry {
            quantity = _becquerel
            unit = becUnit
        }
        putEntry {
            quantity = appendString {
                this value yotta
                this value becquerel
            }
            unit = appendString {
                this value yottaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value zetta
                this value becquerel
            }
            unit = appendString {
                this value zettaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value exa
                this value becquerel
            }
            unit = appendString {
                this value exaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value peta
                this value becquerel
            }
            unit = appendString {
                this value petaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value tera
                this value becquerel
            }
            unit = appendString {
                this value teraSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value giga
                this value becquerel
            }
            unit = appendString {
                this value gigaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value mega
                this value becquerel
            }
            unit = appendString {
                this value megaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value kilo
                this value becquerel
            }
            unit = appendString {
                this value kiloSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value hecto
                this value becquerel
            }
            unit = appendString {
                this value hectoSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value deca
                this value becquerel
            }
            unit = appendString {
                this value decaSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value deci
                this value becquerel
            }
            unit = appendString {
                this value deciSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value centi
                this value becquerel
            }
            unit = appendString {
                this value centiSymbol
                this value becUnit
            }
        }
        putEntry {
            quantity = _curie
            unit = curieUnit
        }
        putEntry {
            quantity = appendString {
                this value kilo
                this value curie
            }
            unit = appendString {
                this value kiloSymbol
                this value curieUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value centi
                this value curie
            }
            unit = appendString {
                this value centiSymbol
                this value curieUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value milli
                this value curie
            }
            unit = appendString {
                this value milliSymbol
                this value curieUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value micro
                this value curie
            }
            unit = appendString {
                this value microSymbol
                this value curieUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value nano
                this value curie
            }
            unit = appendString {
                this value nanoSymbol
                this value curieUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value pico
                this value curie
            }
            unit = appendString {
                this value picoSymbol
                this value curieUnit
            }
        }
        putEntry {
            quantity = _rutherford
            unit = rutherfordUnit
        }
        putEntry {
            quantity = appendString {
                this value kilo
                this value rutherford
            }
            unit = appendString {
                this value kiloSymbol
                this value rutherfordUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value centi
                this value rutherford
            }
            unit = appendString {
                this value centiSymbol
                this value rutherfordUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value milli
                this value rutherford
            }
            unit = appendString {
                this value milliSymbol
                this value rutherfordUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value micro
                this value rutherford
            }
            unit = appendString {
                this value microSymbol
                this value rutherfordUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value nano
                this value rutherford
            }
            unit = appendString {
                this value nanoSymbol
                this value rutherfordUnit
            }
        }
        putEntry {
            quantity = appendString {
                this value pico
                this value rutherford
            }
            unit = appendString {
                this value picoSymbol
                this value rutherfordUnit
            }
        }
    }
}