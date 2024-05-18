package com.ksstats.feature.mainbatting.domain.model

data class MatchType(val id: Int, val type: String, val description: String)
data class Competition(val id: Int, val type: String, val subType: String, val competition: String)
data class Team(val id: Int, val name: String)
data class Ground(val id: Int, val name: String)
data class Country(val id: Int, val name: String)
