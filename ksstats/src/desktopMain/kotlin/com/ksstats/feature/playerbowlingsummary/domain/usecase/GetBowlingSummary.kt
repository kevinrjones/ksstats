package com.ksstats.feature.playerbowlingsummary.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingsummary.domain.repository.BowlingRecordsRepository
import com.ksstats.feature.playerbowlingsummary.data.BowlingSummary
import kotlinx.coroutines.flow.Flow

class GetBowlingSummary(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BowlingSummary>> {
        return repository.getBowlingSummary(searchParameters)
    }
}