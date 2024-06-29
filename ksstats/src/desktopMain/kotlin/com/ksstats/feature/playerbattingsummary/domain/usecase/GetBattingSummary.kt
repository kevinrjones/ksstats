package com.ksstats.feature.playerbattingsummary.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingsummary.data.BattingSummary
import com.ksstats.feature.playerbattingsummary.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetBattingSummary(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BattingSummary>> {
        return repository.getBattingSummary(searchParameters)
    }
}