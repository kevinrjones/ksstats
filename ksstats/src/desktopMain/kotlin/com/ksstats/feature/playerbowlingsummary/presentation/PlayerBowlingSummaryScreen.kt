package com.ksstats.feature.playerbowlingsummary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.*
import com.ksstats.core.types.MatchType
import com.ksstats.core.types.toMatchType
import com.ksstats.feature.playerbowlingsummary.domain.usecase.PlayerBowlingSummaryUseCases
import com.ksstats.feature.playerbowlingsummary.data.BowlingSummary
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import org.koin.compose.koinInject

fun NavGraphBuilder.playerBowlingSummaryScreen(navigate: (String) -> Unit) {
    composable(
        route = StatsAppScreens.BowlingPlayerSummary.name +
                "?matchType={matchType}" +
                "&matchSubType={matchSubType}" +
                "&teamId={teamId}" +
                "&opponentsId={opponentsId}" +
                "&groundId={groundId}" +
                "&hostCountryId={hostCountryId}" +
                "&venue={venue}" +
                "&sortOrder={sortOrder}" +
                "&sortDirection={sortDirection}" +
                "&startDate={startDate}" +
                "&endDate={endDate}" +
                "&season={season}" +
                "&result={result}" +
                "&limit={limit}" +
                "&startRow={startRow}" +
                "&pageSize={pageSize}",
        arguments = listOf(
            navArgument(name = "matchType") {
                type = NavType.StringType
                defaultValue = "t"
            },
            navArgument(name = "matchSubType") {
                type = NavType.StringType
                defaultValue = "t"
            },
            navArgument(name = "teamId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "opponentsId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "groundId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "hostCountryId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "venue") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "sortOrder") {
                type = NavType.IntType
                defaultValue = 3
            },
            navArgument(name = "sortDirection") {
                type = NavType.StringType
                defaultValue = "DESC"
            },
            navArgument(name = "startDate") {
                type = NavType.LongType
                defaultValue = -8520336000 // 1700-1-1
            },
            navArgument(name = "endDate") {
                type = NavType.LongType
                defaultValue = 253402214400 // 9999-12-31
            },
            navArgument(name = "season") {
                type = NavType.StringType
                defaultValue = "All"
            },
            navArgument(name = "result") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "limit") {
                type = NavType.IntType
                defaultValue = 100
            },
            navArgument(name = "startRow") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "pageSize") {
                type = NavType.IntType
                defaultValue = 50
            },
        )
    ) { navBackStackEntry ->

        var pagingParameters by remember { mutableStateOf(PagingParameters(startRow = 0, pageSize = 50, limit = 100)) }
        var searchParameters by remember { mutableStateOf(SearchParameters()) }
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


        val bowlingUseCases: PlayerBowlingSummaryUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: PlayerBowlingSummaryScreenViewModel = viewModel {
            PlayerBowlingSummaryScreenViewModel(bowlingUseCases, summaryUseCases)
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
                startDate = startDate,
                endDate = endDate,
                result = navBackStackEntry.arguments?.getInt("result") ?: 0,
                season = navBackStackEntry.arguments?.getString("season") ?: "All",
                pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: pagingParameters.pageSize,
                startRow = navBackStackEntry.arguments?.getInt("startRow") ?: pagingParameters.startRow,
                limit = navBackStackEntry.arguments?.getInt("limit") ?: pagingParameters.limit,
                fivesLimit = viewModel.getFivesLimitForMatchType(matchType)
            )

            pagingParameters = pagingParameters.copy(
                startRow = navBackStackEntry.arguments?.getInt("startRow") ?: pagingParameters.startRow,
                pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: pagingParameters.pageSize,
                limit = navBackStackEntry.arguments?.getInt("limit") ?: pagingParameters.limit
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
        val matchType = (navBackStackEntry.arguments?.getString("matchType") ?: "t").toMatchType()
        val searchResults = viewModel.bowlingSummary.collectAsState()
        val searching = viewModel.searching.collectAsState()

        val count = searchResults.value.firstOrNull()?.count ?: 0
        val startRow = (navBackStackEntry.arguments?.getInt("startRow") ?: 0)
        val pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: 50

        val displayRecords: List<List<String>> = getDisplayRecords(searchResults.value, startRow, matchType = matchType)

        val startDate = navBackStackEntry.arguments?.getLong("startDate") ?: -99999999999L
        val endDate = navBackStackEntry.arguments?.getLong("endDate") ?: 999999999999L

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(startDate, endDate)


        PlayerBowlingSummaryScreen(
            displayRecords = displayRecords,
            searching = searching,
            matchType = matchType,
            title = "Player Bowling Summary",
            summary = summaryString,
            pageNumber = viewModel.calculatePageNumber(startRow, pageSize),
            pageSize = pageSize,
            startRow = startRow,
            limit = navBackStackEntry.arguments?.getInt("limit") ?: 100,
            rowCount = count,
            sortOrder = searchParameters.sortOrder,
            sortDirection = searchParameters.sortDirection,
            onPageChanged = {
                pagingParameters = calculatePagingParameters(it, pagingParameters, count)
                searchParameters = searchParameters.copy(
                    pageSize = pagingParameters.pageSize,
                    limit = pagingParameters.limit,
                    startRow = pagingParameters.startRow
                )

                val navUrl = buildNavUrl(StatsAppScreens.BowlingPlayerSummary.name, searchParameters)
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

                val navUrl = buildNavUrl(StatsAppScreens.BowlingPlayerSummary.name, searchParameters)
                navigate(navUrl)

            }
        )
    }
}


private fun getDisplayRecords(
    searchResults: List<BowlingSummary>,
    startRow: Int,
    matchType: MatchType,
): List<List<String>> {
    return searchResults.mapIndexed { index, searchResult ->

        var data = listOf(
            (index + startRow + 1).toString(),
            searchResult.name,
            searchResult.team,
            searchResult.matches.toString(),
            searchResult.innings.toString(),
            searchResult.balls.toString(),
            searchResult.maidens.toString(),
            searchResult.runs.toString(),
            searchResult.wickets.toString(),
            searchResult.average.truncate(2),
            getIndividualBb(searchResult.bestBowlingInnings),
        )
        if (matchType.isMultiInningsType()) {
            data = data + getIndividualBb(searchResult.bestBowlingMatch)
        }
        data = data + listOf(
            searchResult.economy.truncate(2),
            searchResult.strikeRate.truncate(2),
            searchResult.fivefor.toString()
        )
        if (matchType.isMultiInningsType()) {
            data = data + searchResult.tenfor.toString()
        }
        data = data + listOf(
            searchResult.fours.toString(),
            searchResult.sixes.toString(),
            searchResult.bowlingImpact.round(3),
        )
        data
    }
}


private fun getIndividualBb(syntheticBB: Double): String {
    val wickets = syntheticBB.toInt()
    val runs = Math.round(0.1 / (syntheticBB - wickets)).toInt()

    return "${wickets}-${runs}";
}

private fun buildNavUrl(baseUrl: String, searchParameters: SearchParameters): String {

    val sortOrder = searchParameters.sortOrder.ordinal

    return "${baseUrl}?" +
            "matchType=${searchParameters.matchType}" +
            "&matchSubType=${searchParameters.matchSubType}" +
            "&teamId=${searchParameters.teamId}" +
            "&opponentsId=${searchParameters.opponentsId}" +
            "&groundId=${searchParameters.groundId}" +
            "&hostCountryId=${searchParameters.hostCountryId}" +
            "&venue=${searchParameters.venue}" +
            "&sortOrder=${sortOrder}" +
            "&sortDirection=${searchParameters.sortDirection}" +
            "&startDate=${searchParameters.startDate}" +
            "&endDate=${searchParameters.endDate}" +
            "&season=${searchParameters.season}" +
            "&result=${searchParameters.result}" +
            "&limit=${searchParameters.limit}" +
            "&startRow=${searchParameters.startRow}" +
            "&pageSize=${searchParameters.pageSize}"

}

private fun calculatePagingParameters(
    pageChangedNavigation: PageChangedNavigation,
    pagingParameters: PagingParameters,
    rowCount: Int,
): PagingParameters {

    val previous = pagingParameters.startRow - pagingParameters.pageSize
    val next = pagingParameters.startRow + pagingParameters.pageSize
    val lastRow =
        (pagingParameters.pageSize.calculateMaxPages(rowCount) * pagingParameters.pageSize) - pagingParameters.pageSize

    val newPagingParameters = when (pageChangedNavigation) {
        PageChangedNavigation.First -> pagingParameters.copy(startRow = 0)
        PageChangedNavigation.Previous -> pagingParameters.copy(startRow = previous)
        PageChangedNavigation.Next -> pagingParameters.copy(startRow = next)
        PageChangedNavigation.Last -> pagingParameters.copy(startRow = lastRow)
        is PageChangedNavigation.PageSizeChange -> pagingParameters.copy(pageSize = pageChangedNavigation.pageSize)
    }
    return newPagingParameters
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerBowlingSummaryScreen(
    displayRecords: List<List<String>>,
    matchType: MatchType,
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

            val isMultiInnings = matchType.isMultiInningsType()
            val fiveWITitle = if (isMultiInnings) "5WI" else "4WI"

            TitleRow(title = title)
            SummaryRow(summary = summary)
            SearchLimitRow(searchLimit = limit, postFix = "Wickets")
            NavigationRow(
                pageNumber = pageNumber,
                pageSize = pageSize,
                maxPages = pageSize.calculateMaxPages(rowCount),
                firstRowNumber = startRow + 1,
                lastRowNumber = rowCount,
                onPageChanged = { onPageChanged(it) }
            )

            Row {
                var metaData = listOf(
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
                        "Balls",
                        70.dp,
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
                    ColumnMetaData(
                        "Maidens",
                        90.dp,
                        sortOrder = SortOrder.Maidens,
                        sortDirection = if (sortOrder == SortOrder.Maidens) {
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
                        "Wickets", 100.dp, sortOrder = SortOrder.Wickets,
                        sortDirection = if (sortOrder == SortOrder.Wickets) {
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
                        "BBI", 90.dp, sortOrder = SortOrder.BBI,
                        sortDirection = if (sortOrder == SortOrder.BBI) {
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
                if (isMultiInnings)
                    metaData = metaData +
                            ColumnMetaData(
                                "BBM", 90.dp, sortOrder = SortOrder.BBM,
                                sortDirection = if (sortOrder == SortOrder.BBM) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )
                metaData = metaData + listOf(
                    ColumnMetaData(
                        "Econ", 90.dp, sortOrder = SortOrder.Econ,
                        sortDirection = if (sortOrder == SortOrder.Econ) {
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
                        fiveWITitle, 90.dp, sortOrder = SortOrder.FiveFor,
                        sortDirection = if (sortOrder == SortOrder.FiveFor) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                )
                if (isMultiInnings)
                    metaData = metaData +
                            ColumnMetaData(
                                "10w/m", 90.dp, sortOrder = SortOrder.TenFor,
                                sortDirection = if (sortOrder == SortOrder.TenFor) {
                                    if (sortDirection == SortDirection.Ascending) {
                                        DisplaySortDirection.Ascending
                                    } else {
                                        DisplaySortDirection.Descending
                                    }
                                } else {
                                    DisplaySortDirection.None
                                }
                            )
                metaData = metaData + listOf(
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





