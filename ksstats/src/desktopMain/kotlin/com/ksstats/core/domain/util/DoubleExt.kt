package com.ksstats.core.domain.util

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.round(places: Int) : String {
    val size = 10.0.pow(places)
    val roundedValue =  (this * size).roundToInt() / size
    return String.format("%.${places}f", roundedValue)
}

fun Double.truncate(places: Int) : String {
    val size = 10.0.pow(places)
    val truncatedValue = kotlin.math.truncate(this * size) / size
    return String.format("%.${places}f", truncatedValue)
}