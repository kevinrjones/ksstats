package com.ksstats.feature.teamrecordspirmarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.EXTRAMATCHDETAILS
import com.ksstats.db.tables.references.FIELDING
import com.ksstats.db.tables.references.INNINGS
import com.ksstats.db.tables.references.TEAMS
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable
import java.math.BigDecimal

object JooqMatchSummaryRecords {
    fun createScoresCte(searchParameters: SearchParameters): SelectOnConditionStep<Record8<Int?, Int?, Int?, Int, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> {
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
            MATCHES.ID,
            EXTRAMATCHDETAILS.TEAMID,
            EXTRAMATCHDETAILS.RESULT,
            rowNumber().over().partitionBy(EXTRAMATCHDETAILS.TEAMID).`as`("rn"),
            sum(
                `when`(EXTRAMATCHDETAILS.RESULT.eq(1), 1).otherwise(0)
            ).over().partitionBy(EXTRAMATCHDETAILS.TEAMID).`as`("wins"),
            sum(
                `when`(EXTRAMATCHDETAILS.RESULT.eq(2), 1).otherwise(0)
            ).over().partitionBy(EXTRAMATCHDETAILS.TEAMID).`as`("lost"),
            sum(
                `when`(EXTRAMATCHDETAILS.RESULT.eq(4), 1).otherwise(0)
            ).over().partitionBy(EXTRAMATCHDETAILS.TEAMID).`as`("drawn"),
            sum(
                `when`(EXTRAMATCHDETAILS.RESULT.eq(8), 1).otherwise(0)
            ).over().partitionBy(EXTRAMATCHDETAILS.TEAMID).`as`("tied"),
        ).from(EXTRAMATCHDETAILS)
            .join(MATCHES).on(MATCHES.ID.eq(EXTRAMATCHDETAILS.MATCHID))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
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

    fun createTotalsCte(cteScoresName: String): SelectHavingStep<Record7<Any, BigDecimal, BigDecimal, BigDecimal, BigDecimal, Double, Double>> {
        val cte = select(
            field("${cteScoresName}.TeamId"),
            sum(field("INNINGS.DIDBAT", Int::class.java)).`as`("Innings"),
            sum(INNINGS.TOTAL).`as`("runs"),
            sum(INNINGS.WICKETS).`as`("wickets"),
            sum(INNINGS.BALLSBOWLED).`as`("ballsbowled"),
            iif(
                sum(INNINGS.WICKETS).cast(Double::class.java).eq(0.0),
                0.0,
                sum(INNINGS.TOTAL).cast(Double::class.java).div(sum(INNINGS.WICKETS))
            ).`as`("avg"),
            iif(
                sum(INNINGS.BALLSBOWLED).cast(Double::class.java).eq(0.0),
                0.0,
                sum(INNINGS.TOTAL).cast(Double::class.java).div(sum(INNINGS.BALLSBOWLED).div(6))
            ).`as`("rpo"),
        )
            .from(cteScoresName)
            .join(MATCHES).on(MATCHES.ID.eq(field("${cteScoresName}.id", Int::class.java)))
            .join(INNINGS).on(INNINGS.MATCHID.eq(field("${cteScoresName}.id", Int::class.java)))
            .where(INNINGS.TEAMID.eq(field("${cteScoresName}.TeamId", Int::class.java)))
            .groupBy(field("${cteScoresName}.TeamId"))

        return cte
    }

    fun createSummaryCte(
        searchParameters: SearchParameters,
        cteScoresName: String,
        cteTotalsName: String
    ): SelectOnConditionStep<Record13<String?, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Serializable>> {
        val cte = select(
            TEAMS.NAME.`as`("Team"),
            coalesce(field("games.played"), 0).`as`("played"),
            coalesce(field("${cteTotalsName}.innings"), 0).`as`("innings"),
            coalesce(field("games.wins"), 0).`as`("won"),
            coalesce(field("games.lost"), 0).`as`("lost"),
            coalesce(field("games.tied"), 0).`as`("tied"),
            coalesce(field("games.drawn"), 0).`as`("drawn"),
            coalesce(field("${cteTotalsName}.runs"), 0).`as`("runs"),
            coalesce(field("${cteTotalsName}.ballsbowled"), 0).`as`("balls"),
            coalesce(field("${cteTotalsName}.wickets"), 0).`as`("wickets"),
            coalesce(field("${cteTotalsName}.avg"), 0).`as`("average"),
            coalesce(field("${cteTotalsName}.rpo"), 0).`as`("runsPerOver"),
            iif(field("${cteTotalsName}.ballsbowled").eq(0),
                0,
                field("${cteTotalsName}.runs").cast(Double::class.java)
                    .div(field("${cteTotalsName}.ballsbowled", Int::class.java)).mul(100),
                ).`as`("strikeRate")
        ).from(
            select(
                field("teamid"),
                field("wins"),
                field("lost"),
                field("drawn"),
                field("tied"),
                field("rn"),
                count().`as`("played")
            ).from(cteScoresName).groupBy(field("teamid")).asTable("games")
        ).leftJoin(cteTotalsName).on(field("${cteTotalsName}.TeamId").eq(field("games.teamid")))
            .join(TEAMS).on(TEAMS.ID.eq(field("games.teamid", Int::class.java)))
        return cte
    }

    fun createCounts(
        cteScoresName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(cteScoresName)

}