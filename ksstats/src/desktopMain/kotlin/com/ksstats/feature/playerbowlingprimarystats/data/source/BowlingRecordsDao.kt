package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingprimarystats.data.InningsByInningsBowling
import com.ksstats.feature.playerbowlingprimarystats.data.PrimaryBowling
import kotlinx.coroutines.flow.Flow

interface BowlingRecordsDao {
    fun getBowlingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>>
    fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>>
    fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
}

