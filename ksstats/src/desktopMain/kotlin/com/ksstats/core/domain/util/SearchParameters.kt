package com.ksstats.core.domain.util

import com.ksstats.core.types.MatchType

data class SearchParameters(
    val matchType: MatchType = MatchType("t"),
    val matchSubType: MatchType = MatchType("t"),
    val teamId: Int? = null,
    val opponentsId: Int? = null,
    val groundId: Int? = null,
    val hostCountryId: Int? = null,
    val venue: Int? = null,
    val sortOrder: SortOrder = SortOrder.Runs,
    val sortDirection: SortDirection = SortDirection.Ascending,
    val startDate: Long = -8520336000,
    val endDate: Long = 253402214400,
    val season: String? = null,
    val result: Int = 0,
    val limit: Int = 50,
    val startRow: Int = 0,
    val pageSize: Int = 50,
    val fivesLimit: Int = 5
)