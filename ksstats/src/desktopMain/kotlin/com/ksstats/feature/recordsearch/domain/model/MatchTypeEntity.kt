package com.ksstats.feature.recordsearch.domain.model

data class MatchTypeEntity(val id: Int, val type: String, val description: String)
data class Competition(val id: Int, val type: String, val subType: String, val competition: String)
data class Team(val id: Int, val name: String)
data class Ground(val id: Int, val name: String)
data class Country(val id: Int, val name: String) {
    companion object {
        fun defaultCountry(): Country =
            Country(id = 0, name = "All")

    }
}

data class SearchData(
    val matchTypes: List<MatchTypeEntity> = listOf(),
    val pageSizes: List<Int> = listOf(),
    val competitions: List<Competition> = listOf(),
    val teams: List<Team> = listOf(),
    val grounds: List<Ground> = listOf(),
    val countries: List<Country> = listOf(),
    val seriesDates: List<String> = listOf(),
    val startAndEndDate: StartEndDate = StartEndDate(),
)
