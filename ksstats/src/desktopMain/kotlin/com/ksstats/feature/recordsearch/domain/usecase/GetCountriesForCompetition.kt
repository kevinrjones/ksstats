package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.Country
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetCountriesForCompetition(private val repository: MainSearchRepository) {

    operator fun invoke(matchType: String): Flow<List<Country>> {
        return repository.getCountriesForCompetition(matchType)
    }
}

