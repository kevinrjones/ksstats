package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetSeriesDatesForCompetition(private val repository: BattingRepository) {

    operator fun invoke(matchType: String): Flow<List<String>> {
        return repository.getSeriesDatesForCompetition(matchType)
    }
}

