package com.ksstats.feature.summary.domain.model

import com.ksstats.core.types.MatchType

data class SummaryResult(
    val team: String,
    val opponents: String,
    val matchType: MatchType,
    val competition: MatchType,
    val ground: String,
    val hostCountry: String,
)
