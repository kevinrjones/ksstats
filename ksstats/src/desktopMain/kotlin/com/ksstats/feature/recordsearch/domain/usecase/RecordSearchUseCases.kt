package com.ksstats.feature.recordsearch.domain.usecase

data class RecordSearchUseCases(
    val initialiseSearch: InitialiseSearchUseCase,
    val getSearchDataForCompetitionAndCountry: GetSearchDataForCompetitionAndCountryUseCase,
    val getCompetitionsForMatchTypeAndCountry: GetCompetitionsForMatchTypeAndCountryUseCase,
    val getTeamsAndGroundsForCompetitionAndCountry: GetTeamsAndGroundsForCompetitionAndCountryUseCase,
)
