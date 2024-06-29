package com.ksstats.feature.showselection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.feature.showselection.domain.usecase.SelectionUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class SelectionViewModel(val selectionUseCases: SelectionUseCases) : ViewModel() {

    // see this:https://medium.com/@alexandre.genet7/android-viewmodel-repository-room-and-retrofit-with-jetpack-compose-2b652d8ff3b9
    private val _selections: MutableStateFlow<List<StatsSelectionItem>> = MutableStateFlow(listOf())
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

    fun onClick(statsAppScreens: StatsAppScreens) {
        TODO("Not yet implemented")
    }
}

