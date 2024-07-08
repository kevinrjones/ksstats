package com.ksstats.feature.playerbattingprimarystats.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.feature.playerbattingprimarystats.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetBattingSummary(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<PrimaryBatting>> {
        return repository.getBattingSummary(searchParameters)
    }
}

class GetBattingInningsByInnings(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<PrimaryBatting>> {
        return repository.getBattingInningsByInnings(searchParameters)
    }
}