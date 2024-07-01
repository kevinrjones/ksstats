package com.ksstats.core.domain.util

import com.ksstats.core.types.EpochSeconds
import com.ksstats.core.types.MatchType
import com.ksstats.core.types.PagingParameters

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
    val startDate: EpochSeconds = EpochSeconds.minimum(),
    val endDate: EpochSeconds = EpochSeconds.now(),
    val season: String? = null,
    val result: Int = 0,
    val pagingParameters: PagingParameters,
    val fivesLimit: Int = 5,
)