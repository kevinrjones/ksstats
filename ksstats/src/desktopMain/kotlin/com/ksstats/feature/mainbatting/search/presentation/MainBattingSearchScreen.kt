package com.ksstats.feature.mainbatting.search.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.core.presentation.components.*
import com.ksstats.feature.mainbatting.presentation.BattingRecordsViewModel
import com.ksstats.feature.mainbatting.presentation.BattingSearchEvent
import com.ksstats.feature.mainbatting.presentation.SearchViewFormat
import com.ksstats.feature.mainbatting.search.presentation.components.*
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.competitionLabel
import com.ksstats.ksstats.generated.resources.matchTypeLabel
import com.ksstats.ksstats.generated.resources.teamsLabel
import com.ksstats.shared.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun MainBattingSearchScreen(matchType: String?, navigate: (String) -> Unit) {
    val viewModel: BattingRecordsViewModel = koinInject()

    val matchTypes = viewModel.matchTypes.collectAsState()
    val selectedMatchType = viewModel.selectedMatchType.collectAsState()
    val competitions = viewModel.competitions.collectAsState()
    val selectedCompetition = viewModel.selectedCompetition.collectAsState()
    val teams = viewModel.teams.collectAsState()
    val selectedTeam = viewModel.selectedTeam.collectAsState()
    val selectedOpposition = viewModel.selectedOpposition.collectAsState()
    val grounds = viewModel.grounds.collectAsState()
    val selectedGround = viewModel.selectedGround.collectAsState()
    val countries = viewModel.countries.collectAsState()
    val selectedCountry = viewModel.selectedCountry.collectAsState()
    val seriesDates = viewModel.seriesDates.collectAsState()
    val selectedSeriesDate = viewModel.selectedSeriesDate.collectAsState()
    val pageSizes = viewModel.pageSizes.collectAsState()
    val selectedPageSize = viewModel.selectedPageSize.collectAsState()
    val minimumRuns = viewModel.minimumRuns.collectAsState()
    val startDate = viewModel.startDate.collectAsState()
    val endDate = viewModel.endDate.collectAsState()
    val searchViewFormat = viewModel.searchViewFormat.collectAsState()

    val dateTimeFormat = LocalDate.Format {
        byUnicodePattern("dd/MM/yyyy")
    }

    viewModel.initializeFromRoute(matchType)

    MainBattingSearchScreenDisplay(
        matchTypeParams = AppDropDownParams(
            matchTypes.value.map { DropDownMenuState(it.type, it.description) },
            DropDownMenuState(selectedMatchType.value.type, selectedMatchType.value.description),
            stringResource(Res.string.matchTypeLabel)
        ),
        competitionParams = AppDropDownParams(
            competitions.value.map { DropDownMenuState(it.subType, it.competition) },
            DropDownMenuState(selectedCompetition.value.type, selectedCompetition.value.competition),
            stringResource(Res.string.competitionLabel)
        ),
        teamParams = AppDropDownParams(
            teams.value.map { DropDownMenuState(it.id.toString(), it.name) },
            DropDownMenuState(selectedTeam.value.id.toString(), selectedTeam.value.name),
            stringResource(Res.string.teamsLabel)
        ),

        oppositionParams = AppDropDownParams(
            teams.value.map { DropDownMenuState(it.id.toString(), it.name) },
            DropDownMenuState(selectedOpposition.value.id.toString(), selectedOpposition.value.name),
            stringResource(Res.string.opponentsLabel)
        ),
        groundsParams = AppDropDownParams(
            grounds.value.map { DropDownMenuState(it.id.toString(), it.name) },
            DropDownMenuState(selectedGround.value.id.toString(), selectedGround.value.name),
            stringResource(Res.string.groundsLabel)
        ),
        countriesParams = AppDropDownParams(
            countries.value.map { DropDownMenuState(it.id.toString(), it.name) },
            DropDownMenuState(selectedCountry.value.id.toString(), selectedCountry.value.name),
            stringResource(Res.string.countriesLabel)
        ),
        seriesDatesParams = AppDropDownParams(
            seriesDates.value.map { DropDownMenuState(it, it) },
            DropDownMenuState(selectedSeriesDate.value, selectedSeriesDate.value),
            stringResource(Res.string.seriesDatesLabel)
        ),
        pageSizesParams = AppDropDownParams(
            pageSizes.value.map { DropDownMenuState(it.toString(), it.toString()) },
            DropDownMenuState(selectedPageSize.value.toString(), selectedPageSize.value.toString()),
            stringResource(Res.string.pageSizeLabel)
        ),
        minimumRuns = minimumRuns.value,
        minimumRunsLabel = stringResource(Res.string.minimumRunsLabel),
        startDateLabel = startDate.value.format(dateTimeFormat), // displayLabel = ldt?.format(dateTimeFormat)!!
        startDate = startDate.value,
        endDateLabel = endDate.value.format(dateTimeFormat),
        endDate = endDate.value,
        searchViewFormat = searchViewFormat.value,
        onBattingEvent = { evt: BattingSearchEvent ->
            viewModel.uiEvent(evt)
        },
        navigate = {
            val navUrl = buildNavUrl(it, viewModel)
            navigate(navUrl)
        }
    )
}

fun buildNavUrl(baseUrl: String, viewModel: BattingRecordsViewModel): String {

    viewModel.saveSearchParamters()

    return "${baseUrl}?" +
            "matchType=${viewModel.selectedMatchType.value.type}" +
            "&matchSubType=${viewModel.selectedCompetition.value.type}" +
            "&teamId=${viewModel.selectedTeam.value.id}" +
            "&opponentsId=${viewModel.selectedOpposition.value.id}" +
            "&groundId=${viewModel.selectedGround.value.id}" +
            "&hostCountryId=${viewModel.selectedCountry.value.id}" +
            "&venue=${viewModel.venue.value}" +
            "&sortOrder=${viewModel.defaultSortOrder}" +
            "&sortDirection=${viewModel.defaultSortDirection}" +
            "&startDate=${viewModel.startDate.value}" +
            "&endDate=${viewModel.endDate.value}" +
            "&result=${viewModel.matchResult}" +
            "&limit=${viewModel.minimumRuns.value}" +
            "&startRow=0" +
            "&pageSize=${viewModel.selectedPageSize.value}"
}

@Preview
@Composable
fun MainBattingSearchScreenDisplayPreview() {
    MainBattingSearchScreenDisplay(
        matchTypeParams = AppDropDownParams(),
        competitionParams = AppDropDownParams(),
        teamParams = AppDropDownParams(),
        oppositionParams = AppDropDownParams(),
        groundsParams = AppDropDownParams(),
        countriesParams = AppDropDownParams(),
        seriesDatesParams = AppDropDownParams(),
        pageSizesParams = AppDropDownParams(),
        minimumRuns = 100,
        minimumRunsLabel = stringResource(Res.string.minimumRunsLabel),
        startDateLabel = "Start Date",
        startDate = LocalDate.now(),
        endDateLabel = "End Date",
        endDate = LocalDate.now(),
        searchViewFormat = SearchViewFormat.PlayerSummary,
        onBattingEvent = {},
        navigate = {}
    )
}


@Composable
fun MainBattingSearchScreenDisplay(
    matchTypeParams: AppDropDownParams,
    competitionParams: AppDropDownParams,
    teamParams: AppDropDownParams,
    oppositionParams: AppDropDownParams,
    groundsParams: AppDropDownParams,
    countriesParams: AppDropDownParams,
    seriesDatesParams: AppDropDownParams,
    pageSizesParams: AppDropDownParams,
    minimumRuns: Int,
    minimumRunsLabel: String,
    startDateLabel: String,
    startDate: LocalDate,
    endDateLabel: String,
    endDate: LocalDate,
    searchViewFormat: SearchViewFormat,
    onBattingEvent: (BattingSearchEvent) -> Unit = {},
    navigate: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = Color.Black)
    ) {

        MatchTypeRow(matchTypeParams, competitionParams, pageSizesParams, minimumRuns, minimumRunsLabel, onBattingEvent)
        TeamsRow(teamParams, oppositionParams, onBattingEvent)
        VenueRow(onBattingEvent)
        CountriesGroundsRow(groundsParams, countriesParams, onBattingEvent)
        DateRangeRow(seriesDatesParams, startDateLabel, startDate, endDateLabel, endDate, onBattingEvent)
        ResultRow(onBattingEvent)
        ViewFormatRow(searchViewFormat, onBattingEvent)
        ButtonRow(onBattingEvent = {
            when (it) {
                is BattingSearchEvent.SearchBatting -> navigate(StatsAppScreen.BattingDetails.name)
                else -> {
                    onBattingEvent(it)
                }
            }
        })
    }
}