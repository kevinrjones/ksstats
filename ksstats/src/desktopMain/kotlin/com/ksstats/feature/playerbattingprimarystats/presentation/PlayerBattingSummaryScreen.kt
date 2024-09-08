package com.ksstats.feature.playerbattingprimarystats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.*
import com.ksstats.core.types.*
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.feature.playerbattingprimarystats.domain.usecase.PlayerBattingPrimaryStatsUseCases
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import com.ksstats.shared.utils.buildDetailsScreenNavUrl
import com.ksstats.shared.utils.buildRecordsScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.playerBattingSummaryScreen(
    navigate: (String) -> Unit,
    screen: StatsAppScreens,
    title: StringResource,
) {

    composable(
        route = buildSummaryScreenRoute(screen),
        arguments = buildRecordsScreenNavArguments(100)
    ) { navBackStackEntry ->

        var pagingParameters by remember { mutableStateOf(PagingParameters(startRow = 0, pageSize = 50, limit = 100)) }
        var searchParameters by remember { mutableStateOf(SearchParameters(pagingParameters = pagingParameters)) }
        var summarySearchParameters by remember {
            mutableStateOf(
                SummarySearchParameters(
                    MatchType.default(),
                    MatchType.default(),
                    0,
                    0,
                    0,
                    0
                )
            )
        }


        val battingUseCases: PlayerBattingPrimaryStatsUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: PlayerBattingSummaryScreenViewModel = viewModel {
            PlayerBattingSummaryScreenViewModel(battingUseCases, summaryUseCases, screen)
        }
        val summary = viewModel.summary.collectAsState()

        LaunchedEffect(Unit) {
            val matchType = (navBackStackEntry.arguments?.getString("matchType") ?: "t").toMatchType()
            val matchSubType = (navBackStackEntry.arguments?.getString("matchSubType") ?: "t").toMatchType()
            val teamId = navBackStackEntry.arguments?.getInt("teamId") ?: 0
            val opponentsId = navBackStackEntry.arguments?.getInt("opponentsId") ?: 0
            val groundId = navBackStackEntry.arguments?.getInt("groundId") ?: 0
            val hostCountryId = navBackStackEntry.arguments?.getInt("hostCountryId") ?: 0
            val startDate = navBackStackEntry.arguments?.getLong("startDate") ?: -99999999999L
            val endDate = navBackStackEntry.arguments?.getLong("endDate") ?: 999999999999L

            pagingParameters = pagingParameters.copy(
                startRow = navBackStackEntry.arguments?.getInt("startRow") ?: pagingParameters.startRow,
                pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: pagingParameters.pageSize,
                limit = navBackStackEntry.arguments?.getInt("limit") ?: pagingParameters.limit
            )

            searchParameters = SearchParameters(
                matchType = matchType,
                matchSubType = matchSubType,
                teamId = teamId,
                opponentsId = opponentsId,
                groundId = groundId,
                hostCountryId = hostCountryId,
                venue = navBackStackEntry.arguments?.getInt("venue") ?: 0,
                sortOrder = SortOrder.entries[navBackStackEntry.arguments?.getInt("sortOrder") ?: 3],
                sortDirection = SortDirection.valueOf(
                    navBackStackEntry.arguments?.getString("sortDirection") ?: "DESC"
                ),
                startDate = startDate.toEpochSeconds(),
                endDate = endDate.toEpochSeconds(),
                season = navBackStackEntry.arguments?.getString("season") ?: "All",
                result = navBackStackEntry.arguments?.getInt("result") ?: 0,
                pagingParameters = pagingParameters,
            )

            summarySearchParameters = SummarySearchParameters(
                matchType = matchType,
                matchSubType = matchSubType,
                teamId = teamId,
                opponentsId = opponentsId,
                groundId = groundId,
                hostCountryId = hostCountryId,
            )

            viewModel.getSearchResults(searchParameters)

            viewModel.getSummary(
                searchParameters = SummarySearchParameters(
                    matchType,
                    matchSubType,
                    teamId,
                    opponentsId,
                    groundId,
                    hostCountryId
                ),
                startDate = startDate,
                endDate = endDate
            )

        }

        val searchResults = viewModel.battingSummary.collectAsState()
        val searching = viewModel.searching.collectAsState()

        val count = searchResults.value.count
        val displayRecords = getDisplayRecords(searchResults.value.data, pagingParameters.startRow)

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)

        PlayerBattingSummaryScreen(
            displayRecords = displayRecords,
            recordScreen = screen,
            searching = searching,
            title = stringResource(title),
            summary = summaryString,
            pageNumber = pagingParameters.calculatePageNumber(),
            pageSize = pagingParameters.pageSize,
            startRow = navBackStackEntry.arguments?.getInt("startRow") ?: 0,
            limit = navBackStackEntry.arguments?.getInt("limit") ?: 100,
            rowCount = count,
            sortOrder = searchParameters.sortOrder,
            sortDirection = searchParameters.sortDirection,
            onPageChanged = {
                pagingParameters = pagingParameters.calculateNewPagingParameters(it, count)
                searchParameters = searchParameters.copy(
                    pagingParameters = pagingParameters
                )

                val navUrl = buildDetailsScreenNavUrl(screen.name, searchParameters)
                navigate(navUrl)
            },
            onSort = { order ->

                // reverse the sort direction if the order is the same otherwise keep it the same
                val sortDirectionText = navBackStackEntry.arguments?.getString("sortDirection") ?: "DESC"
                val sortOrderFromNav = SortOrder.entries[navBackStackEntry.arguments?.getInt("sortOrder") ?: 3]

                val sortDirection = if (sortOrderFromNav == order) {
                    if (sortDirectionText.lowercase() == "ascending") {
                        SortDirection.Descending
                    } else {
                        SortDirection.Ascending
                    }
                } else {
                    SortDirection.valueOf(sortDirectionText)
                }

                searchParameters = searchParameters.copy(
                    sortOrder = order,
                    sortDirection = sortDirection
                )

                val navUrl = buildDetailsScreenNavUrl(screen.name, searchParameters)
                navigate(navUrl)

            }
        )
    }
}


private fun getDisplayRecords(searchResults: List<PrimaryBatting>, startRow: Int): List<Map<String, String>> {
    return searchResults.mapIndexed { index, searchResult ->

        val highestScore = getHighestScoreString(searchResult.highestScore)

        mapOf(
            "index" to (index + startRow + 1).toString(),
            "name" to searchResult.name,
            "team" to searchResult.team,
            "opponents" to searchResult.opponents,
            "matches" to searchResult.matches.toString(),
            "innings" to searchResult.innings.toString(),
            "notouts" to searchResult.notOuts.toString(),
            "runs" to searchResult.runs.toString(),
            "hs" to highestScore,
            "avg" to searchResult.average.truncate(2),
            "hundreds" to searchResult.hundreds.toString(),
            "fifties" to searchResult.fifties.toString(),
            "ducks" to searchResult.ducks.toString(),
            "fours" to searchResult.fours.toString(),
            "sixes" to searchResult.sixes.toString(),
            "balls" to searchResult.balls.toString(),
            "sr" to searchResult.strikeRate.round(2),
            "bi" to searchResult.battingImpact.round(3),
            "year" to searchResult.year,
            "ground" to getGroundName(searchResult.ground, searchResult.countryName),
            "countryname" to searchResult.countryName,
        )
    }
}

fun getGroundName(ground: String, countryName: String): String {
    if(ground.isEmpty()) return ""
    if(countryName.isEmpty()) return ground
    return "$ground ($countryName)"
}


private fun getHighestScoreString(highestScore: Double): String {
    val hsWhole = highestScore.toInt()
    val hsPart = Math.ceil(highestScore).toInt()

    if (hsWhole == hsPart) {
        return hsWhole.toString()
    } else {
        return "${hsWhole}*"
    }
}

@Composable
fun PlayerBattingSummaryScreen(
    displayRecords: List<Map<String, String>>,
    searching: State<Boolean>,
    recordScreen: StatsAppScreens,
    summary: String,
    title: String,
    pageNumber: Int,
    pageSize: Int,
    startRow: Int,
    limit: Int,
    rowCount: Int,
    sortOrder: SortOrder,
    sortDirection: SortDirection,
    onPageChanged: (PageChangedNavigation) -> Unit,
    onSort: (SortOrder) -> Unit,
) {

    Box(
        modifier = Modifier
            .height(0.dp)
            .background(Color.Transparent)
    ) {

        Column(
            // initial height needed as the grid is empty, setting it to 0 is fine (it appears!)
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            TitleRow(title = title)
            SummaryRow(summary = summary)
            SearchLimitRow(searchLimit = limit)
            NavigationRow(
                pageNumber = pageNumber,
                pageSize = pageSize,
                maxPages = pageSize.calculateMaxPages(rowCount),
                firstRowNumber = startRow + 1,
                lastRowNumber = rowCount,
                onPageChanged = { onPageChanged(it) }
            )

            Row {
                val metaData = mapOf(
                    "index" to ColumnMetaData("", 60.dp),
                    "name" to ColumnMetaData(
                        "Name",
                        170.dp,
                        sortOrder = SortOrder.SortNamePart,
                        sortDirection = if (sortOrder == SortOrder.SortNamePart) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "team" to when (recordScreen) {
                        StatsAppScreens.BowlingPlayerSummary ->
                            ColumnMetaData(
                                "Teams", 200.dp, sortOrder = SortOrder.Team,
                                sortDirection = if (sortOrder == SortOrder.Team) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        StatsAppScreens.BattingMatchTotals ->
                            ColumnMetaData(
                                "Teams", 200.dp, sortOrder = SortOrder.Team,
                                sortDirection = if (sortOrder == SortOrder.Team) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        else ->
                            ColumnMetaData(
                                "Team", 200.dp, sortOrder = SortOrder.Team,
                                sortDirection = if (sortOrder == SortOrder.Team) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )
                    },
                    "opponents" to when (recordScreen) {
                        StatsAppScreens.BattingSeriesAverages ->
                            ColumnMetaData(
                                "Opponents", 200.dp, sortOrder = SortOrder.Opponents,
                                sortDirection = if (sortOrder == SortOrder.Opponents) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )
                        StatsAppScreens.BattingByOppositionTeam ->
                            ColumnMetaData(
                                "Opponents", 200.dp, sortOrder = SortOrder.Opponents,
                                sortDirection = if (sortOrder == SortOrder.Opponents) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        else -> null
                    },
                    "ground" to when (recordScreen) {
                        StatsAppScreens.BattingGroundAverages ->
                            ColumnMetaData(
                                "Ground", 400.dp, sortOrder = SortOrder.Ground,
                                sortDirection = if (sortOrder == SortOrder.Ground) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        else -> null
                    },
                    "countryname" to when (recordScreen) {
                        StatsAppScreens.BattingByHostCountry ->
                            ColumnMetaData(
                                "Host Country", 200.dp, sortOrder = SortOrder.CountryName,
                                sortDirection = if (sortOrder == SortOrder.CountryName) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        else -> null
                    },
                    "year" to when (recordScreen) {
                        StatsAppScreens.BattingSeriesAverages ->
                            ColumnMetaData(
                                "Year", 200.dp, sortOrder = SortOrder.Year,
                                sortDirection = if (sortOrder == SortOrder.Year) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        StatsAppScreens.BattingByYear ->
                            ColumnMetaData(
                                "Year", 200.dp, sortOrder = SortOrder.Year,
                                sortDirection = if (sortOrder == SortOrder.Year) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )

                        StatsAppScreens.BattingBySeason ->
                            ColumnMetaData(
                                "Year", 200.dp, sortOrder = SortOrder.Year,
                                sortDirection = if (sortOrder == SortOrder.Year) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )


                        else -> null
                    },
                    "matches" to ColumnMetaData(
                        "M",
                        70.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Matches,
                        sortDirection = if (sortOrder == SortOrder.Matches) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }

                    ),
                    "innings" to ColumnMetaData(
                        "I",
                        70.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Innings,
                        sortDirection = if (sortOrder == SortOrder.Innings) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }

                    ),
                    "notouts" to ColumnMetaData(
                        name = "N/Os",
                        width = 90.dp,
                        replaceZero = false,
                        align = TextAlign.End,
                        sortOrder = SortOrder.NotOuts,
                        sortDirection = if (sortOrder == SortOrder.NotOuts) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }

                    ),
                    "runs" to ColumnMetaData(
                        "Runs", 100.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Runs,
                        sortDirection = if (sortOrder == SortOrder.Runs) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "hs" to ColumnMetaData(
                        "HS",
                        90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.HighestScore,
                        sortDirection = if (sortOrder == SortOrder.HighestScore) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }

                    ),
                    "avg" to ColumnMetaData(
                        "Avg", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Avg,
                        sortDirection = if (sortOrder == SortOrder.Avg) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "hundreds" to ColumnMetaData(
                        "100s", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Hundreds,
                        sortDirection = if (sortOrder == SortOrder.Hundreds) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "fifties" to ColumnMetaData(
                        "50s", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Fifties,
                        sortDirection = if (sortOrder == SortOrder.Fifties) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "ducks" to ColumnMetaData(
                        "0s", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Ducks,
                        sortDirection = if (sortOrder == SortOrder.Ducks) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "fours" to ColumnMetaData(
                        "4s", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Fours,
                        sortDirection = if (sortOrder == SortOrder.Fours) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "sixes" to ColumnMetaData(
                        "6s", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Sixes,
                        sortDirection = if (sortOrder == SortOrder.Sixes) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "balls" to ColumnMetaData(
                        "Balls", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Balls,
                        sortDirection = if (sortOrder == SortOrder.Balls) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "sr" to ColumnMetaData(
                        "SR", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.SR,
                        sortDirection = if (sortOrder == SortOrder.SR) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "bi" to ColumnMetaData(
                        "BI", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.BI,
                        sortDirection = if (sortOrder == SortOrder.BI) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    )
                )
                Table(
                    rowCount = displayRecords.size,
                    cellContent = { key, row ->
                        if (displayRecords.isNotEmpty()) {
                            getContent(displayRecords, key, row, metaData)
                        }
                        else
                            null
                    },
                    metaData = metaData,
                    onSort = { order ->
                        onSort(order)
                    }
                )
            }
        }
        if (searching.value) {
            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // this prevents the pointer events propagating to the child composables (the Column in this case)
                }
                .background(Color.Gray.copy(alpha = 0.5F))
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}




