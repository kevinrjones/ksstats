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
    Average,
    Fifties,
    Hundreds,
    Ducks,
    strikeRate,
    BI,
    Maidens,
    None,
    BBI,
    BBM,
    Econ,
    FiveFor,
    TenFor,
    Wickets,
    MatchStartDateAsOffset,
    Score,
    Minutes,
    Ground,
    Location,
    CountryName,
    Overs,
    BallsPerOver,
    Dismissals,
    WicketKeepingDismissals,
    Caught,
    Stumpings,
    CaughtFielder,
    CaughtWicketKeeper,
    BestDismissals,
    BestCaughtFielder,
    BestCaughtKeeper,
    BestStumpings,
    Won,
    Drawn,
    Lost,
    Tied,
    rpo,
    Played,
    Avg,
    Result,
    StartDate,
    InningsOrder,
    BallsBowled,
    Total,
    HowMuch,
    Series,
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