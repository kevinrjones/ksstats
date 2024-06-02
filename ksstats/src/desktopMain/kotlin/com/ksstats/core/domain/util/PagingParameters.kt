package com.ksstats.core.domain.util

data class PagingParameters(val startRow: Int, val pageSize: Int, val limit: Int)

fun Int.calculateMaxPages(count: Int) = Math.ceil(count / this.toDouble()).toInt()
