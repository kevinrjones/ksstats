package com.ksstats.feature.showselection.domain.repository

import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.feature.summary.util.SummarySearchParameters
import kotlinx.coroutines.flow.Flow

interface SummaryRepository {
    fun getSearchSummary(searchParameters: SummarySearchParameters): Flow<SummaryResult>
}
