package com.ksstats.feature.playerbowlingprimarystats.data.repository

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingprimarystats.data.InningsByInningsBowling
import com.ksstats.feature.playerbowlingprimarystats.data.PrimaryBowling
import com.ksstats.feature.playerbowlingprimarystats.data.source.BowlingRecordsDao
import com.ksstats.feature.playerbowlingprimarystats.domain.repository.BowlingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBowlingRecordsRepository(private val bowlingRecordsDao: BowlingRecordsDao) : BowlingRecordsRepository {
    override fun getBowlingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getBowlingSummary(searchParameters)
    }

    override fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>> {
        return bowlingRecordsDao.getBowlingInningsByInnings(searchParameters)
    }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>> {
        return bowlingRecordsDao.getMatchTotals(searchParameters)
    }

    override fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getSeriesAverages(searchParameters)
    }

    override fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getGroundAverages(searchParameters)
    }

    override fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getByHostCountry(searchParameters)
    }

    override fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getByOppositionTeam(searchParameters)
    }

    override fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getByYearOfMatchStart(searchParameters)
    }

    override fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return bowlingRecordsDao.getBySeason(searchParameters)
    }
}