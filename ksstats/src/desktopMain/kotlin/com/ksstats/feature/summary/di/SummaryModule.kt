package com.ksstats.feature.summary.di

import com.ksstats.feature.showselection.domain.repository.SummaryRepository
import com.ksstats.feature.summary.data.repository.JooqSummaryRepository
import com.ksstats.feature.summary.data.source.JooqBattingSummaryDao
import com.ksstats.feature.summary.data.source.SummaryDao
import com.ksstats.feature.summary.domain.usecase.GetSummaryUseCase
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun summaryModule() = module {
    singleOf(::SummaryUseCases)
    singleOf(::GetSummaryUseCase)
    singleOf(::JooqSummaryRepository) bind SummaryRepository::class
    factory { JooqBattingSummaryDao(get()) } bind SummaryDao::class
}
