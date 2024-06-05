package com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.shared.data.BattingSearchResults
import kotlinx.coroutines.flow.Flow

interface BattingRecordsRepository {
    fun getPlayerSummary(searchParameters: SearchParameters): Flow<List<BattingSearchResults>>
}