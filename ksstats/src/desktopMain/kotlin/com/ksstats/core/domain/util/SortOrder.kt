package com.ksstats.core.domain.util


enum class SortOrder {
    SortNamePart,
    Teams,
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
    Maidens,
    None,
    BBI,
    BBM,
    Econ,
    FiveFor,
    TenFor,
    Wickets,
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