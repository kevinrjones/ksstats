package com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.battingrecordsdisplay.data.BattingSearchResults
import kotlinx.coroutines.flow.Flow

interface BattingRecordsRepository {
    fun getBattingDetails(searchParameters: SearchParameters): Flow<List<BattingSearchResults>>
}