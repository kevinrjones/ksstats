package com.ksstats.core.domain.util

fun String.isMultiInningsType(): Boolean {
    val multiInningsMatchTypes = listOf("t", "f", "wt", "wf", "sec", "minc", "ut")

    return multiInningsMatchTypes.contains(this)
}