package com.ksstats.feature.summary.util

import com.ksstats.core.types.MatchType

data class SummarySearchParameters(
    val matchType: MatchType,
    val matchSubType: MatchType,
    val teamId: Int,
    val opponentsId: Int,
    val groundId: Int,
    val hostCountryId: Int,
)
