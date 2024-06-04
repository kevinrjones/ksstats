package com.ksstats.feature.battingrecordsdisplay.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.core.presentation.components.*
import com.ksstats.feature.battingrecordsdisplay.data.BattingSearchResults
import com.ksstats.feature.battingrecordsdisplay.domain.usecase.BattingDetailsUseCases
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import org.koin.compose.koinInject

fun NavGraphBuilder.battingDetailsScreen(navigate: (String) -> Unit) {
    composable(
        route = StatsAppScreen.BattingDetails.name +
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
                    "",
                    "",
                    0,
                    0,
                    0,
                    0
                )
            )
        }


        val battingUseCases: BattingDetailsUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: BattingDetailsScreenViewModel = viewModel {
            BattingDetailsScreenViewModel(battingUseCases, summaryUseCases)
        }
        val summary = viewModel.summary.collectAsState()

        LaunchedEffect(Unit) {
            val matchType = navBackStackEntry.arguments?.getString("matchType") ?: "t"
            val matchSubType = navBackStackEntry.arguments?.getString("matchSubType") ?: "t"
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

        val searchResults = viewModel.battingSearchResults.collectAsState()
        val searching = viewModel.searching.collectAsState()

        val count = searchResults.value.firstOrNull()?.count ?: 0
        val startRow = (navBackStackEntry.arguments?.getInt("startRow") ?: 0)
        val displayRecords: List<List<String>> = getDisplayRecords(searchResults.value, startRow)

        val startDate = navBackStackEntry.arguments?.getLong("startDate") ?: -99999999999L
        val endDate = navBackStackEntry.arguments?.getLong("endDate") ?: 999999999999L

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(startDate, endDate)

        BattingDetailsScreen(
            displayRecords = displayRecords,
            searching = searching,
            title = "Player Batting Summary",
            summary = summaryString,
            pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: 50,
            startRow = navBackStackEntry.arguments?.getInt("startRow") ?: 0,
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

                val navUrl = buildNavUrl(StatsAppScreen.BattingDetails.name, searchParameters)
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

                val navUrl = buildNavUrl(StatsAppScreen.BattingDetails.name, searchParameters)
                navigate(navUrl)

            }
        )
    }
}


fun getDisplayRecords(searchResults: List<BattingSearchResults>, startRow: Int): List<List<String>> {
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

fun getHighestScoreString(highestScore: Double): String {
    val hsWhole = highestScore.toInt()
    val hsPart = Math.ceil(highestScore).toInt()

    if (hsWhole == hsPart) {
        return hsWhole.toString()
    } else {
        return "${hsWhole}*"
    }
}

fun buildNavUrl(baseUrl: String, searchParameters: SearchParameters): String {

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

fun calculatePagingParameters(
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

@Composable
fun BattingDetailsScreen(
    displayRecords: List<List<String>>,
    searching: State<Boolean>,
    summary: String,
    title: String,
    pageSize: Int,
    startRow: Int,
    limit: Int,
    rowCount: Int,
    sortOrder: SortOrder,
    sortDirection: SortDirection,
    onPageChanged: (PageChangedNavigation) -> Unit,
    onSort: (SortOrder) -> Unit,
) {

    // initial height needed as the grid is empty, setting it to 0 is fine (it appears!)
    Box(
        modifier = Modifier
            .height(0.dp)
            .background(Color.Transparent)
    ) {
        if (searching.value) {

            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { }
                .background(Color.Gray)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            TitleRow(title = title)
            SummaryRow(summary = summary)
            SearchLimitRow(searchLimit = limit)
            NavigationRow(
                pageNumber = 1,
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
    }
}




