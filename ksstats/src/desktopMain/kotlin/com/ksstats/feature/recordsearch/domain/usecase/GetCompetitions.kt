package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.Competition
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetCompetitions(private val repository: MainSearchRepository) {

    operator fun invoke(matchType: String): Flow<List<Competition>> {
        return repository.getCompetitions(matchType)
    }
}

