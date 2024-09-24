package com.ksstats.feature.teamrecordspirmarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.math.BigDecimal
import java.time.LocalDate

object JooqMatchResultsRecords {
    fun createScoresCte(searchParameters: SearchParameters): SelectConditionStep<Record13<Any, Any, Int?, Int?, String?, Long?, String?, String?, String?, Any, Any, Int?, Int?>> {

        val teamIdCondition = if (searchParameters.teamId != 0) {
            and(EXTRAMATCHDETAILS.TEAMID.eq(searchParameters.teamId))
        } else null
        val opponentsIdCondition = if (searchParameters.opponentsId != 0) {
            and(EXTRAMATCHDETAILS.OPPONENTSID.eq(searchParameters.opponentsId))
        } else null
        val groundCondition = if (searchParameters.groundId != 0) {
            and(MATCHES.LOCATIONID.eq(searchParameters.groundId))
        } else null
        val homeCountryIdCondition = if (searchParameters.hostCountryId != 0) {
            and(MATCHES.HOMECOUNTRYID.eq(searchParameters.hostCountryId))
        } else null
        val dateOrSeasonCondition = if (searchParameters.season != "All") {
            and(MATCHES.SERIESDATE.eq(searchParameters.season))
        } else {
            and(MATCHES.MATCHSTARTDATEASOFFSET.ge(searchParameters.startDate.value))
                .and(MATCHES.MATCHSTARTDATEASOFFSET.le(searchParameters.endDate.value))
        }

        val matchResultCondition = if (searchParameters.result != 0) {
            and(EXTRAMATCHDETAILS.RESULT.bitAnd(searchParameters.result).notEqual(0))
        } else null
        val homeOrAwayCondition = if (searchParameters.venue != 0) {
            and(EXTRAMATCHDETAILS.HOMEAWAY.bitAnd(searchParameters.venue).notEqual(0))
        } else null

        val cte = select(
            field("team.name").`as`("team"),
            field("opponents.name").`as`("opponents"),
            MATCHES.VICTORYTYPE,
            MATCHES.HOWMUCH,
            MATCHES.MATCHDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.RESULTSTRING,
            GROUNDS.KNOWNAS,
            MATCHES.CAID,
            field("team.id").`as`("teamid"),
            field("opponents.id").`as`("opponentsid"),
            coalesce(MATCHES.WHOWONID, 0).`as`("WhoWonId"),
            coalesce(MATCHES.TOSSTEAMID, 0).`as`("TossTeamId"),
        ).from(MATCHES)
            .join(EXTRAMATCHDETAILS).on(
                EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
            .join(TEAMS.`as`("team")).on(field("team.id").eq(EXTRAMATCHDETAILS.TEAMID)).and(MATCHES.ID.eq(
                EXTRAMATCHDETAILS.MATCHID))
            .join(TEAMS.`as`("opponents")).on(field("opponents.id").eq(EXTRAMATCHDETAILS.OPPONENTSID)).and(MATCHES.ID.eq(
                EXTRAMATCHDETAILS.MATCHID))
            .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
            .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(MATCHES.ID.`in`(select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE).where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))))
            .and(homeCountryIdCondition)
            .and(dateOrSeasonCondition)
            .and(teamIdCondition)
            .and(opponentsIdCondition)
            .and(groundCondition)
            .and(matchResultCondition)
            .and(homeOrAwayCondition)
            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))
            .and(MATCHES.HOWMUCH.ge(searchParameters.pagingParameters.limit))

        return cte

    }

    fun createCounts(
        cteScoresName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(cteScoresName)

}