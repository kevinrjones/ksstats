package com.ksstats.core.domain.util



enum class SortOrder {
    SortNamePart,
    Team,
    Opponents,
    Runs,
    Balls,
    Year,
    Matches,
    Innings,
    Fours,
    Sixes,
    NotOuts,
    HighestScore,
    Avg,
    Fifties,
    Hundreds,
    Ducks,
    SR,
    BI,
}

enum class SortDirection(val direction: String) {
    Ascending(""),
    Descending("desc"),
}