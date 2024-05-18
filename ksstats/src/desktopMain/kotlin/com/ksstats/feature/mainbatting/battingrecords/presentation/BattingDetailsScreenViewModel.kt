package com.ksstats.feature.mainbatting.battingrecords.presentation

import com.ksstats.core.domain.util.SavedStateStore
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.presentation.ViewModel
import com.ksstats.feature.mainbatting.battingrecords.data.BattingSearchResults
import com.ksstats.feature.mainbatting.battingrecords.domain.usecase.BattingDetailsUseCases
import com.ksstats.feature.mainbatting.domain.model.MatchType
import com.ksstats.feature.mainbatting.domain.usecase.BattingUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BattingDetailsScreenViewModel(
    battingDetailsUseCases: BattingDetailsUseCases,
    val savedStateStore: SavedStateStore<SearchParameters>
) : ViewModel() {

    private val _battingSearchResults: MutableStateFlow<List<BattingSearchResults>> =
        MutableStateFlow(listOf<BattingSearchResults>())
    val battingSearchResults: StateFlow<List<BattingSearchResults>> = _battingSearchResults.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val searchParameters = savedStateStore.get("battingSearchParameters") ?: SearchParameters()

                battingDetailsUseCases.getBattingDetails.invoke(searchParameters)
                    .collect {
                        _battingSearchResults.value = it
                    }
            }
        }
    }
}