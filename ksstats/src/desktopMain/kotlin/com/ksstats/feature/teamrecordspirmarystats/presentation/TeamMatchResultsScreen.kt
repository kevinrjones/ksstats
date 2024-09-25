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
import com.ksstats.feature.teamrecordspirmarystats.data.source.MatchResults
import com.ksstats.feature.teamrecordspirmarystats.domain.usecase.TeamPrimaryStatsUseCases
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.team
import com.ksstats.shared.utils.buildDetailsScreenNavUrl
import com.ksstats.shared.utils.buildRecordsScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.teamMatchResultsScreen(
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
        val viewModel: TeamMatchResultsScreenViewModel = viewModel {
            TeamMatchResultsScreenViewModel(teamUseCases, summaryUseCases, screen)
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

        val count = searchResults.value.count
        val displayRecords: List<Map<String, String>> = getDisplayRecords(searchResults.value.data)

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)


        TeamMatchResultsScreen(
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
            onPageChanged = {
                pagingParameters = pagingParameters.calculateNewPagingParameters(it, count)
                searchParameters = searchParameters.copy(
                    pagingParameters = pagingParameters
                )

                val navUrl = buildDetailsScreenNavUrl(screen.name, searchParameters)
                navigate(navUrl)
            },
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

fun getDisplayRecords(searchResults: List<MatchResults>): List<Map<String, String>> {
    return searchResults.mapIndexed { index, searchResult ->


        mapOf(
            "team" to searchResult.team,
            "result" to getWonText(searchResult.victoryType, searchResult.teamId, searchResult.whoWonId),
            "margin" to getMargin(searchResult.victoryType, searchResult.howMuch),
            "toss" to getToss(searchResult.teamId, searchResult.tossTeamId),
            "opponents" to searchResult.opponents,
            "ground" to searchResult.knownAs,
            "startDate" to searchResult.matchStartDate,
        )
    }

}

fun getToss(teamId: Int, tossTeamId: Int): String {
    if(teamId == tossTeamId) return "won"
    return "lost"
}

fun getMargin(victoryType: Int, howMuch: Int): String {
    return when (victoryType) {
        2 -> "$howMuch runs"
        3 -> "$howMuch wickets"
        4 -> "Innings and $howMuch runs"
        else -> ""
    }

}

fun getWonText(victoryType: Int, teamId: Int, whoWonId: Int): String {
    if(victoryType == 5) return "tied"
    if(victoryType == 1) return "drawn"
    if(teamId == whoWonId) return "won"
    return "lost"

}

@Composable
fun TeamMatchResultsScreen(
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
                    "result" to ColumnMetaData(
                        stringResource(Res.string.played),
                        200.dp,
                        sortOrder = SortOrder.Result,
                        sortDirection = if (sortOrder == SortOrder.Result) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "margin" to ColumnMetaData(
                        stringResource(Res.string.margin),
                        200.dp,
                        sortOrder = SortOrder.HowMuch,
                        sortDirection = if (sortOrder == SortOrder.HowMuch) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "toss" to ColumnMetaData(
                        stringResource(Res.string.toss),
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
                    "opponents" to ColumnMetaData(
                        stringResource(Res.string.opponent),
                        200.dp,
                        sortOrder = SortOrder.Opponents,
                        sortDirection = if (sortOrder == SortOrder.Opponents) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "ground" to ColumnMetaData(
                        stringResource(Res.string.groundsLabel),
                        300.dp,
                        sortOrder = SortOrder.Location,
                        sortDirection = if (sortOrder == SortOrder.Location) {
                            if (sortDirection == SortDirection.Ascending) {
                                DisplaySortDirection.Ascending
                            } else {
                                DisplaySortDirection.Descending
                            }
                        } else {
                            DisplaySortDirection.None
                        }
                    ),
                    "startDate" to ColumnMetaData(
                        stringResource(Res.string.startDateLabel),
                        300.dp,
                        sortOrder = SortOrder.MatchStartDateAsOffset,
                        sortDirection = if (sortOrder == SortOrder.MatchStartDateAsOffset) {
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