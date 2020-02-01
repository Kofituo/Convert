package com.example.unitconverter.constants

import android.util.SparseIntArray
import java.math.BigDecimal

object Length : ConstantsInterface {

    val footToInch: BigDecimal get() = BigDecimal(12)

    val footToMetre: BigDecimal get() = BigDecimal(3048).scaleByPowerOfTen(-4)

    val inchToYard: BigDecimal get() = BigDecimal(36)

    val inchToMetre: BigDecimal get() = footToMetre.divide(footToInch)

    val metreToMile: BigDecimal get() = BigDecimal("1609.344")

    val footToMile: BigDecimal get() = BigDecimal(5280)

    val inchToMile: BigDecimal get() = BigDecimal(63360)

    val mileToYard: BigDecimal get() = inverseOf(BigDecimal(1760))

    val yardToFeet: BigDecimal get() = BigDecimal(3)

    val metresToYard: BigDecimal get() = BigDecimal("0.9144")

    val metreToNauticalMile: BigDecimal get() = BigDecimal(1852)

    val nauticalMileToFoot: BigDecimal get() = metreToNauticalMile.divide(footToMetre, mathContext)

    val nauticalMileToInch: BigDecimal get() = nauticalMileToFoot.multiply(footToInch)

    val nauticalMileToMile: BigDecimal get() = metreToNauticalMile.divide(metreToMile, mathContext)

    val nauticalMileToYard: BigDecimal get() = metreToNauticalMile.divide(metresToYard, mathContext)

    val nauticalLeagueToMetre: BigDecimal get() = metreToNauticalMile.multiply(3)

    val nauticalLeagueToMile: BigDecimal
        get() = nauticalLeagueToMetre.divide(metreToMile, mathContext)

    val nauticalLeagueToFoot: BigDecimal
        get() = nauticalLeagueToMetre.divide(footToMetre, mathContext)

    val nauticalLeagueToInch: BigDecimal get() = nauticalLeagueToFoot.multiply(footToInch)

    val nauticalLeagueToYard: BigDecimal
        get() = nauticalLeagueToMetre.divide(metresToYard, mathContext)

    val nauticalLeagueToNauticalMile: BigDecimal get() = BigDecimal(3)

    val fathomToYard: BigDecimal get() = BigDecimal(2)

    val fathomToFeet: BigDecimal get() = BigDecimal(6)

    val fathomToMetre: BigDecimal get() = BigDecimal(2).multiply(metresToYard)

    val fathomToInch: BigDecimal get() = fathomToFeet.multiply(footToInch)

    val fathomToMile: BigDecimal get() = fathomToMetre.divide(metreToMile, mathContext)

    val fathomToNauticalMile: BigDecimal
        get() = fathomToMetre.divide(metreToNauticalMile, mathContext)

    val fathomToNauticalLeague: BigDecimal get() = fathomToNauticalMile.divide(3, mathContext)

    val rodToYard get() = BigDecimal(11).divide(2) //5.5

    val rodToFeet get() = BigDecimal(33).divide(2) //16.5

    val rodToMile get() = BigDecimal.ONE.divide(320)

    val rodToMetre: BigDecimal get() = rodToFeet.multiply(footToMetre)

    val rodToInch: BigDecimal get() = BigDecimal(198)

    val rodToNauticalMile: BigDecimal get() = rodToMetre.divide(metreToNauticalMile, mathContext)

    val rodToFathom get() = BigDecimal(11).divide(4)

    val rodToLeague: BigDecimal get() = rodToNauticalMile.divide(BigDecimal(3), mathContext)

    val thouToInch: BigDecimal get() = BigDecimal.ONE.scaleByPowerOfTen(-3)

    val thouToYard get() = inverseOf(BigDecimal(36000))

    val thouToFoot get() = inverseOf(BigDecimal(12000))

    val thouToMetre: BigDecimal get() = thouToYard.multiply(metresToYard)

    val thouToMile: BigDecimal get() = inverseOf(BigDecimal(63360000))

    val thouToNauticalMile: BigDecimal get() = thouToMetre.divide(metreToNauticalMile, mathContext)

    val thouToNauticalLeague: BigDecimal
        get() = thouToMetre.divide(metreToNauticalMile.multiply(3), mathContext)

    val thouToFathom: BigDecimal get() = inverseOf(BigDecimal(72000))

    val thouToRod: BigDecimal get() = thouToYard.divide(rodToYard, mathContext)

    val angstromToMetre: BigDecimal get() = BigDecimal.ONE.scaleByPowerOfTen(-10)

    val angstromToFoot: BigDecimal get() = angstromToMetre.divide(footToMetre, mathContext)

    val angstromToInch get() = angstromToFoot.multiply(12)

    val angstromToYard: BigDecimal get() = angstromToFoot.divide(3)

    val angstromToMile get() = angstromToFoot.divide(3 * 1760, mathContext)

    val angstromToNauticalMile get() = angstromToMetre.divide(1852, mathContext)

    val angstromToNauticalLeague get() = angstromToMetre.divide(1852 * 3, mathContext)

    val angstromToFathom get() = angstromToFoot.divide(6)

    val angstromToRod: BigDecimal get() = angstromToFoot.divide(BigDecimal("16.5"), mathContext)

    val angstromToThou: BigDecimal get() = angstromToFoot.divide(thouToFoot, mathContext)

    val planckLengthToMetre: BigDecimal get() = BigDecimal("1.616255").scaleByPowerOfTen(-35)

    val planckLengthToInch: BigDecimal get() = planckLengthToMetre.divide(inchToMetre, mathContext)

    val planckLengthToFoot: BigDecimal get() = planckLengthToMetre.divide(footToMetre, mathContext)

    val planckLengthToYard: BigDecimal
        get() = planckLengthToMetre.divide(footToMetre, mathContext).divide(BigDecimal(3))

    val planckLengthToMile: BigDecimal get() = planckLengthToMetre.divide(metreToMile, mathContext)

    val planckLengthToNauticalMile: BigDecimal
        get() = planckLengthToMetre.divide(metreToNauticalMile, mathContext)

    val planckLengthToLeague get() = planckLengthToMetre.divide(1852 * 3, mathContext)

    val planckLengthToFathom: BigDecimal
        get() = planckLengthToMetre.divide(fathomToMetre, mathContext)//to fathom

    val planckLengthToRod: BigDecimal
        get() = planckLengthToMetre.divide(rodToMetre, mathContext)//to rod

    val planckLengthToThou: BigDecimal get() = planckLengthToMetre.divide(thouToMetre, mathContext)

    val planckLengthToAngstrom: BigDecimal
        get() = planckLengthToMetre.divide(angstromToMetre, mathContext)

    val chainToFeet: BigDecimal get() = BigDecimal(66)

    val chainToYard: BigDecimal get() = BigDecimal(22)

    val chainToRod: BigDecimal get() = BigDecimal(4)

    val chainToMile: BigDecimal get() = inverseOf(BigDecimal(80))

    val chainToInch: BigDecimal get() = BigDecimal(792)

    val chainToNauticalMile: BigDecimal
        get() = chainToMetre.divide(metreToNauticalMile, mathContext)

    val chainToMetre: BigDecimal get() = BigDecimal("20.1168")

    val chainToNauticalLeague: BigDecimal get() = chainToMetre.divide(1852 * 3, mathContext)

    val chainToThou: BigDecimal get() = BigDecimal(792000)

    val chainToFathom: BigDecimal get() = BigDecimal(11)

    val chainToAngstrom: BigDecimal get() = inverseOf(chainToMetre.scaleByPowerOfTen(10))

    val chainToPlanckLength: BigDecimal
        get() = planckLengthToMetre.divide(chainToMetre, mathContext)

    val furlongToMetre: BigDecimal get() = BigDecimal("201.168")

    val furlongToThou: BigDecimal get() = BigDecimal(7920000)

    val furlongToYard: BigDecimal get() = BigDecimal(220)

    val furlongToFeet: BigDecimal get() = BigDecimal(660)

    val furlongToRod: BigDecimal get() = BigDecimal(40)

    val furlongToMile: BigDecimal get() = inverseOf(BigDecimal(8))

    val furlongToInch: BigDecimal get() = BigDecimal(7920)

    val furlongToNauticalMile: BigDecimal
        get() = furlongToMetre.divide(metreToNauticalMile, mathContext)

    val furlongToNauticalLeague: BigDecimal get() = furlongToMetre.divide(1852 * 3, mathContext)

    val furlongToFathom: BigDecimal get() = BigDecimal(110)

    val furlongToChain: BigDecimal get() = BigDecimal(10)

    val furlongToAngstrom: BigDecimal get() = inverseOf(furlongToMetre.scaleByPowerOfTen(10))

    val furlongPlanckLength: BigDecimal
        get() = planckLengthToMetre.divide(furlongToMetre, mathContext)

    val lyToMetre: BigDecimal get() = BigDecimal(9460730472580800)

    val lyToMile: BigDecimal get() = lyToMetre.divide(metreToMile, mathContext)//to mile

    val lyToYard: BigDecimal get() = lyToMetre.divide(metresToYard, mathContext)//to yard

    val lyToFoot: BigDecimal get() = lyToMetre.divide(footToMetre, mathContext)//to foot

    val lyToInch: BigDecimal get() = lyToMetre.divide(inchToMetre, mathContext)//to inch

    val lyToNauticalMile: BigDecimal
        get() = lyToMetre.divide(metreToNauticalMile, mathContext)//to nautical mile

    val lyToLeague: BigDecimal
        get() = lyToMetre.divide(BigDecimal(1852 * 3), mathContext)//to league

    val lyToRod: BigDecimal get() = lyToMetre.divide(rodToMetre, mathContext)//to rod

    val lyToFathom: BigDecimal get() = lyToMetre.divide(fathomToMetre, mathContext)//to fathom

    val lyToChain: BigDecimal get() = lyToMetre.divide(chainToMetre, mathContext)//to chain

    val lyToFurlong: BigDecimal get() = lyToMetre.divide(furlongToMetre, mathContext)//to furlong

    val lyToThou: BigDecimal get() = lyToMetre.divide(thouToMetre, mathContext)//to thou

    val lyToPlanck: BigDecimal get() = lyToMetre.divide(planckLengthToMetre, mathContext)//to planck

    val lyToAngstrom: BigDecimal get() = lyToMetre.divide(angstromToMetre, mathContext)//to angstrom

    fun metreConversions(): SparseIntArray =
        Mass.buildPrefixMass()
}