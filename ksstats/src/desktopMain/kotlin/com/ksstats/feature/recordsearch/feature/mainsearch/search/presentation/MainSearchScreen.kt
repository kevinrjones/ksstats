package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ksstats.core.domain.util.Limits
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.core.domain.util.SortOrder
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.AppDropDownParams
import com.ksstats.core.presentation.components.DropDownMenuState
import com.ksstats.core.types.MatchType
import com.ksstats.feature.recordsearch.domain.presentation.components.*
import com.ksstats.feature.recordsearch.domain.usecase.RecordSearchUseCases
import com.ksstats.feature.recordsearch.feature.mainsearch.search.utils.MainSearchType
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.shared.now
import com.ksstats.shared.toSeconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(FormatStringsInDatetimeFormats::class)
fun NavGraphBuilder.mainSearchScreen(
    searchType: MainSearchType,
    limits: Limits,
    navigate: (String) -> Unit,
) {
    composable(
        route = searchType.screen.name
                + "?matchType={matchType}",
        arguments = listOf(
            navArgument(name = "matchType") {
                type = NavType.StringType
                nullable = true
            },
        )
    ) {

        val recordSearchUseCases: RecordSearchUseCases = koinInject()
        val viewModel: MainSearchViewModel = viewModel {
            MainSearchViewModel(recordSearchUseCases, limits.limitValues, searchType)
        }

        val dateTimeFormat = LocalDate.Format {
            byUnicodePattern("dd/MM/yyyy")
        }


        MainSearchScreen(
            matchTypeParams = AppDropDownParams(
                viewModel.state.value.matchTypes.map { DropDownMenuState(it.type, it.description) },
                DropDownMenuState(
                    viewModel.state.value.selectedMatchType.type,
                    viewModel.state.value.selectedMatchType.description
                ),
                stringResource(Res.string.matchTypeLabel)
            ),
            competitionParams = AppDropDownParams(
                viewModel.state.value.competitions.map { DropDownMenuState(it.subType, it.competition) },
                DropDownMenuState(
                    viewModel.state.value.selectedCompetition.type,
                    viewModel.state.value.selectedCompetition.competition
                ),
                stringResource(Res.string.competitionLabel)
            ),
            teamParams = AppDropDownParams(
                viewModel.state.value.teams.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedTeam.id.toString(),
                    viewModel.state.value.selectedTeam.name
                ),
                stringResource(Res.string.teamsLabel)
            ),

            oppositionParams = AppDropDownParams(
                viewModel.state.value.teams.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedOpposition.id.toString(),
                    viewModel.state.value.selectedOpposition.name
                ),
                stringResource(Res.string.opponentsLabel)
            ),
            groundsParams = AppDropDownParams(
                viewModel.state.value.grounds.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedGround.id.toString(),
                    viewModel.state.value.selectedGround.name
                ),
                stringResource(Res.string.groundsLabel)
            ),
            countriesParams = AppDropDownParams(
                viewModel.state.value.countries.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedCountry.id.toString(),
                    viewModel.state.value.selectedCountry.name
                ),
                stringResource(Res.string.countriesLabel)
            ),
            seriesDatesParams = AppDropDownParams(
                viewModel.state.value.seriesDates.map { DropDownMenuState(it, it) },
                DropDownMenuState(viewModel.state.value.selectedSeriesDate, viewModel.state.value.selectedSeriesDate),
                stringResource(Res.string.seriesDatesLabel)
            ),
            pageSizesParams = AppDropDownParams(
                viewModel.state.value.pageSizes.map { DropDownMenuState(it.toString(), it.toString()) },
                DropDownMenuState(
                    viewModel.state.value.selectedPageSize.toString(),
                    viewModel.state.value.selectedPageSize.toString()
                ),
                stringResource(Res.string.pageSizeLabel)
            ),
            limit = viewModel.state.value.minimumValue,
            limitLabel = stringResource(limits.limitLabel),
            startDateLabel = viewModel.state.value.startDate.format(dateTimeFormat), // displayLabel = ldt?.format(dateTimeFormat)!!
            startDate = viewModel.state.value.startDate,
            endDateLabel = viewModel.state.value.endDate.format(dateTimeFormat),
            endDate = viewModel.state.value.endDate,
            searchViewFormat = viewModel.state.value.searchViewFormat,
            isLoaded = viewModel.state.value.loaded,
            isBattingSearch = viewModel.state.value.isBattingTeamSearch,
            mainSearchType = searchType,
            onSearchEvent = { evt: MainSearchEvent ->
                viewModel.uiEvent(evt)
            },
            onTeamTypeChanged = {viewModel.onTeamTypeChanged()},
            navigate = { baseUrl, sortOrder, sortDirection ->
                val navUrl = buildNavUrl(baseUrl, viewModel, sortOrder, sortDirection)
                navigate(navUrl)
            }
        )
    }
}

fun buildNavUrl(baseUrl: String, viewModel: MainSearchViewModel, sortOrder: SortOrder, sortDirection: SortDirection): String {

    return "${baseUrl}?" +
            "matchType=${viewModel.state.value.selectedMatchType.type}" +
            "&matchSubType=${viewModel.state.value.selectedCompetition.subType}" +
            "&teamId=${viewModel.state.value.selectedTeam.id}" +
            "&opponentsId=${viewModel.state.value.selectedOpposition.id}" +
            "&groundId=${viewModel.state.value.selectedGround.id}" +
            "&hostCountryId=${viewModel.state.value.selectedCountry.id}" +
            "&venue=${viewModel.state.value.venue}" +
            "&sortOrder=${sortOrder.ordinal}" +
            "&sortDirection=${sortDirection.name}" +
            "&startDate=${viewModel.state.value.startDate.toSeconds()}" +
            "&endDate=${viewModel.state.value.endDate.toSeconds()}" +
            "&season=${viewModel.state.value.selectedSeriesDate}" +
            "&result=${viewModel.state.value.matchResult}" +
            "&limit=${viewModel.state.value.minimumValue}" +
            "&startRow=0" +
            "&pageSize=${viewModel.state.value.selectedPageSize}"

}


@Composable
fun MainSearchScreen(
    matchTypeParams: AppDropDownParams,
    competitionParams: AppDropDownParams,
    teamParams: AppDropDownParams,
    oppositionParams: AppDropDownParams,
    groundsParams: AppDropDownParams,
    countriesParams: AppDropDownParams,
    seriesDatesParams: AppDropDownParams,
    pageSizesParams: AppDropDownParams,
    limit: Int,
    limitLabel: String,
    startDateLabel: String,
    startDate: LocalDate,
    endDateLabel: String,
    endDate: LocalDate,
    searchViewFormat: SearchViewFormat,
    isLoaded: Boolean,
    isBattingSearch: Boolean,
    mainSearchType: MainSearchType,
    onSearchEvent: (MainSearchEvent) -> Unit = {},
    onTeamTypeChanged: () -> Unit = {},
    navigate: (String, SortOrder, SortDirection) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        val selectedMatchType = MatchType(matchTypeParams.selectedOption.key)

        MatchTypeRow(matchTypeParams, competitionParams, pageSizesParams, limit, limitLabel, onSearchEvent)
        TeamsRow(teamParams, oppositionParams, onSearchEvent)
        VenueRow(onSearchEvent)
        CountriesGroundsRow(groundsParams, countriesParams, onSearchEvent)
        DateRangeRow(seriesDatesParams, startDateLabel, startDate, endDateLabel, endDate, onSearchEvent)
        ResultRow(onSearchEvent)
        if(mainSearchType == MainSearchType.Teams) {
            TeamsViewFormatRow(searchViewFormat, selectedMatchType, onSearchEvent)
        } else {
            ViewFormatRow(searchViewFormat, selectedMatchType, onSearchEvent)
        }
        if(mainSearchType == MainSearchType.Teams) {
            Spacer(Modifier.height(16.dp))
            BattingOrBowlingTeamRow(isBattingSearch, onTeamTypeChanged = onTeamTypeChanged)
            Spacer(Modifier.height(16.dp))
        }
        ButtonRow(isLoaded, onBattingEvent = { mainSearchEvent ->
            when (mainSearchEvent) {
                is MainSearchEvent.SearchMain -> {
                    val navigateTo = getNavigateToScreenName(mainSearchType, searchViewFormat)
                    val (sortOrder, sortDirection) = getDefaultSortParameters(mainSearchType, searchViewFormat)
                    navigate(navigateTo, sortOrder, sortDirection)
                }

                else -> {
                    onSearchEvent(mainSearchEvent)
                }
            }
        })
    }
}

fun getNavigateToScreenName(mainSearchType: MainSearchType, searchViewFormat: SearchViewFormat): String {
    return when (mainSearchType) {

        MainSearchType.Batting -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> StatsAppScreens.BattingPlayerSummary.name
            SearchViewFormat.InningsByInnings -> StatsAppScreens.BattingInningsByInnings.name
            SearchViewFormat.MatchTotals -> StatsAppScreens.BattingMatchTotals.name
            SearchViewFormat.SeriesAverages -> StatsAppScreens.BattingSeriesAverages.name
            SearchViewFormat.GroundAverages -> StatsAppScreens.BattingGroundAverages.name
            SearchViewFormat.ByHostCountry -> StatsAppScreens.BattingByHostCountry.name
            SearchViewFormat.ByOppositionTeam -> StatsAppScreens.BattingByOppositionTeam.name
            SearchViewFormat.ByYearOfMatchStart -> StatsAppScreens.BattingByYear.name
            SearchViewFormat.BySeason -> StatsAppScreens.BattingBySeason.name
            SearchViewFormat.TeamSummary -> throw Exception("Invalid format for this search")
            SearchViewFormat.MatchResults -> throw Exception("Invalid format for this search")
            SearchViewFormat.OverallExtras -> throw Exception("Invalid format for this search")
            SearchViewFormat.ExtrasByInnings -> throw Exception("Invalid format for this search")
            SearchViewFormat.HighestTargetChased -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> throw Exception("Invalid format for this search")
        }

        MainSearchType.Bowling -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> StatsAppScreens.BowlingPlayerSummary.name
            SearchViewFormat.InningsByInnings -> StatsAppScreens.BowlingInningsByInnings.name
            SearchViewFormat.MatchTotals -> StatsAppScreens.BowlingMatchTotals.name
            SearchViewFormat.SeriesAverages -> StatsAppScreens.BowlingSeriesAverages.name
            SearchViewFormat.GroundAverages -> StatsAppScreens.BowlingGroundAverages.name
            SearchViewFormat.ByHostCountry -> StatsAppScreens.BowlingByHostCountry.name
            SearchViewFormat.ByOppositionTeam -> StatsAppScreens.BowlingByOppositionTeam.name
            SearchViewFormat.ByYearOfMatchStart -> StatsAppScreens.BowlingByYear.name
            SearchViewFormat.BySeason -> StatsAppScreens.BowlingBySeason.name
            SearchViewFormat.TeamSummary -> throw Exception("Invalid format for this search")
            SearchViewFormat.MatchResults -> throw Exception("Invalid format for this search")
            SearchViewFormat.OverallExtras -> throw Exception("Invalid format for this search")
            SearchViewFormat.ExtrasByInnings -> throw Exception("Invalid format for this search")
            SearchViewFormat.HighestTargetChased -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> throw Exception("Invalid format for this search")
        }

        MainSearchType.Fielding -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> StatsAppScreens.FieldingPlayerSummary.name
            SearchViewFormat.InningsByInnings -> StatsAppScreens.FieldingInningsByInnings.name
            SearchViewFormat.MatchTotals -> StatsAppScreens.FieldingMatchTotals.name
            SearchViewFormat.SeriesAverages -> StatsAppScreens.FieldingSeriesAverages.name
            SearchViewFormat.GroundAverages -> StatsAppScreens.FieldingGroundAverages.name
            SearchViewFormat.ByHostCountry -> StatsAppScreens.FieldingByHostCountry.name
            SearchViewFormat.ByOppositionTeam -> StatsAppScreens.FieldingByOppositionTeam.name
            SearchViewFormat.ByYearOfMatchStart -> StatsAppScreens.FieldingByYear.name
            SearchViewFormat.BySeason -> StatsAppScreens.FieldingBySeason.name
            SearchViewFormat.TeamSummary -> throw Exception("Invalid format for this search")
            SearchViewFormat.MatchResults -> throw Exception("Invalid format for this search")
            SearchViewFormat.OverallExtras -> throw Exception("Invalid format for this search")
            SearchViewFormat.ExtrasByInnings -> throw Exception("Invalid format for this search")
            SearchViewFormat.HighestTargetChased -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> throw Exception("Invalid format for this search")
        }

        MainSearchType.Teams -> when (searchViewFormat) {
            SearchViewFormat.TeamSummary -> StatsAppScreens.TeamSummary.name
            SearchViewFormat.InningsByInnings -> StatsAppScreens.TeamInningsByInnings.name
            SearchViewFormat.MatchTotals -> StatsAppScreens.TeamMatchTotalsScreen.name
            SearchViewFormat.SeriesAverages -> StatsAppScreens.TeamSeriesRecords.name
            SearchViewFormat.GroundAverages -> TODO()
            SearchViewFormat.ByHostCountry -> TODO()
            SearchViewFormat.ByOppositionTeam -> TODO()
            SearchViewFormat.ByYearOfMatchStart -> TODO()
            SearchViewFormat.BySeason -> TODO()
            SearchViewFormat.MatchResults -> StatsAppScreens.MatchResults.name
            SearchViewFormat.OverallExtras -> TODO()
            SearchViewFormat.ExtrasByInnings -> TODO()
            SearchViewFormat.HighestTargetChased -> TODO()
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> TODO()
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> TODO()
            SearchViewFormat.PlayerSummary -> throw Exception("Invalid format for this search")
        }
    }
}

fun getDefaultSortParameters(mainSearchType: MainSearchType, searchViewFormat: SearchViewFormat): Pair<SortOrder, SortDirection> {
    return when (mainSearchType) {

        MainSearchType.Batting -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.InningsByInnings -> Pair(SortOrder.Score, SortDirection.Descending)
            SearchViewFormat.MatchTotals -> Pair(SortOrder.Score, SortDirection.Descending)
            SearchViewFormat.SeriesAverages -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.GroundAverages -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.ByHostCountry -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.ByOppositionTeam -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.ByYearOfMatchStart -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.BySeason -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.TeamSummary -> throw Exception("Invalid format for this search")
            SearchViewFormat.MatchResults -> throw Exception("Invalid format for this search")
            SearchViewFormat.OverallExtras -> throw Exception("Invalid format for this search")
            SearchViewFormat.ExtrasByInnings -> throw Exception("Invalid format for this search")
            SearchViewFormat.HighestTargetChased -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> throw Exception("Invalid format for this search")
        }

        MainSearchType.Bowling -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.InningsByInnings -> Pair(SortOrder.MatchStartDateAsOffset, SortDirection.Ascending)
            SearchViewFormat.MatchTotals -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.SeriesAverages -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.GroundAverages -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.ByHostCountry -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.ByOppositionTeam -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.ByYearOfMatchStart -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.BySeason -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.TeamSummary -> throw Exception("Invalid format for this search")
            SearchViewFormat.MatchResults -> throw Exception("Invalid format for this search")
            SearchViewFormat.OverallExtras -> throw Exception("Invalid format for this search")
            SearchViewFormat.ExtrasByInnings -> throw Exception("Invalid format for this search")
            SearchViewFormat.HighestTargetChased -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> throw Exception("Invalid format for this search")
        }

        MainSearchType.Fielding -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.InningsByInnings -> Pair(SortOrder.MatchStartDateAsOffset, SortDirection.Ascending)
            SearchViewFormat.MatchTotals -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.SeriesAverages -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.GroundAverages -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.ByHostCountry -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.ByOppositionTeam -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.ByYearOfMatchStart -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.BySeason -> Pair(SortOrder.Dismissals, SortDirection.Descending)
            SearchViewFormat.TeamSummary -> throw Exception("Invalid format for this search")
            SearchViewFormat.MatchResults -> throw Exception("Invalid format for this search")
            SearchViewFormat.OverallExtras -> throw Exception("Invalid format for this search")
            SearchViewFormat.ExtrasByInnings -> throw Exception("Invalid format for this search")
            SearchViewFormat.HighestTargetChased -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> throw Exception("Invalid format for this search")
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> throw Exception("Invalid format for this search")
        }

        MainSearchType.Teams -> when(searchViewFormat) {
            SearchViewFormat.TeamSummary -> Pair(SortOrder.Won, SortDirection.Descending)
            SearchViewFormat.InningsByInnings -> Pair(SortOrder.MatchStartDateAsOffset, SortDirection.Ascending)
            SearchViewFormat.MatchTotals -> Pair(SortOrder.Total, SortDirection.Descending)
            SearchViewFormat.SeriesAverages -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.GroundAverages -> TODO()
            SearchViewFormat.ByHostCountry -> TODO()
            SearchViewFormat.ByOppositionTeam -> TODO()
            SearchViewFormat.ByYearOfMatchStart -> TODO()
            SearchViewFormat.BySeason -> TODO()
            SearchViewFormat.MatchResults -> Pair(SortOrder.MatchStartDateAsOffset, SortDirection.Ascending)
            SearchViewFormat.OverallExtras -> TODO()
            SearchViewFormat.ExtrasByInnings -> TODO()
            SearchViewFormat.HighestTargetChased -> TODO()
            SearchViewFormat.SuccessfullyDefendedLowestTarget -> TODO()
            SearchViewFormat.SuccessfullyDefendedInUnReducedMatch -> TODO()
            SearchViewFormat.PlayerSummary -> throw Exception("Invalid format for this search")
        }
    }
}

@Preview
@Composable
fun MainBattingSearchScreenPreview() {
    MainSearchScreen(
        matchTypeParams = AppDropDownParams(),
        competitionParams = AppDropDownParams(),
        teamParams = AppDropDownParams(),
        oppositionParams = AppDropDownParams(),
        groundsParams = AppDropDownParams(),
        countriesParams = AppDropDownParams(),
        seriesDatesParams = AppDropDownParams(),
        pageSizesParams = AppDropDownParams(),
        limit = 100,
        limitLabel = stringResource(Res.string.minimumRunsLabel),
        startDateLabel = "Start Date",
        startDate = LocalDate.now(),
        endDateLabel = "End Date",
        endDate = LocalDate.now(),
        searchViewFormat = SearchViewFormat.PlayerSummary,
        isLoaded = true,
        isBattingSearch = true,
        mainSearchType = MainSearchType.Batting,
        onSearchEvent = {},
        navigate = {url, sortOrder, sortDirection ->}
    )
}
