package com.ksstats.feature.mainbatting.domain.repository

import com.ksstats.feature.mainbatting.domain.model.MatchTypes
import kotlinx.coroutines.flow.Flow

interface BattingRepository {
    fun getMatchTypes() : Flow<MatchTypes>
}