package com.ksstats.feature.battingrecordsdisplay.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.battingrecordsdisplay.data.BattingSearchResults
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetBattingDetails(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BattingSearchResults>> {
        return repository.getBattingDetails(searchParameters)
    }
}