package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import com.ksstats.core.presentation.components.DropDownMenuState
import kotlinx.datetime.LocalDate

sealed class MainSearchEvent {

    data class StartDateChangedEvent(val date: LocalDate?) : MainSearchEvent()
    data class EndDateChangedEvent(val date: LocalDate?) : MainSearchEvent()
    data class MatchTypeSelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class CompetitionSelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class MinimumValueChangedEvent(val value: String) : MainSearchEvent()
    data class PageSizeChangedEvent(val value: String) : MainSearchEvent()
    data class TeamSelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class OpponentSelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class CountrySelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class GroundSelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class SeriesDateSelectionEvent(val menuState: DropDownMenuState) : MainSearchEvent()
    data class ResultSelectionEvent(val won: Boolean, val lost: Boolean, val drawn: Boolean, val tied: Boolean) :
        MainSearchEvent()

    data class VenueSelectionEvent(val home: Boolean, val away: Boolean, val neutral: Boolean) : MainSearchEvent()
    data class SearchViewFormatEvent(val format: SearchViewFormat) : MainSearchEvent()
    data object SearchMain : MainSearchEvent()
    data object Reset : MainSearchEvent()
}