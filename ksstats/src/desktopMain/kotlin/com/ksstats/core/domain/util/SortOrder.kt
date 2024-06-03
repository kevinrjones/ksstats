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
    None,
}

enum class SortDirection(val direction: String) {
    Ascending(""),
    Descending("desc"),
}
enum class DisplaySortDirection {
    None,
    Ascending,
    Descending,
}