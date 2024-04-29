package com.ksstats.feature.showselection.presentation.selection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ksstats.core.presentation.ViewModel
import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.feature.showselection.domain.usecase.SelectionUseCases
import androidx.compose.runtime.State
import com.ksstats.core.presentation.StatsAppScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class SelectionViewModel(val selectionUseCases: SelectionUseCases) : ViewModel() {

    // see this:https://medium.com/@alexandre.genet7/android-viewmodel-repository-room-and-retrofit-with-jetpack-compose-2b652d8ff3b9
    private val _selections: MutableStateFlow<List<StatsSelectionItem>> = MutableStateFlow(listOf<StatsSelectionItem>())
    val selections: StateFlow<List<StatsSelectionItem>> = _selections.asStateFlow()
    private var getStatsSelectionsJob: Job? = null

    init {
        getSelectionItems()
    }

    private fun getSelectionItems() {
        getStatsSelectionsJob?.cancel()
        val selections = mutableListOf<StatsSelectionItem>()
        getStatsSelectionsJob = selectionUseCases.getSelection().onEach {
            selections.add(it)
            _selections.value = selections
        }.launchIn(viewModelScope)
    }

    fun onClick(statsAppScreen: StatsAppScreen) {
        TODO("Not yet implemented")
    }
}

