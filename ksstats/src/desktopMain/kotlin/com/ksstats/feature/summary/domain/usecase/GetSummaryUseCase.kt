package com.ksstats.feature.summary.domain.usecase

import com.ksstats.feature.showselection.domain.repository.SummaryRepository
import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.feature.summary.util.SummarySearchParameters
import kotlinx.coroutines.flow.Flow

class GetSummaryUseCase(val repository: SummaryRepository) {
    operator fun invoke(searchParameters: SummarySearchParameters): Flow<SummaryResult> {
        return repository.getSearchSummary(searchParameters)
    }
}