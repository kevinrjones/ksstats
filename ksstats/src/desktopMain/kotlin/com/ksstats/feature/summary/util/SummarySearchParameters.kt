package com.ksstats.feature.summary.util

data class SummarySearchParameters(
    val matchType: String,
    val matchSubType: String,
    val teamId: Int,
    val opponentsId: Int,
    val groundId: Int,
    val hostCountryId: Int,
)
