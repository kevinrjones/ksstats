package com.ksstats.feature.summary.domain.model

data class SummaryResult(
    val team: String,
    val opponents: String,
    val matchType: String,
    val competition: String,
    val ground: String,
    val hostCountry: String,
)
