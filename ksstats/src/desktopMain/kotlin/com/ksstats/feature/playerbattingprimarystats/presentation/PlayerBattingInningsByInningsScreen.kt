package com.ksstats.feature.playerbattingprimarystats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
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
import com.ksstats.shared.utils.buildDeailsScreenNavUrl
import com.ksstats.shared.utils.buildSummaryScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import org.koin.compose.koinInject

fun NavGraphBuilder.playerBattingInningsByInningsScreen(navigate: (String) -> Unit) {
    composable(
        route = buildSummaryScreenRoute(StatsAppScreens.BattingInningsByInnings),
        arguments = buildSummaryScreenNavArguments(100)
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
        val viewModel: PlayerBattingInningsByInningsScreenViewModel = viewModel {
            PlayerBattingInningsByInningsScreenViewModel(battingUseCases, summaryUseCases)
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

        val count = searchResults.value.firstOrNull()?.count ?: 0
        val displayRecords: List<List<String>> = getDisplayRecords(searchResults.value, pagingParameters.startRow)

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)

        PlayerBattingInningsByInningsScreen(
            displayRecords = displayRecords,
            searching = searching,
            title = "Player Batting Summary",
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

                val navUrl = buildDeailsScreenNavUrl(StatsAppScreens.BattingPlayerSummary.name, searchParameters)
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

                val navUrl = buildDeailsScreenNavUrl(StatsAppScreens.BattingPlayerSummary.name, searchParameters)
                navigate(navUrl)

            }
        )
    }
}


private fun getDisplayRecords(searchResults: List<PrimaryBatting>, startRow: Int): List<List<String>> {
    return searchResults.mapIndexed { index, searchResult ->

        val highestScore = getHighestScoreString(searchResult.highestScore)

        listOf(
            (index + startRow + 1).toString(),
            searchResult.name,
            searchResult.team,
            searchResult.matches.toString(),
            searchResult.innings.toString(),
            searchResult.notOuts.toString(),
            searchResult.runs.toString(),
            highestScore,
            searchResult.hundreds.toString(),
            searchResult.fifties.toString(),
            searchResult.ducks.toString(),
            searchResult.fours.toString(),
            searchResult.sixes.toString(),
            searchResult.balls.toString(),
            searchResult.average.truncate(2),
            searchResult.strikeRate.round(2),
            searchResult.battingImpact.round(3),
        )
    }
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerBattingInningsByInningsScreen(
    displayRecords: List<List<String>>,
    searching: State<Boolean>,
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
                val metaData = listOf(
                    ColumnMetaData("", 60.dp),
                    ColumnMetaData(
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
                    ColumnMetaData(
                        "Teams", 200.dp, sortOrder = SortOrder.Teams,
                        sortDirection = if (sortOrder == SortOrder.Teams) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    ColumnMetaData(
                        "M",
                        70.dp,
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
                    ColumnMetaData(
                        "I",
                        70.dp,
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
                    ColumnMetaData(
                        "N/Os",
                        90.dp,
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
                    ColumnMetaData(
                        "Runs", 100.dp, sortOrder = SortOrder.Runs,
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
                    ColumnMetaData(
                        "HS",
                        90.dp,
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
                    ColumnMetaData(
                        "100s", 90.dp, sortOrder = SortOrder.Hundreds,
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
                    ColumnMetaData(
                        "50s", 90.dp, sortOrder = SortOrder.Fifties,
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
                    ColumnMetaData(
                        "0s", 90.dp, sortOrder = SortOrder.Ducks,
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
                    ColumnMetaData(
                        "4s", 90.dp, sortOrder = SortOrder.Fours,
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
                    ColumnMetaData(
                        "6s", 90.dp, sortOrder = SortOrder.Sixes,
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
                    ColumnMetaData(
                        "Balls", 90.dp, sortOrder = SortOrder.Balls,
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
                    ColumnMetaData(
                        "Avg", 90.dp, sortOrder = SortOrder.Avg,
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
                    ColumnMetaData(
                        "SR", 90.dp, sortOrder = SortOrder.SR,
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
                    ColumnMetaData(
                        "BI", 90.dp, sortOrder = SortOrder.BI,
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
                    columnCount = metaData.size,
                    rowCount = displayRecords.size,
                    cellContent = { column, row ->
                        if (displayRecords.isNotEmpty())
                            displayRecords[row][column]
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




