package com.ksstats.di

import com.ksstats.feature.showselection.di.openingSelectionModule
import org.koin.dsl.module

fun appModule() = module {
    includes(openingSelectionModule())
}