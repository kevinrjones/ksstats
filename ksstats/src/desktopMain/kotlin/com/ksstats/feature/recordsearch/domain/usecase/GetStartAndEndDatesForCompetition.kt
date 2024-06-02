package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.StartEndDate
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class GetStartAndEndDatesForCompetition(private val repository: MainSearchRepository) {

    operator fun invoke(matchType: String): Flow<StartEndDate> {
        return repository.getStartAndEndDatesForCompetition(matchType)
    }
}