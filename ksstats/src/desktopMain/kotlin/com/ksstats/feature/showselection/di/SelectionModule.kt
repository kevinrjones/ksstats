package com.ksstats.feature.showselection.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.ksstats.feature.showselection.domain.usecase.SelectionUseCases
import com.ksstats.feature.showselection.domain.usecase.GetSelection
import com.ksstats.feature.showselection.presentation.SelectionViewModel

fun selectionModule() = module {
    singleOf(::GetSelection)
    singleOf(::SelectionUseCases)
}

