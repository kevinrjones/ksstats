package com.ksstats.feature.mainbatting.presentation

import com.ksstats.core.presentation.ViewModel
import com.ksstats.feature.mainbatting.domain.model.MatchTypes
import com.ksstats.feature.mainbatting.domain.usecase.BattingUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BattingRecordsViewModel(val battingUseCases: BattingUseCases) : ViewModel() {

    private val _matchTypes: MutableStateFlow<List<MatchTypes>> = MutableStateFlow(listOf<MatchTypes>())
    val matchTypes: StateFlow<List<MatchTypes>> = _matchTypes.asStateFlow()

    init {
        val f = battingUseCases.getMatchTypes()
        this.viewModelScope.launch {
            f.collect {
            }
        }
    }

    suspend fun getMatchTypes(): Flow<List<MatchTypes>> {
        return matchTypes
    }
}