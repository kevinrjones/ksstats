package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingprimarystats.data.BowlingSummary
import kotlinx.coroutines.flow.Flow

interface BowlingRecordsDao {
    fun getBowlingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<BowlingSummary>>
    fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<BowlingSummary>>
}

