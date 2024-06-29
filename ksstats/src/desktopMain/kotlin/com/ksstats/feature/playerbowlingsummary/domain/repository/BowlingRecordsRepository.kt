package com.ksstats.feature.playerbowlingsummary.domain.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingsummary.data.BowlingSummary
import kotlinx.coroutines.flow.Flow

interface BowlingRecordsRepository {
    fun getBowlingSummary(searchParameters: SearchParameters): Flow<List<BowlingSummary>>
}