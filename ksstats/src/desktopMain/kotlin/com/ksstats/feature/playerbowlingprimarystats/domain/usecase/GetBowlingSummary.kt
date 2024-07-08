package com.ksstats.feature.playerbowlingprimarystats.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingprimarystats.domain.repository.BowlingRecordsRepository
import com.ksstats.feature.playerbowlingprimarystats.data.BowlingSummary
import kotlinx.coroutines.flow.Flow

class GetBowlingSummary(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BowlingSummary>> {
        return repository.getBowlingSummary(searchParameters)
    }
}

class GetBowlingInningsByInnings(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BowlingSummary>> {
        return repository.getBowlingInningsByInnings(searchParameters)
    }
}
