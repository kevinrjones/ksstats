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

object JooqMatchTotalsRecords {
    fun createScoresCte(searchParameters: SearchParameters): SelectConditionStep<Record15<Any, Any, Int?, String?, String?, Long?, String?, String?, String?, Int, Any, Any, Any, Any, Double>> {

        val teamIdCondition = if (searchParameters.teamId != 0) {
            and(field("i1.Teamid").eq(searchParameters.teamId))
        } else null
        val opponentsIdCondition = if (searchParameters.opponentsId != 0) {
            and(field("i1.OpponentsId").eq(searchParameters.opponentsId))
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
            inline(0).`as`("InningsOrder"),
            field("i1.TOTAL").add(coalesce(field("i2.total"), 0)).`as`("Total"),
            field("i1.wickets").add(coalesce(field("i2.wickets"), 0)).`as`("Wickets"),
            coalesce(field("i1.ballsbowled"), 0).add(coalesce(field("i2.ballsbowled"), 0)).`as`("BallsBowled"),
            field("i1.BallsPerOver"),
            iif(
                coalesce(field("i1.ballsbowled"), 0).add(coalesce(field("i2.ballsbowled"), 0)).eq(0),
                0.0,
                coalesce(field("i1.total", Int::class.java), 0).add(coalesce(field("i2.total"), 0))
                    .cast(Double::class.java).div(
                        coalesce(field("i1.ballsbowled", Int::class.java), 0).add(coalesce(field("i2.ballsbowled"), 0))
                    ).mul(6)
            ).`as`("rpo")
        ).from(MATCHES)
            .join(INNINGS.`as`("i1")).on(field("i1.MATCHID").eq(MATCHES.ID))
            .and(field("i1.inningsNumber").eq(inline(1)))
            .leftJoin(INNINGS.`as`("i2"))
            .on(field("i2.matchid").eq(MATCHES.ID))
            .and(field("i2.teamid").eq(field("i1.teamid")))
            .and(field("i2.inningsNumber").eq(inline(2)))
            .join(TEAMS.`as`("team")).on(field("i1.teamid").eq(field("team.id", Int::class.java)))
            .join(TEAMS.`as`("opponents")).on(field("i1.opponentsId").eq(field("opponents.id", Int::class.java)))
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
            .and(field("i1.total").add(coalesce(field("i2.total"), 0)).ge(searchParameters.pagingParameters.limit))

        return cte

    }

    fun createCounts(
        cteScoresName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(cteScoresName)

}