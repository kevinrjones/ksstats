package com.ksstats.core.domain.util

/*
 matchType: String,
   matchSubType: String,
   teamId: Int?,
   opponentsId: Int?,
   groundId: Int?,
   hostCountryId: Int?,
   venue: Int?,
   sortOrder: Int?,
   sortDirection: String?,
   startDate: Int?,
   endDate: Int?,
   result: Int?,
   limit: Int?,
   startRow: Int?,
   pageSize: Int?
 */

data class SearchParameters(
    val fromNavigation:Boolean = false,
    val matchType: String = "t",
    val matchSubType: String = "t",
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
    val pageSize: Int = 50
)
