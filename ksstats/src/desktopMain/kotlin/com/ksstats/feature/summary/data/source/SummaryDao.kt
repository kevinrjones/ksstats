package com.ksstats.feature.summary.data.source

import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.feature.summary.util.SummarySearchParameters
import kotlinx.coroutines.flow.Flow

interface SummaryDao {
    fun getSearchSummary(searchParameters: SummarySearchParameters) : Flow<SummaryResult>
}