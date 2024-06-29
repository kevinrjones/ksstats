package com.ksstats.feature.playerbattingsummary.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingsummary.data.BattingSummary
import kotlinx.coroutines.flow.Flow

interface BattingRecordsDao {
    fun getBattingSummary(searchParameters: SearchParameters): Flow<List<BattingSummary>>
}

