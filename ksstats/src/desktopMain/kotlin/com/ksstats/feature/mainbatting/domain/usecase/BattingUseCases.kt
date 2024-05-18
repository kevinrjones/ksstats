package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.core.domain.usecases.GetSearchRecordsPageSizes

data class BattingUseCases(
    val getMatchTypes: GetMatchTypes,
    val getCompetitions: GetCompetitions,
    val getTeamsForCompetition: GetTeamsForCompetition,
    val getGroundsForCompetition: GetGroundsForCompetition,
    val getCountriesForCompetition: GetCountriesForCompetition,
    val getSeriesDatesForCompetition: GetSeriesDatesForCompetition,
    val getPageSizes: GetSearchRecordsPageSizes,
)
