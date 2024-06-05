package com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.shared.data.BattingSearchResults
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.BattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBattingRecordsRepository(private val battingRecordsDao: BattingRecordsDao) : BattingRecordsRepository {
    override fun getPlayerSummary(searchParameters: SearchParameters): Flow<List<BattingSearchResults>> {
        return battingRecordsDao.getPlayerSummary(searchParameters)
    }


}