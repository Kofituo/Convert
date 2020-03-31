package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.put
import com.example.unitconverter.miscellaneous.value
import com.example.unitconverter.miscellaneous.valueWithSpace

class Energy(override val context: Context) : RecyclerDataAbstractClass() {

    private val _joule = getString(R.string.joule)
    private val joule = _joule.toLowerCase(locale)
    private val jouleUnit = getString(R.string.joule_unit)
    private val tnt = getString(R.string.tnt)
    private val _gram = getString(R.string.gram)
    private val gram = _gram.toLowerCase(locale)
    private val gramUnit = getString(R.string.gram_unit)
    private val of = getString(R.string.of)
    private val ton = getString(R.string.ton)
    private val tonUnit = getString(R.string.metricTonUnit)
    private val calorie = getString(R.string.calorie)
    private val calorieUnit = getString(R.string.calorie_unit)

    override fun getList() =
        buildRecyclerList(90) {
            add(_joule, jouleUnit)
            addAll(
                massPrefixes(joule, jouleUnit)
            )
            add(calorie, calorieUnit)
            entry {
                this quantity buildString {
                    put {
                        this value kilo
                        this value calorie.toLowerCase(locale)
                    }
                }
                this unit buildString {
                    put {
                        this value kiloSymbol
                        this value calorieUnit
                    }
                }
            }
            entry {
                this quantity buildString {
                    put {
                        this valueWithSpace _gram
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit gramUnit
            }
            entry {
                this quantity buildString {
                    put {
                        this value kilo
                        this valueWithSpace gram
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit buildString {
                    put {
                        this value kiloSymbol
                        this value gramUnit
                    }
                }
            }
            add(
                buildString {
                    put {
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }, buildString {
                    put {
                        this value tonUnit
                    }
                }
            )
            add(
                buildString {
                    put {
                        this value kilo
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }, buildString {
                    put {
                        this value kiloSymbol
                        this value tonUnit
                    }
                }
            )
            add(
                buildString {
                    put {
                        this value mega
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }, buildString {
                    put {
                        this value megaSymbol
                        this value tonUnit
                    }
                }
            )
            add(
                buildString {
                    put {
                        this value giga
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }, buildString {
                    put {
                        this value gigaSymbol
                        this value tonUnit
                    }
                }
            )
            add(
                buildString {
                    put {
                        this value tera
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }, buildString {
                    put {
                        this value teraSymbol
                        this value tonUnit
                    }
                }
            )
        }
}