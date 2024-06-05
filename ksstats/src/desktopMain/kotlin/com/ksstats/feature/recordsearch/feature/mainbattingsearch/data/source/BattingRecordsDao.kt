package com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.shared.data.BattingSearchResults
import kotlinx.coroutines.flow.Flow

interface BattingRecordsDao {
    fun getPlayerSummary(searchParameters: SearchParameters): Flow<List<BattingSearchResults>>
}

