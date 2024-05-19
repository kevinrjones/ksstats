package com.ksstats.feature.mainbatting.di

import com.ksstats.feature.mainbatting.domain.usecase.BattingUseCases
import com.ksstats.feature.mainbatting.domain.usecase.GetMatchTypes
import com.ksstats.feature.mainbatting.domain.usecase.GetCompetitions
import com.ksstats.feature.mainbatting.domain.usecase.GetTeamsForCompetition
import com.ksstats.feature.mainbatting.domain.usecase.GetGroundsForCompetition
import com.ksstats.feature.mainbatting.domain.usecase.GetCountriesForCompetition
import com.ksstats.feature.mainbatting.domain.usecase.GetSeriesDatesForCompetition
import com.ksstats.core.domain.usecases.GetSearchRecordsPageSizes
import com.ksstats.feature.mainbatting.presentation.BattingRecordsViewModel
import com.ksstats.feature.mainbatting.data.repository.JooqBattingRecordsRepository
import com.ksstats.feature.mainbatting.data.source.BattingRecordsDao
import com.ksstats.feature.mainbatting.data.source.JooqBattingRecordsDao
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun battingRecordsModule() = module {
    singleOf(::GetMatchTypes)
    singleOf(::GetCompetitions)
    singleOf(::GetTeamsForCompetition)
    singleOf(::GetGroundsForCompetition)
    singleOf(::GetCountriesForCompetition)
    singleOf(::GetSeriesDatesForCompetition)
    singleOf(::GetSearchRecordsPageSizes)
    singleOf(::BattingUseCases)
    singleOf(::JooqBattingRecordsRepository) bind BattingRepository::class
    singleOf(::JooqBattingRecordsDao) bind BattingRecordsDao::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
    factory { BattingRecordsViewModel(get()) }
}
