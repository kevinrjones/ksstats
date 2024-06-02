package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.MatchType
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetMatchTypes(private val repository: MainSearchRepository) {

    operator fun invoke(): Flow<List<MatchType>> {
        return repository.getMatchTypes()
    }
}
