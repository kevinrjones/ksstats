package com.ksstats.feature.teamrecordspirmarystats.presentation

import androidx.compose.foundation.background
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
import androidx.navigation.compose.composable
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.*
import com.ksstats.core.types.*
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamSummary
import com.ksstats.feature.teamrecordspirmarystats.domain.usecase.TeamPrimaryStatsUseCases
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.innings
import com.ksstats.ksstats.generated.resources.team
import com.ksstats.shared.fromSeconds
import com.ksstats.shared.utils.buildDetailsScreenNavUrl
import com.ksstats.shared.utils.buildRecordsScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import kotlinx.datetime.format
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.teamSummaryScreen(
    navigate: (String) -> Unit,
    screen: StatsAppScreens,
    title: StringResource,
) {
    composable(
        route = buildSummaryScreenRoute(screen),
        arguments = buildRecordsScreenNavArguments(100)
    ) { navBackStackEntry ->

        var pagingParameters by remember {
            mutableStateOf(PagingParameters(startRow = 0, pageSize = 50, limit = 1))
        }
        var searchParameters by remember {
            mutableStateOf(
                SearchParameters(
                    sortOrder = SortOrder.Won,
                    sortDirection = SortDirection.Descending,
                    pagingParameters = pagingParameters
                )
            )
        }
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

        val teamUseCases: TeamPrimaryStatsUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: TeamSummaryScreenViewModel = viewModel {
            TeamSummaryScreenViewModel(teamUseCases, summaryUseCases, screen)
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

            val sortOrderNavArgument = navBackStackEntry.arguments?.getInt("sortOrder")

            val sortOrderIndex = if (sortOrderNavArgument == null || sortOrderNavArgument == -1)
                SortOrder.Score.ordinal
            else
                sortOrderNavArgument

            val sortOrder = SortOrder.entries[sortOrderIndex]

            searchParameters = SearchParameters(
                matchType = matchType,
                matchSubType = matchSubType,
                teamId = teamId,
                opponentsId = opponentsId,
                groundId = groundId,
                hostCountryId = hostCountryId,
                venue = navBackStackEntry.arguments?.getInt("venue") ?: 0,
                sortOrder = sortOrder,
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

        val searchResults = viewModel.teamSummary.collectAsState()

        val searching = viewModel.searching.collectAsState()

        val count = 0
        val displayRecords: List<Map<String, String>> = getDisplayRecords(searchResults.value.data)

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)


        TeamSummaryScreen(
            displayRecords = displayRecords,
            searching = searching,
            summary = summaryString,
            title = stringResource(title),
            pageNumber = pagingParameters.calculatePageNumber(),
            pageSize = pagingParameters.pageSize,
            startRow = navBackStackEntry.arguments?.getInt("startRow") ?: 0,
            limit = navBackStackEntry.arguments?.getInt("limit") ?: 1,
            rowCount = count,
            sortOrder = searchParameters.sortOrder,
            sortDirection = searchParameters.sortDirection,
            onPageChanged = {},
            onSort = { order ->
                val sortDirectionText = navBackStackEntry.arguments?.getString("sortDirection") ?: "DESC"
                val sortOrderFromNav = SortOrder.entries[navBackStackEntry.arguments?.getInt("sortOrder")
                    ?: SortOrder.SortNamePart.ordinal]

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

fun getDisplayRecords(searchResults: List<TeamSummary>): List<Map<String, String>> {
    return searchResults.mapIndexed { index, searchResult ->


        mapOf(
            "team" to searchResult.teamName,
            "matches" to searchResult.matches.toString(),
            "innings" to searchResult.innings.toString(),
            "won" to searchResult.won.toString(),
            "lost" to searchResult.lost.toString(),
            "tied" to searchResult.tied.toString(),
            "drawn" to searchResult.drawn.toString(),
            "runs" to searchResult.runs.toString(),
            "wickets" to searchResult.wickets.toString(),
            "average" to searchResult.average.truncate(2),
            "runsPerOver" to searchResult.runsPerOver.truncate(2),
            "strikeRate" to searchResult.strikeRate.truncate(2),
        )
    }

}

@Composable
fun TeamSummaryScreen(
    displayRecords: List<Map<String, String>>,
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
            SearchLimitRow(searchLimit = limit, postFix = "Dismissals")
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
                    "team" to ColumnMetaData(
                        stringResource(Res.string.team),
                        200.dp,
                        sortOrder = SortOrder.Team,
                        sortDirection = if (sortOrder == SortOrder.Team) {
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
                        stringResource(Res.string.played),
                        200.dp,
                        sortOrder = SortOrder.Played,
                        sortDirection = if (sortOrder == SortOrder.Played) {
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
                        stringResource(Res.string.innings),
                        200.dp,
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
                    "won" to ColumnMetaData(
                        stringResource(Res.string.wonLabel),
                        90.dp,
                        sortOrder = SortOrder.Won,
                        sortDirection = if (sortOrder == SortOrder.Won) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "lost" to ColumnMetaData(
                        stringResource(Res.string.lostLabel),
                        90.dp,
                        sortOrder = SortOrder.Lost,
                        sortDirection = if (sortOrder == SortOrder.Lost) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "tied" to ColumnMetaData(
                        stringResource(Res.string.tiedLabel),
                        90.dp,
                        sortOrder = SortOrder.Tied,
                        sortDirection = if (sortOrder == SortOrder.Tied) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "drawn" to ColumnMetaData(
                        stringResource(Res.string.drawnLabel),
                        90.dp,
                        sortOrder = SortOrder.Drawn,
                        sortDirection = if (sortOrder == SortOrder.Drawn) {
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
                        stringResource(Res.string.runs),
                        90.dp,
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
                    "wickets" to ColumnMetaData(
                        stringResource(Res.string.wicketsLabel),
                        90.dp,
                        sortOrder = SortOrder.Wickets,
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
                    "average" to ColumnMetaData(
                        stringResource(Res.string.averageLabel),
                        90.dp,
                        sortOrder = SortOrder.Average,
                        sortDirection = if (sortOrder == SortOrder.Average) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "runsPerOver" to ColumnMetaData(
                        stringResource(Res.string.runsPerOverLabel),
                        90.dp,
                        sortOrder = SortOrder.RunsPerOver,
                        sortDirection = if (sortOrder == SortOrder.RunsPerOver) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "strikeRate" to ColumnMetaData(
                        stringResource(Res.string.strikeRate),
                        90.dp,
                        sortOrder = SortOrder.strikeRate,
                        sortDirection = if (sortOrder == SortOrder.strikeRate) {
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

                Table(
                    rowCount = displayRecords.size,
                    cellContent = { key, row ->
                        if (displayRecords.isNotEmpty()) {
                            getContent(displayRecords, key, row, metaData)
                        } else
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
