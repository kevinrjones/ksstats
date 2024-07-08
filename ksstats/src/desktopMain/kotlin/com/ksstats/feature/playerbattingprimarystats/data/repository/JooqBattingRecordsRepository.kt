package com.ksstats.feature.playerbattingprimarystats.data.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.feature.playerbattingprimarystats.data.source.BattingRecordsDao
import com.ksstats.feature.playerbattingprimarystats.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBattingRecordsRepository(private val battingRecordsDao: BattingRecordsDao) : BattingRecordsRepository {
    override fun getBattingSummary(searchParameters: SearchParameters): Flow<List<PrimaryBatting>> {
        return battingRecordsDao.getBattingSummary(searchParameters)
    }

    override fun getBattingInningsByInnings(searchParameters: SearchParameters): Flow<List<PrimaryBatting>> {
        return battingRecordsDao.getBattingInningsByInnings(searchParameters)
    }

}