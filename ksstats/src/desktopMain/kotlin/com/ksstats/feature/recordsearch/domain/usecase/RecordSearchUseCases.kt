package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.core.domain.usecases.GetSearchRecordsPageSizes

data class RecordSearchUseCases(
    val getMatchTypes: GetMatchTypes,
    val getCompetitions: GetCompetitions,
    val getTeamsForCompetitionAndCountry: GetTeamsForCompetitionAndCountry,
    val getGroundsForCompetitionAndCountry: GetGroundsForCompetitionAndCountry,
    val getCountriesForCompetition: GetCountriesForCompetition,
    val getSeriesDatesForCompetition: GetSeriesDatesForCompetition,
    val getStartAndEndDatesForCompetition: GetStartAndEndDatesForCompetition,
    val getPageSizes: GetSearchRecordsPageSizes,
    val getTeamsForCountry: GetTeamsForCountry,
)
