package com.ksstats.feature.playersummary.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.shared.data.BattingSearchResults
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerSummary(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BattingSearchResults>> {
        return repository.getPlayerSummary(searchParameters)
    }
}