package com.ksstats.feature.playerbattingprimarystats.data.repository

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.InningsByInningsBatting
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.feature.playerbattingprimarystats.data.source.BattingRecordsDao
import com.ksstats.feature.playerbattingprimarystats.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBattingRecordsRepository(private val battingRecordsDao: BattingRecordsDao) : BattingRecordsRepository {
    override fun getBattingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getBattingSummary(searchParameters)
    }

    override fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getSeriesAverages(searchParameters)
    }

    override fun getBattingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>> {
        return battingRecordsDao.getBattingInningsByInnings(searchParameters)
    }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>> {
        return battingRecordsDao.getMatchTotals(searchParameters)
    }

    override fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getGroundAverages(searchParameters)
    }

    override fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getByHostCountry(searchParameters)
    }

    override fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getByOppositionTeam(searchParameters)
    }

    override fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getByYearOfMatchStart(searchParameters)
    }

    override fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return battingRecordsDao.getBySeason(searchParameters)
    }

}