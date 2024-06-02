package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetSeriesDatesForCompetition(private val repository: MainSearchRepository) {

    operator fun invoke(matchType: String): Flow<List<String>> {
        return repository.getSeriesDatesForCompetition(matchType)
    }
}

