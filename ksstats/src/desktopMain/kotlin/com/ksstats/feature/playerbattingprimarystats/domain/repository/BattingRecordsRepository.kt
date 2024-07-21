package com.ksstats.feature.playerbattingprimarystats.domain.repository

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.InningsByInningsBatting
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import kotlinx.coroutines.flow.Flow

interface BattingRecordsRepository {
    fun getBattingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getBattingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>>
    fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>>
    fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
}