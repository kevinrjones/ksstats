package com.ksstats.feature.showselection.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.ksstats.feature.showselection.domain.usecase.SelectionUseCases
import com.ksstats.feature.showselection.domain.usecase.GetSelection
import com.ksstats.feature.showselection.presentation.selection.SelectionViewModel

fun openingSelectionModule() = module {
    singleOf(::GetSelection)
    singleOf(::SelectionUseCases)
    factory { SelectionViewModel(get()) }
}

