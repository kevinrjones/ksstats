package com.ksstats.feature.summary.data.repository

import com.ksstats.feature.showselection.domain.repository.SummaryRepository
import com.ksstats.feature.summary.data.source.SummaryDao
import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.feature.summary.util.SummarySearchParameters
import kotlinx.coroutines.flow.Flow

class JooqSummaryRepository(private val summaryDao: SummaryDao) : SummaryRepository {
    override fun getSearchSummary(searchParameters: SummarySearchParameters): Flow<SummaryResult> {
        return summaryDao.getSearchSummary(searchParameters)
    }
}