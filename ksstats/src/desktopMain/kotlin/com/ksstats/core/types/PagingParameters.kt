package com.ksstats.core.types

import com.ksstats.core.presentation.components.PageChangedNavigation

data class PagingParameters(val startRow: Int, val pageSize: Int, val limit: Int) {
    fun getLastRow(rowCount: Int) =
        (pageSize.calculateMaxPages(rowCount) * pageSize) - pageSize

    fun calculateNewPagingParameters(
        pageChangedNavigation: PageChangedNavigation,
        rowCount: Int,
    ): PagingParameters {

        val previous = startRow - pageSize
        val next = startRow + pageSize
        val lastRow = getLastRow(rowCount)

        val newPagingParameters = when (pageChangedNavigation) {
            PageChangedNavigation.First -> copy(startRow = 0)
            PageChangedNavigation.Previous -> copy(startRow = previous)
            PageChangedNavigation.Next -> copy(startRow = next)
            PageChangedNavigation.Last -> copy(startRow = lastRow)
            is PageChangedNavigation.PageSizeChange -> copy(pageSize = pageChangedNavigation.pageSize)
        }
        return newPagingParameters
    }

    fun calculatePageNumber(): Int =
         (startRow/pageSize) + 1


}

fun Int.calculateMaxPages(count: Int) = Math.ceil(count / this.toDouble()).toInt()


