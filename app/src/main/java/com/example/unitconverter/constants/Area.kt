package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Length.chainToFeet
import com.example.unitconverter.constants.Length.chainToMetre
import com.example.unitconverter.constants.Length.chainToNauticalLeague
import com.example.unitconverter.constants.Length.chainToNauticalMile
import com.example.unitconverter.constants.Length.footToMetre
import com.example.unitconverter.constants.Length.inchToMetre
import com.example.unitconverter.constants.Length.metreToNauticalMile
import com.example.unitconverter.constants.Length.metresToYard
import com.example.unitconverter.constants.Length.nauticalLeagueToFoot
import com.example.unitconverter.constants.Length.nauticalLeagueToInch
import com.example.unitconverter.constants.Length.nauticalLeagueToMetre
import com.example.unitconverter.constants.Length.nauticalLeagueToMile
import com.example.unitconverter.constants.Length.nauticalLeagueToNauticalMile
import com.example.unitconverter.constants.Length.nauticalLeagueToYard
import com.example.unitconverter.constants.Length.nauticalMileToFoot
import com.example.unitconverter.constants.Length.nauticalMileToInch
import com.example.unitconverter.constants.Length.nauticalMileToMile
import com.example.unitconverter.constants.Length.nauticalMileToYard
import java.math.BigDecimal

object Area {

    val footToInch: BigDecimal get() = inverseOf(BigDecimal(144))

    val footToYard get() = BigDecimal(9)

    val feetToMetre: BigDecimal get() = footToMetre.pow(2)

    val metreToInch: BigDecimal get() = BigDecimal("0.00064516")

    val metreToYard: BigDecimal get() = metresToYard.pow(2)

    val inchToYard: BigDecimal get() = BigDecimal(1296)

    val metreToMile: BigDecimal get() = BigDecimal("2589988.110336")

    val mileToInch: BigDecimal get() = BigDecimal(4014489600)

    val feetToMile: BigDecimal get() = BigDecimal(27878400)

    val yardToMile: BigDecimal get() = BigDecimal(3097600)

    val metreToNauticalMiles: BigDecimal get() = metreToNauticalMile.pow(2)

    val yardToNauticalMile: BigDecimal get() = nauticalMileToYard.pow(2)

    val feetToNauticalMile: BigDecimal get() = nauticalMileToFoot.pow(2)

    val inchesToNauticalMile: BigDecimal get() = nauticalMileToInch.pow(2)

    val milesToNauticalMile: BigDecimal get() = nauticalMileToMile.pow(2)

    val nauticalMileToLeague: BigDecimal get() = BigDecimal(9)

    val leagueToMetre: BigDecimal get() = nauticalLeagueToMetre.pow(2)

    val leagueToFeet: BigDecimal get() = nauticalLeagueToFoot.pow(2)

    val leagueToYard: BigDecimal get() = nauticalLeagueToYard.pow(2)

    val leagueToInch: BigDecimal get() = nauticalLeagueToInch.pow(2)

    val leagueToMile: BigDecimal get() = nauticalLeagueToMile.pow(2)

    val chainToMetres: BigDecimal get() = chainToMetre.pow(2)

    val chainToFoot: BigDecimal get() = chainToFeet.pow(2)

    val chainToMile get() = inverseOf(BigDecimal(6400))

    val chainToYards: BigDecimal get() = BigDecimal(484)

    val chainToNauticalMiles: BigDecimal get() = chainToNauticalMile.pow(2)

    val chainToLeague: BigDecimal get() = chainToNauticalLeague.pow(2)

    val chainToInch: BigDecimal get() = BigDecimal(792 * 792)

    val chainToAcre: BigDecimal get() = BigDecimal.TEN

    val acreToMile: BigDecimal get() = inverseOf(BigDecimal(640))

    val acreToFoot: BigDecimal get() = BigDecimal(43560)

    val acreToInch: BigDecimal get() = BigDecimal(6272640)

    val acreToMetre: BigDecimal get() = BigDecimal("4046.8564224")

    val acreToYard: BigDecimal get() = BigDecimal(4840)

    val acreToNauticalMile: BigDecimal
        get() = acreToMile.multiply(nauticalMileToMile.pow(-2, mathContext))

    val acreToNauticalLeague: BigDecimal
        get() = acreToNauticalMile.multiply(nauticalLeagueToNauticalMile.pow(-2, mathContext))

    val acreToHectare: BigDecimal get() = inverseOf(acreToMetre.scaleByPowerOfTen(-4))

    val hectareToMetre: BigDecimal get() = BigDecimal(10000)

    val mileToHectare: BigDecimal get() = inverseOf(BigDecimal("258.9988110336"))

    val hectareToYard: BigDecimal get() = hectareToMetre.divide(metreToYard, mathContext)

    val hectareToFeet: BigDecimal get() = hectareToMetre.divide(footToMetre.pow(2), mathContext)

    val hectareToInch: BigDecimal get() = hectareToMetre.divide(inchToMetre.pow(2), mathContext)

    val hectareToNauticalMile: BigDecimal
        get() = hectareToMetre.divide(metreToNauticalMiles, mathContext)

    val hectareToLeague: BigDecimal
        get() = hectareToNauticalMile.divide(nauticalMileToLeague, mathContext)

    val hectareToChain: BigDecimal get() = hectareToMetre.divide(chainToMetres, mathContext)

    val areToMetre: BigDecimal get() = BigDecimal(100)

    val areToFeet: BigDecimal get() = inverseOf(feetToMetre.scaleByPowerOfTen(-2))

    val areToYard: BigDecimal get() = inverseOf(metreToYard.scaleByPowerOfTen(-2))

    val areToInch: BigDecimal get() = inverseOf(metreToInch.scaleByPowerOfTen(-2))

    val areToNauticalMile: BigDecimal get() = inverseOf(metreToNauticalMiles.scaleByPowerOfTen(-2))

    val areToLeague: BigDecimal get() = inverseOf(leagueToMetre.scaleByPowerOfTen(-2))

    val areToChain: BigDecimal get() = inverseOf(chainToMetres.scaleByPowerOfTen(-2))

    val areToMile: BigDecimal get() = inverseOf(metreToMile.scaleByPowerOfTen(-2))

    val areToAcre: BigDecimal get() = inverseOf(acreToMetre.scaleByPowerOfTen(-2))

    val areToHectare: BigDecimal get() = BigDecimal.ONE.scaleByPowerOfTen(-2)

    val barnToMetre: BigDecimal get() = BigDecimal.ONE.scaleByPowerOfTen(-28)

    val barnToFeet: BigDecimal get() = inverseOf(feetToMetre.scaleByPowerOfTen(28))

    val barnToYard: BigDecimal get() = inverseOf(metreToYard.scaleByPowerOfTen(28))

    val barnToInch: BigDecimal get() = inverseOf(metreToInch.scaleByPowerOfTen(28))

    val barnToNauticalMile: BigDecimal get() = inverseOf(metreToNauticalMiles.scaleByPowerOfTen(28))

    val barnToLeague: BigDecimal get() = inverseOf(leagueToMetre.scaleByPowerOfTen(28))

    val barnToChain: BigDecimal get() = inverseOf(chainToMetres.scaleByPowerOfTen(28))

    val barnToMile: BigDecimal get() = inverseOf(metreToMile.scaleByPowerOfTen(28))

    val barnToAcre: BigDecimal get() = inverseOf(acreToMetre.scaleByPowerOfTen(28))

    val barnToHectare: BigDecimal get() = inverseOf(hectareToMetre.scaleByPowerOfTen(28))

    val barnToAre: BigDecimal get() = inverseOf(areToMetre.scaleByPowerOfTen(28))

    fun amongSquareMetreMap(): SparseIntArray =
        SparseIntArray(8).apply {
            //from square metre perspective
            append(0, 0)
            append(1, 3 * 2)//for kilo
            append(2, 2 * 2) //for hecto
            append(3, 1 * 2)//for deca
            append(4, -1 * 2)//for deci
            append(5, -2 * 2)//for centi
            append(6, -3 * 2)//for milli
            append(7, -6 * 2)//for micro
        }
}