package com.ksstats.feature.playerbowlingprimarystats.di

import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.PlayerBowlingPrimaryStatsUseCases
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetBowlingSummary
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetBowlingInningsByInnings
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetBowlingMatchTotals
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetSeriesAverages
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetGroundAverages
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetByHostCountry
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetBySeason
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetByOppositionTeam
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetByYearOfMatchStart
import com.ksstats.feature.playerbowlingprimarystats.data.repository.JooqBowlingRecordsRepository
import com.ksstats.feature.playerbowlingprimarystats.data.source.BowlingRecordsDao
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingRecordsDao
import com.ksstats.feature.playerbowlingprimarystats.domain.repository.BowlingRecordsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerBowlingSummaryModule() = module {
    singleOf(::PlayerBowlingPrimaryStatsUseCases)
    singleOf(::GetBowlingSummary)
    singleOf(::GetBowlingInningsByInnings)
    singleOf(::GetBowlingMatchTotals)
    singleOf(::GetSeriesAverages)
    singleOf(::GetGroundAverages)
    singleOf(::GetByHostCountry)
    singleOf(::GetBySeason)
    singleOf(::GetGroundAverages)
    singleOf(::GetByOppositionTeam)
    singleOf(::GetByYearOfMatchStart)
    singleOf(::JooqBowlingRecordsRepository) bind BowlingRecordsRepository::class
    factory { JooqBowlingRecordsDao(get()) } bind BowlingRecordsDao::class
}

