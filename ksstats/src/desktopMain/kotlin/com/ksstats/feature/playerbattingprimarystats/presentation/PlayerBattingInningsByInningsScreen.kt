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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.*
import com.ksstats.core.types.*
import com.ksstats.feature.playerbattingprimarystats.data.InningsByInningsBatting
import com.ksstats.feature.playerbattingprimarystats.domain.usecase.PlayerBattingPrimaryStatsUseCases
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import com.ksstats.shared.fromSeconds
import com.ksstats.shared.utils.buildDeailsScreenNavUrl
import com.ksstats.shared.utils.buildRecordsScreenNavArguments
import com.ksstats.shared.utils.buildSummaryScreenRoute
import kotlinx.datetime.format
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.playerBattingInningsByInningsScreen(
    navigate: (String) -> Unit,
    screen: StatsAppScreens,
    title: StringResource,
) {

    composable(
        route = buildSummaryScreenRoute(screen),
        arguments = buildRecordsScreenNavArguments(100)
    ) { navBackStackEntry ->

        var pagingParameters by remember {
            mutableStateOf(PagingParameters(startRow = 0, pageSize = 50, limit = 0))
        }
        var searchParameters by remember {
            mutableStateOf(
                SearchParameters(
                    sortOrder = SortOrder.Score,
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


        val battingUseCases: PlayerBattingPrimaryStatsUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: PlayerBattingInningsByInningsScreenViewModel = viewModel {
            PlayerBattingInningsByInningsScreenViewModel(battingUseCases, summaryUseCases, screen)
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

        val searchResults = viewModel.inningsByInnings.collectAsState()
        val searching = viewModel.searching.collectAsState()

        val count = searchResults.value.count ?: 0
        val displayRecords: List<List<String>> = getDisplayRecords(searchResults.value.data, pagingParameters.startRow)

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(searchParameters.startDate, searchParameters.endDate)

        val isInningsByInnings = screen == StatsAppScreens.BattingInningsByInnings

        PlayerBattingInningsByInningsScreen(
            displayRecords = displayRecords,
            isInningsByInnings = isInningsByInnings,
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

                val navUrl = buildDeailsScreenNavUrl(screen.name, searchParameters)
                navigate(navUrl)
            },
            onSort = { order ->

                // reverse the sort direction if the order is the same otherwise keep it the same
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

                val navUrl = buildDeailsScreenNavUrl(screen.name, searchParameters)
                navigate(navUrl)

            }
        )
    }
}


private fun getDisplayRecords(searchResults: List<InningsByInningsBatting>, startRow: Int): List<List<String>> {
    return searchResults.mapIndexed { index, searchResult ->

        val format = createMatchDateEnglishDateFormat()
        val date = searchResult.matchDate.fromSeconds().format(format)
        listOf(
            (index + startRow + 1).toString(),
            searchResult.name,
            searchResult.team,
            getScore(searchResult.score, searchResult.notOut),
            searchResult.minutes.toString(),
            searchResult.balls.toString(),
            searchResult.fours.toString(),
            searchResult.sixes.toString(),
            searchResult.strikeRate.round(2),
            searchResult.innings.toString(),
            searchResult.opponents,
            searchResult.ground,
            date,
        )
    }
}

fun getScore(score: Int, notOut: Int): String =
    if (notOut == 1)
        "${score}*"
    else
        score.toString()


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerBattingInningsByInningsScreen(
    displayRecords: List<List<String>>,
    searching: State<Boolean>,
    isInningsByInnings: Boolean,
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
                        "Team", 200.dp, sortOrder = SortOrder.Teams,
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
                        "Score",
                        90.dp,
                        sortOrder = SortOrder.Score,
                        align = TextAlign.End,
                        sortDirection = if (sortOrder == SortOrder.Score) {
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
                        "Mins",
                        90.dp,
                        align = TextAlign.End,
                        sortOrder = SortOrder.Minutes,
                        sortDirection = if (sortOrder == SortOrder.Minutes) {
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
                        90.dp,
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
                    ColumnMetaData(
                        "4s",
                        50.dp,
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
                    ColumnMetaData(
                        "6s",
                        50.dp,
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
                    ColumnMetaData(
                        "SR",
                        90.dp,
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
                    ColumnMetaData(
                        "Innings",
                        40.dp,
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
                        },
                        visible = isInningsByInnings
                    ),
                    ColumnMetaData(
                        "Opponent",
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
                    ColumnMetaData(
                        "Ground",
                        250.dp,
                        sortOrder = SortOrder.Ground,
                        sortDirection = if (sortOrder == SortOrder.Ground) {
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
                        "Match Date",
                        150.dp, sortOrder =
                        SortOrder.MatchStartDateAsOffset,
                        sortDirection = if (sortOrder == SortOrder.MatchStartDateAsOffset) {
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




