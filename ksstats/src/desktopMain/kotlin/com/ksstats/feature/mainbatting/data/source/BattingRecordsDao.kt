package com.ksstats.feature.mainbatting.data.source

import com.ksstats.feature.mainbatting.domain.model.MatchTypes
import kotlinx.coroutines.flow.Flow

interface BattingRecordsDao {
    fun getMatchTypes() : Flow<MatchTypes>
}