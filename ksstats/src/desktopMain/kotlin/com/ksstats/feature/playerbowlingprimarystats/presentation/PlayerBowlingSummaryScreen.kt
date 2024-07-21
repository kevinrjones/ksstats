package com.ksstats.feature.playerbowlingprimarystats.presentation

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
import androidx.navigation.compose.composable
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.*
import com.ksstats.core.types.*
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.PlayerBowlingSummaryUseCases
import com.ksstats.feature.playerbowlingprimarystats.data.BowlingSummary
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import com.ksstats.shared.utils.buildDeailsScreenNavUrl
import com.ksstats.shared.utils.buildRecordsScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.playerBowlingSummaryScreen(
    navigate: (String) -> Unit,
    screen: StatsAppScreens,
    title: StringResource,
) {
    composable(
        route = buildSummaryScreenRoute(screen),
        arguments = buildRecordsScreenNavArguments(10)
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
                result = navBackStackEntry.arguments?.getInt("result") ?: 0,
                season = navBackStackEntry.arguments?.getString("season") ?: "All",
                pagingParameters = pagingParameters,
                fivesLimit = viewModel.getFivesLimitForMatchType(matchType)
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

        val searchResults = viewModel.bowlingSummary.collectAsState()
        val searching = viewModel.searching.collectAsState()

        val count = searchResults.value.count

        val displayRecords: List<Map<String, String>> = getDisplayRecords(
            searchResults.value.data,
            pagingParameters.startRow,
            matchType = searchParameters.matchType
        )

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)


        PlayerBowlingSummaryScreen(
            displayRecords = displayRecords,
            searching = searching,
            matchType = searchParameters.matchType,
            title = stringResource(title),
            summary = summaryString,
            pageNumber = pagingParameters.calculatePageNumber(),
            pageSize = pagingParameters.pageSize,
            startRow = pagingParameters.startRow,
            limit = navBackStackEntry.arguments?.getInt("limit") ?: 100,
            rowCount = count,
            sortOrder = searchParameters.sortOrder,
            sortDirection = searchParameters.sortDirection,
            onPageChanged = {
                pagingParameters = pagingParameters.calculateNewPagingParameters(it, count)
                searchParameters = searchParameters.copy(
                    pagingParameters = pagingParameters
                )

                val navUrl = buildDeailsScreenNavUrl(screen.name, searchParameters)
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

                val navUrl = buildDeailsScreenNavUrl(screen.name, searchParameters)
                navigate(navUrl)

            }
        )
    }
}


private fun getDisplayRecords(
    searchResults: List<BowlingSummary>,
    startRow: Int,
    matchType: MatchType,
): List<Map<String, String>> {
    return searchResults.mapIndexed { index, searchResult ->

        val data = mutableMapOf(
            "index" to (index + startRow + 1).toString(),
            "name" to searchResult.name,
            "team" to searchResult.team,
            "matches" to searchResult.matches.toString(),
            "innings" to searchResult.innings.toString(),
            "balls" to searchResult.balls.toString(),
            "maidens" to searchResult.maidens.toString(),
            "runs" to searchResult.runs.toString(),
            "wickets" to searchResult.wickets.toString(),
            "avg" to searchResult.average.truncate(2),
            "bbi" to getIndividualBb(searchResult.bestBowlingInnings),
            "econ" to searchResult.economy.truncate(2),
            "sr" to searchResult.strikeRate.truncate(2),
            "fivefor" to searchResult.fivefor.toString(),
            "fours" to searchResult.fours.toString(),
            "sixes" to searchResult.sixes.toString(),
            "bi" to searchResult.bowlingImpact.round(3),
        )
        if (matchType.isMultiInningsType()) {
            data["bbm"] = getIndividualBb(searchResult.bestBowlingMatch)
        }
        if (matchType.isMultiInningsType()) {
            data["tenfor"] = searchResult.tenfor.toString()
        }
        data
    }
}


private fun getIndividualBb(syntheticBB: Double): String {
    val wickets = syntheticBB.toInt()
    val runs = Math.round(0.1 / (syntheticBB - wickets)).toInt()

    return "${wickets}-${runs}";
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerBowlingSummaryScreen(
    displayRecords: List<Map<String, String>>,
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
                var metaData = mapOf(
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
                    "team" to ColumnMetaData(
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
                    "matches" to ColumnMetaData(
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
                    "innings" to ColumnMetaData(
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
                    "balls" to ColumnMetaData(
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
                    "maidens" to ColumnMetaData(
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
                    "runs" to ColumnMetaData(
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
                    "wickets" to ColumnMetaData(
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
                    "avg" to ColumnMetaData(
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
                    "bbi" to ColumnMetaData(
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
                    metaData = metaData + mapOf(
                        "bbm" to ColumnMetaData(
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
                    )
                metaData = metaData + mapOf(
                    "econ" to ColumnMetaData(
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
                    "sr" to ColumnMetaData(
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
                    "fivefor" to ColumnMetaData(
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
                    metaData = metaData + mapOf(
                        "tenfor" to ColumnMetaData(
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
                    )
                metaData = metaData + mapOf(
                    "fours" to ColumnMetaData(
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
                    "sixes" to ColumnMetaData(
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
                    "bi" to ColumnMetaData(
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
                    rowCount = displayRecords.size,
                    cellContent = { key, row ->
                        if (displayRecords.isNotEmpty())
                            displayRecords[row][key]
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





