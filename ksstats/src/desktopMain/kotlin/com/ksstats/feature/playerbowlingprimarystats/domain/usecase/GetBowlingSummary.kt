package com.ksstats.feature.playerbowlingprimarystats.domain.usecase

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbowlingprimarystats.data.InningsByInningsBowling
import com.ksstats.feature.playerbowlingprimarystats.domain.repository.BowlingRecordsRepository
import com.ksstats.feature.playerbowlingprimarystats.data.PrimaryBowling
import kotlinx.coroutines.flow.Flow

class GetBowlingSummary(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return repository.getBowlingSummary(searchParameters)
    }
}

class GetBowlingInningsByInnings(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>> {
        return repository.getBowlingInningsByInnings(searchParameters)
    }
}

class GetBowlingMatchTotals(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>> {
        return repository.getMatchTotals(searchParameters)
    }
}

class GetSeriesAverages(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return repository.getSeriesAverages(searchParameters)
    }
}

class GetGroundAverages(val repository: BowlingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        return repository.getGroundAverages(searchParameters)
    }
}
