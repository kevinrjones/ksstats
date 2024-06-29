package com.ksstats.feature.playerbowlingsummary.data.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingsummary.data.BowlingSummary
import com.ksstats.feature.playerbowlingsummary.data.source.BowlingRecordsDao
import com.ksstats.feature.playerbowlingsummary.domain.repository.BowlingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBowlingRecordsRepository(private val bowlingRecordsDao: BowlingRecordsDao) : BowlingRecordsRepository {
    override fun getBowlingSummary(searchParameters: SearchParameters): Flow<List<BowlingSummary>> {
        return bowlingRecordsDao.getBowlingSummary(searchParameters)
    }
}