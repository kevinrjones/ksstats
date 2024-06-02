package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.Team
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetTeamsForCountry(private val repository: MainSearchRepository) {

    operator fun invoke(matchType: String, countryId: Int): Flow<List<Team>> {
        return repository.getTeamsForCompetitionAndCountry(matchType, countryId)
    }
}

