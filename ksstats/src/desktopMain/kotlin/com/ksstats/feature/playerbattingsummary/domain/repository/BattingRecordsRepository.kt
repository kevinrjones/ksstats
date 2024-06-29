package com.ksstats.feature.playerbattingsummary.domain.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingsummary.data.BattingSummary
import kotlinx.coroutines.flow.Flow

interface BattingRecordsRepository {
    fun getBattingSummary(searchParameters: SearchParameters): Flow<List<BattingSummary>>
}