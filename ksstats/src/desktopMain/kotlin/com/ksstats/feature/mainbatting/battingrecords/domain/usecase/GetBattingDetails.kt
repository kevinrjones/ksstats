package com.ksstats.feature.mainbatting.battingrecords.domain.usecase

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.mainbatting.battingrecords.data.BattingSearchResults
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetBattingDetails(val repository: BattingRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<List<BattingSearchResults>> {
        return repository.getBattingDetails(searchParameters)
    }
}