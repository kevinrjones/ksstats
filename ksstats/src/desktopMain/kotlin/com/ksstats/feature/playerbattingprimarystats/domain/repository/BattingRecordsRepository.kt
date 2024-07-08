package com.ksstats.feature.playerbattingprimarystats.domain.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import kotlinx.coroutines.flow.Flow

interface BattingRecordsRepository {
    fun getBattingSummary(searchParameters: SearchParameters): Flow<List<PrimaryBatting>>
    fun getBattingInningsByInnings(searchParameters: SearchParameters): Flow<List<PrimaryBatting>>
}