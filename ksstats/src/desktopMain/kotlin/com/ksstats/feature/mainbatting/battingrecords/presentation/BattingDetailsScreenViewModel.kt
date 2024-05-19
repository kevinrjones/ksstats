package com.ksstats.feature.mainbatting.battingrecords.presentation

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.presentation.ViewModel
import com.ksstats.feature.mainbatting.battingrecords.data.BattingSearchResults
import com.ksstats.feature.mainbatting.battingrecords.domain.usecase.BattingDetailsUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BattingDetailsScreenViewModel(
    private val battingDetailsUseCases: BattingDetailsUseCases

) : ViewModel() {
    private val _battingSearchResults: MutableStateFlow<List<BattingSearchResults>> =
        MutableStateFlow(listOf())

    val battingSearchResults: StateFlow<List<BattingSearchResults>> = _battingSearchResults.asStateFlow()

    fun getSearchResults(searchParameters: SearchParameters) {
        println("Get search results for $searchParameters")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                battingDetailsUseCases.getBattingDetails.invoke(searchParameters)
                    .collect {
                        _battingSearchResults.value = it
                    }
            }
        }
    }

}