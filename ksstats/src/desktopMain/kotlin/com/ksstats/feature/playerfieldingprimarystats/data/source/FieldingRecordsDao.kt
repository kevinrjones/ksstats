package com.ksstats.feature.playerfieldingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerfieldingprimarystats.data.InningsByInningsFielding
import com.ksstats.feature.playerfieldingprimarystats.data.PrimaryFielding
import kotlinx.coroutines.flow.Flow

interface FieldingRecordsDao {
    fun getFieldingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
    fun getFieldingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>>
    fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
    fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>>
    fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
    fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
    fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
    fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
    fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>
}

