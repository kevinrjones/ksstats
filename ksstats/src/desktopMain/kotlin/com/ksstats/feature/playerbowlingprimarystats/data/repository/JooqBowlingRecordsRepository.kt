package com.ksstats.feature.playerbowlingprimarystats.data.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingprimarystats.data.BowlingSummary
import com.ksstats.feature.playerbowlingprimarystats.data.source.BowlingRecordsDao
import com.ksstats.feature.playerbowlingprimarystats.domain.repository.BowlingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqBowlingRecordsRepository(private val bowlingRecordsDao: BowlingRecordsDao) : BowlingRecordsRepository {
    override fun getBowlingSummary(searchParameters: SearchParameters): Flow<List<BowlingSummary>> {
        return bowlingRecordsDao.getBowlingSummary(searchParameters)
    }

    override fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<List<BowlingSummary>> {
        return bowlingRecordsDao.getBowlingInningsByInnings(searchParameters)
    }
}