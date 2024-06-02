package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.Ground
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetGroundsForCompetitionAndCountry(private val repository: MainSearchRepository) {

    operator fun invoke(matchType: String, countryId: Int): Flow<List<Ground>> {
        return repository.getGroundsForCompetitionAndCountry(matchType, countryId)
    }
}

