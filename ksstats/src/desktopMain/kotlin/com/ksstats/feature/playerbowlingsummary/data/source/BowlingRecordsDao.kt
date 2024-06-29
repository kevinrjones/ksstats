package com.ksstats.feature.playerbowlingsummary.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingsummary.data.BowlingSummary
import kotlinx.coroutines.flow.Flow

interface BowlingRecordsDao {
    fun getBowlingSummary(searchParameters: SearchParameters): Flow<List<BowlingSummary>>
}

