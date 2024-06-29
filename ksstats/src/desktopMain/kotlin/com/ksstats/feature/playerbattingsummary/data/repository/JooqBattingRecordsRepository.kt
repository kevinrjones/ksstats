package com.ksstats.feature.playerbattingsummary.data.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingsummary.data.BattingSummary
import com.ksstats.feature.playerbattingsummary.data.source.BattingRecordsDao
import com.ksstats.feature.playerbattingsummary.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBattingRecordsRepository(private val battingRecordsDao: BattingRecordsDao) : BattingRecordsRepository {
    override fun getBattingSummary(searchParameters: SearchParameters): Flow<List<BattingSummary>> {
        return battingRecordsDao.getBattingSummary(searchParameters)
    }


}