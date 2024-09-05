package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.InningsByInningsBatting
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.feature.playerbowlingprimarystats.data.InningsByInningsBowling
import com.ksstats.feature.playerbowlingprimarystats.data.PrimaryBowling
import kotlinx.coroutines.flow.Flow

interface BowlingRecordsDao {
    fun getBowlingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>>
    fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>>
    fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>>
    fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
    fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>>
}

