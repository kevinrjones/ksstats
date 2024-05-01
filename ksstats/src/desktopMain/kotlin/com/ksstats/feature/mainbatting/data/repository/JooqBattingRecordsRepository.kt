package com.ksstats.feature.mainbatting.data.repository

import com.ksstats.feature.mainbatting.data.source.BattingRecordsDao
import com.ksstats.feature.mainbatting.domain.model.MatchTypes
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class JooqBattingRecordsRepository(private val battingRecordsDao: BattingRecordsDao) : BattingRepository {
    override fun getMatchTypes(): Flow<MatchTypes> {
        return battingRecordsDao.getMatchTypes()
    }
}