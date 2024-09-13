package com.ksstats.feature.playerfieldingprimarystats.presentation

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
import com.ksstats.feature.playerfieldingprimarystats.data.PrimaryFielding
import com.ksstats.feature.playerfieldingprimarystats.domain.usecase.PlayerFieldingPrimaryStatsUseCases
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import com.ksstats.shared.utils.buildDetailsScreenNavUrl
import com.ksstats.shared.utils.buildRecordsScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.playerFieldingSummaryScreen(
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


        val fieldingUseCases: PlayerFieldingPrimaryStatsUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: PlayerFieldingSummaryScreenViewModel = viewModel {
            PlayerFieldingSummaryScreenViewModel(fieldingUseCases, summaryUseCases, screen)
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

        val searchResults = viewModel.fieldingSummary.collectAsState()
        val searching = viewModel.searching.collectAsState()

        val count = searchResults.value.count
        val displayRecords = getDisplayRecords(searchResults.value.data, pagingParameters.startRow)

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)

        PlayerFieldingSummaryScreen(
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


private fun getDisplayRecords(searchResults: List<PrimaryFielding>, startRow: Int): List<Map<String, String>> {
    return searchResults.mapIndexed { index, searchResult ->

        val bestDismissals = getBestDismissals(searchResult.bestCaughtF, searchResult.bestCaughtWk, searchResult.bestStumpings)

        mapOf(
            "index" to (index + startRow + 1).toString(),
            "name" to searchResult.name,
            "team" to searchResult.team,
            "opponents" to searchResult.opponents,
            "matches" to searchResult.matches.toString(),
            "innings" to searchResult.innings.toString(),
            "year" to searchResult.year,
            "ground" to getGroundName(searchResult.ground, searchResult.countryName),
            "countryname" to searchResult.countryName,
            "dismissals" to searchResult.dismissals.toString(),
            "stumped" to searchResult.stumped.toString(),
            "caughtwk" to searchResult.caughtWk.toString(),
            "caughtf" to searchResult.caughtF.toString(),
            "caught" to searchResult.caught.toString(),
            "wicketkeeperdismissals" to searchResult.wicketKeeperDismissals.toString(),
            "bestDismissals" to bestDismissals
        )
    }
}

fun getGroundName(ground: String, countryName: String): String {
    if(ground.isEmpty()) return ""
    if(countryName.isEmpty()) return ground
    return "$ground ($countryName)"
}


private fun getBestDismissals(bestCaughtF: Int, bestCaughtWk: Int, bestStumped: Int): String {
    if(bestCaughtF == 0)
        return "${bestStumped + bestCaughtWk} (${bestCaughtWk} c, ${bestStumped} st)"
    else
        return "${bestStumped + bestCaughtF} (${bestCaughtF} c, ${bestStumped} st)"
}

@Composable
fun PlayerFieldingSummaryScreen(
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

                        StatsAppScreens.FieldingMatchTotals ->
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
                        StatsAppScreens.FieldingSeriesAverages ->
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
                        StatsAppScreens.FieldingByOppositionTeam ->
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
                        StatsAppScreens.FieldingGroundAverages ->
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
                        StatsAppScreens.FieldingByHostCountry ->
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
                        StatsAppScreens.FieldingSeriesAverages ->
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

                        StatsAppScreens.FieldingByYear ->
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

                        StatsAppScreens.FieldingBySeason ->
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
                    "dismissals" to ColumnMetaData(
                        name = "Dismissals",
                        width = 90.dp,
                        replaceZero = false,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Dismissals,
                        sortDirection = if (sortOrder == SortOrder.Dismissals) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }

                    ),
                    "stumped" to ColumnMetaData(
                        "Stumped", 100.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Stumpings,
                        sortDirection = if (sortOrder == SortOrder.Stumpings) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "caughtwk" to ColumnMetaData(
                        "Caught WK",
                        90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.CaughtWicketKeeper,
                        sortDirection = if (sortOrder == SortOrder.CaughtWicketKeeper) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }

                    ),
                    "caughtf" to ColumnMetaData(
                        "Caught F", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.CaughtFielder,
                        sortDirection = if (sortOrder == SortOrder.CaughtFielder) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "wicketkeeperdismissals" to ColumnMetaData(
                        "WK Dismissals", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.WicketKeepingDismissals,
                        sortDirection = if (sortOrder == SortOrder.WicketKeepingDismissals) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "caught" to ColumnMetaData(
                        "Caught", 90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Caught,
                        sortDirection = if (sortOrder == SortOrder.Caught) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "bestDismissals" to ColumnMetaData(
                        "Best Dismissals", 150.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.BestDismissals,
                        sortDirection = if (sortOrder == SortOrder.BestDismissals) {
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




