package com.ksstats.feature.teamrecordspirmarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable
import java.math.BigDecimal

object JooqTeamSeriesRecords {

    fun createTeamIdCte(searchParameters: SearchParameters): SelectOnConditionStep<Record6<Int?, Int?, Int?, Int?, String?, Int?>> {
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
            MATCHES.ID,
            EXTRAMATCHDETAILS.TEAMID,
            EXTRAMATCHDETAILS.OPPONENTSID,
            EXTRAMATCHDETAILS.RESULT,
            MATCHES.SERIESDATE,
            MATCHES.SERIESNUMBER
        )
            .from(EXTRAMATCHDETAILS)
            .join(MATCHES).on(MATCHES.ID.eq(EXTRAMATCHDETAILS.MATCHID))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(
                MATCHES.ID.`in`(
                    select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                        .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                )
            )
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

    fun createTotalsCte(cteTeamIdsName: String): SelectHavingStep<Record10<Any, Any, Any, BigDecimal, BigDecimal, BigDecimal, BigDecimal, Int?, Double, Double>> {
        val cte = select(
            field("${cteTeamIdsName}.TeamId"),
            field("${cteTeamIdsName}.OpponentsId"),
            field("${cteTeamIdsName}.SeriesNumber"),
            sum(field("INNINGS.DIDBAT", Int::class.java)).`as`("Innings"),
            sum(INNINGS.TOTAL).`as`("runs"),
            sum(INNINGS.WICKETS).`as`("wickets"),
            sum(INNINGS.BALLSBOWLED).`as`("ballsbowled"),
            max(INNINGS.TOTAL).`as`("hs"),
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
            .from(cteTeamIdsName)
            .join(MATCHES).on(MATCHES.ID.eq(field("${cteTeamIdsName}.id", Int::class.java)))
            .join(INNINGS).on(INNINGS.MATCHID.eq(field("${cteTeamIdsName}.id", Int::class.java)))
            .where(INNINGS.TEAMID.eq(field("${cteTeamIdsName}.TeamId", Int::class.java)))
            .and(INNINGS.OPPONENTSID.eq(field("${cteTeamIdsName}.OpponentsId", Int::class.java)))
            .groupBy(
                field("${cteTeamIdsName}.TeamId"),
                field("${cteTeamIdsName}.OpponentsId"),
                field("${cteTeamIdsName}.SeriesNumber"),
            )

        return cte
    }

    fun createCteForResult(cteTeamIdsName: String, resultType: Int): SelectHavingStep<Record4<Any, Any, Any, Int>> {
        return select(
            field("${cteTeamIdsName}.TeamId"),
            field("${cteTeamIdsName}.OpponentsId"),
            field("${cteTeamIdsName}.SeriesNumber"),
            count().`as`("count")
        ).from(cteTeamIdsName)
            .where(field("result").eq(resultType))
            .groupBy(
                field("${cteTeamIdsName}.TeamId"),
                field("${cteTeamIdsName}.OpponentsId"),
                field("${cteTeamIdsName}.SeriesNumber")
            )
    }

    fun createCteLowestScore(cteTeamIdsName: String): SelectHavingStep<Record4<Any, Any, Any, Any>> {
        return select(
            field("${cteTeamIdsName}.TeamId"),
            field("${cteTeamIdsName}.OpponentsId"),
            field("${cteTeamIdsName}.SeriesNumber"),
            min(field("total"))
        ).from(cteTeamIdsName)
            .join(MATCHES).on(MATCHES.ID.eq(field("${cteTeamIdsName}.id", Int::class.java)))
            .join(INNINGS).on(INNINGS.MATCHID.eq(field("${cteTeamIdsName}.id", Int::class.java)))
            .where(field("${cteTeamIdsName}.TeamId").eq(INNINGS.TEAMID))
            .and(INNINGS.COMPLETE)
            .groupBy(
                field("${cteTeamIdsName}.TeamId"),
                field("${cteTeamIdsName}.OpponentsId"),
                field("${cteTeamIdsName}.SeriesNumber")
            )
    }


    fun createSummaryCte(
        searchParameters: SearchParameters,
        cteTeamIdsName: String,
        cteTotalsName: String,
        cteWinsName: String,
        cteLossesName: String,
        cteDrawsName: String,
        cteTiesName: String,
        cteLowestScoreName: String,
    ): SelectConditionStep<Record16<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Serializable>> {
        val cte = select(
            field("T.NAME").`as`("Team"),
            field("O.NAME").`as`("Opponents"),
            coalesce(field("games.played"), 0).`as`("played"),
            coalesce(field("${cteTotalsName}.innings"), 0).`as`("innings"),
            coalesce(field("${cteWinsName}.count"), 0).`as`("won"),
            coalesce(field("${cteLossesName}.count"), 0).`as`("lost"),
            coalesce(field("${cteTiesName}.count"), 0).`as`("tied"),
            coalesce(field("${cteDrawsName}.count"), 0).`as`("drawn"),
            coalesce(field("games.seriesDate"), 0).`as`("Year"),
            coalesce(field("${cteTotalsName}.hs"), 0).`as`("hs"),
            coalesce(field("${cteTotalsName}.runs"), 0).`as`("runs"),
            coalesce(field("${cteTotalsName}.ballsbowled"), 0).`as`("balls"),
            coalesce(field("${cteTotalsName}.wickets"), 0).`as`("wickets"),
            coalesce(field("${cteTotalsName}.avg"), 0).`as`("average"),
            coalesce(field("${cteTotalsName}.rpo"), 0).`as`("rpo"),
            iif(
                field("${cteTotalsName}.ballsbowled").eq(0),
                0,
                field("${cteTotalsName}.runs").cast(Double::class.java)
                    .div(field("${cteTotalsName}.ballsbowled", Int::class.java)).mul(100),
            ).`as`("strikeRate")
        ).from(
            select(
                field("teamid"),
                field("opponentsid"),
                field("seriesNumber"),
                field("seriesDate"),
                count().`as`("played")
            ).from(cteTeamIdsName)
                .groupBy(
                    field("teamid"),
                    field("opponentsid"),
                    field("seriesNumber"),
                    field("seriesDate"),
                ).asTable("games")
        ).leftJoin(cteTotalsName)
            .on(field("${cteTotalsName}.TeamId").eq(field("games.teamid")))
            .and(field("${cteTotalsName}.OpponentsId").eq(field("games.opponentsId")))
            .and(field("${cteTotalsName}.seriesNumber").eq(field("games.seriesNumber")))
            .leftJoin(cteWinsName).on(field("${cteWinsName}.TeamId").eq(field("games.teamid")))
            .and(field("${cteWinsName}.OpponentsId").eq(field("games.opponentsId")))
            .and(field("${cteWinsName}.seriesNumber").eq(field("games.seriesNumber")))
            .leftJoin(cteLossesName).on(field("${cteLossesName}.TeamId").eq(field("games.teamid")))
            .and(field("${cteLossesName}.OpponentsId").eq(field("games.opponentsId")))
            .and(field("${cteLossesName}.seriesNumber").eq(field("games.seriesNumber")))
            .leftJoin(cteDrawsName).on(field("${cteDrawsName}.TeamId").eq(field("games.teamid")))
            .and(field("${cteDrawsName}.OpponentsId").eq(field("games.opponentsId")))
            .and(field("${cteDrawsName}.seriesNumber").eq(field("games.seriesNumber")))
            .leftJoin(cteTiesName).on(field("${cteTiesName}.TeamId").eq(field("games.teamid")))
            .and(field("${cteTiesName}.OpponentsId").eq(field("games.opponentsId")))
            .and(field("${cteTiesName}.seriesNumber").eq(field("games.seriesNumber")))
            .leftJoin(cteLowestScoreName).on(field("${cteLowestScoreName}.TeamId").eq(field("games.teamid")))
            .and(field("${cteLowestScoreName}.OpponentsId").eq(field("games.opponentsId")))
            .and(field("${cteLowestScoreName}.seriesNumber").eq(field("games.seriesNumber")))
            .join(TEAMS.`as`("T")).on(field("T.ID").eq(field("games.teamId", Int::class.java)))
            .join(TEAMS.`as`("O")).on(field("O.ID").eq(field("games.opponentsId", Int::class.java)))
            .where(field("${cteTotalsName}.runs").ge(searchParameters.pagingParameters.limit))
        return cte
    }

    fun createCounts(
        cteTotalsName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(cteTotalsName)

}