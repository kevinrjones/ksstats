package com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.battingrecordsdisplay.data.BattingSearchResults
import kotlinx.coroutines.flow.Flow

interface BattingRecordsDao {
    fun getBattingDetails(searchParameters: SearchParameters): Flow<List<BattingSearchResults>>
}

