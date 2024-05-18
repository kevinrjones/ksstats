package com.ksstats.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class GetSearchRecordsPageSizes() {
    operator fun invoke(): Flow<List<Int>> {
        return listOf(listOf(50, 100, 150, 200)).asFlow()
    }
}