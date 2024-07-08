package com.ksstats.feature.playerbattingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import kotlinx.coroutines.flow.Flow

interface BattingRecordsDao {
    fun getBattingSummary(searchParameters: SearchParameters): Flow<List<PrimaryBatting>>
    fun getBattingInningsByInnings(searchParameters: SearchParameters): Flow<List<PrimaryBatting>>
}

