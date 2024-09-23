package com.ksstats.feature.teamrecordspirmarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.EXTRAMATCHDETAILS
import com.ksstats.db.tables.references.FIELDING
import com.ksstats.db.tables.references.INNINGS
import com.ksstats.db.tables.references.TEAMS
import org.jooq.*
import org.jooq.impl.DSL.*
import java.math.BigDecimal

object JooqInningsByInningsRecords {
    fun createScoresCte(searchParameters: SearchParameters): SelectConditionStep<Record15<Any, Any, Int?, String?, String?, Long?, String?, String?, String?, Int?, Int?, Int?, Int?, Int?, Double>> {

        val teamIdCondition = if (searchParameters.teamId != 0) {
            and(FIELDING.TEAMID.eq(searchParameters.teamId))
        } else null
        val opponentsIdCondition = if (searchParameters.opponentsId != 0) {
            and(FIELDING.OPPONENTSID.eq(searchParameters.opponentsId))
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
            EXTRAMATCHDETAILS.RESULT,
            MATCHES.MATCHTITLE,
            MATCHES.MATCHDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.RESULTSTRING,
            MATCHES.LOCATION,
            MATCHES.CAID,
            INNINGS.INNINGSORDER,
            INNINGS.TOTAL,
            INNINGS.WICKETS,
            INNINGS.BALLSBOWLED,
            INNINGS.BALLSPEROVER,
            iif(
                INNINGS.BALLSBOWLED.eq(0),
                0.0,
                INNINGS.TOTAL.cast(Double::class.java).div(INNINGS.BALLSBOWLED.mul(6))
            ).`as`("rpo")
        ).from(MATCHES)
            .join(INNINGS).on(INNINGS.MATCHID.eq(MATCHES.ID))
            .join(TEAMS.`as`("team")).on(INNINGS.TEAMID.eq(field("team.id", Int::class.java)))
            .join(TEAMS.`as`("opponents")).on(INNINGS.OPPONENTSID.eq(field("opponents.id", Int::class.java)))
            .join(EXTRAMATCHDETAILS).on(
                EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID)
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("team.id", Int::class.java)))
                    .and(homeOrAwayCondition)
            )
            .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(homeCountryIdCondition)
            .and(dateOrSeasonCondition)
            .and(teamIdCondition)
            .and(opponentsIdCondition)
            .and(groundCondition)
            .and(matchResultCondition)
            .and(homeOrAwayCondition)
            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))

        return cte

    }

    fun createCounts(
        cteScoresName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(cteScoresName)

}