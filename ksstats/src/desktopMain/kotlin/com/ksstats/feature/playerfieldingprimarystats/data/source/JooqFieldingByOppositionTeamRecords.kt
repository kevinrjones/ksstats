package com.ksstats.feature.playerfieldingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable
import java.math.BigDecimal

object JooqFieldingByOppositionTeamRecords {

    fun createTemporaryFieldingCte(
        searchParameters: SearchParameters,
    ): SelectJoinStep<Record10<Int, Int, String, Int, Int, Int, Int, Int, Double, Int>> {

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

        val cte =
            select(
                field("f.MatchId", Int::class.java),
                field("f.playerid", Int::class.java),
                field("f.matchtype", String::class.java),
                field("f.inningsnumber", Int::class.java),
                field("f.teamId", Int::class.java),
                field("f.opponentsId", Int::class.java),
                field("f.caughtf", Int::class.java),
                field("f.caughtwk", Int::class.java),
                field("f.stumped", Double::class.java),
                field("f.dismissals", Int::class.java)
            ).from(
                select()
                    .from(FIELDING)
                    .where(
                        FIELDING.MATCHID.`in`(
                            select(MATCHES.ID)
                                .from(MATCHES)
                                .where(
                                    field("id").`in`(
                                        select(FIELDING.MATCHID)
                                            .from(FIELDING)
                                    )
                                )
                                .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
                                .and(homeCountryIdCondition)
                                .and(dateOrSeasonCondition)
                        )
                            .and(teamIdCondition)
                            .and(opponentsIdCondition)
                            .and(groundCondition)
                    )
                    .and(
                        field("matchid")
                            .`in`(
                                select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                                    .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                            )
                    )
                    .and(FIELDING.PLAYERID.ne(1)).asTable("f")
                    .join(EXTRAMATCHDETAILS).on(
                        EXTRAMATCHDETAILS.MATCHID.eq(field("f.MATCHID", Int::class.java))
                            .and(
                                EXTRAMATCHDETAILS.TEAMID.eq(field("f.TEAMID", Int::class.java))
                            )
                            .and(matchResultCondition)
                            .and(homeOrAwayCondition)
                    )
                    .join(MATCHES).on(MATCHES.ID.eq(field("f.matchId", Int::class.java)))
            )
        return cte
    }

    fun createTemporaryTeamsCte(
        searchParameters: SearchParameters,
    ): SelectHavingStep<Record3<Int?, String?, String>> {

        //todo: aggregateDistinct should be groupConcatDistinct when fixed in JOOQ

        val cte = select(
            PLAYERSTEAMS.PLAYERID,
            TEAMSMATCHTYPES.MATCHTYPE,
            aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
        ).from(PLAYERSTEAMS)
            .join(TEAMS).on(TEAMS.ID.eq(PLAYERSTEAMS.TEAMID))
            .join(TEAMSMATCHTYPES).on(TEAMS.ID.eq(TEAMSMATCHTYPES.TEAMID))
            .and(TEAMSMATCHTYPES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(PLAYERSTEAMS.MATCHTYPE.eq(searchParameters.matchType.value))
            .groupBy(PLAYERSTEAMS.PLAYERID, TEAMSMATCHTYPES.MATCHTYPE)


        return cte
    }

    fun createTemporaryFieldingCounts(fieldingCteName: String): SelectHavingStep<Record7<Int, Int, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> {
        return select(
            field("playerId", Int::class.java),
            field("OpponentsId", Int::class.java),
            field("matchType", String::class.java),
            sum(field("caughtf", Int::class.java)).`as`("caughtf"),
            sum(field("caughtwk", Int::class.java)).`as`("caughtwk"),
            sum(field("stumped", Int::class.java)).`as`("stumped"),
            sum(field("dismissals", Int::class.java)).`as`("dismissals"),
        ).from(fieldingCteName)
            .groupBy(field("playerid"), field("OpponentsId"), field("matchtype"))
    }

    fun createTemporaryFieldingBest(fieldingCteName: String): SelectConditionStep<Record7<Int, Int, String, Int, Int, Int, Int>> {
        return select(
            field("playerId", Int::class.java),
            field("OpponentsId", Int::class.java),
            field("matchType", String::class.java),
            field("caughtf", Int::class.java),
            field("caughtwk", Int::class.java),
            field("stumped", Int::class.java),
            field("dismissals", Int::class.java),
        ).from(
            select(
                field("playerId", Int::class.java),
                field("matchType", String::class.java),
                field("OpponentsId", String::class.java),
                field("caughtf", Int::class.java),
                field("caughtwk", Int::class.java),
                field("stumped", Int::class.java),
                field("dismissals", Int::class.java),
                rowNumber().over().partitionBy(field("playerid"), field("OpponentsId"), field("OpponentsId"))
                    .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

            ).from(fieldingCteName)
        ).where(field("rn", Int::class.java).eq(inline(1)))
    }

    fun createTemporaryPlayed(fieldingCteName: String): SelectHavingStep<Record3<Int, Int, Int>> {
        return select(
            field("playerId", Int::class.java),
            field("OpponentsId", Int::class.java),
            count().`as`("matches"),
        ).from(fieldingCteName)
            .where(field("inningsNumber", Int::class.java).eq(inline(1)))
            .groupBy(field("playerid"), field("OpponentsId"))
    }

    fun createTemporaryInnings(matchType: String, fieldingCteName: String): SelectHavingStep<Record3<Int, Int, Int>> {
        val selInningsNumber = select(
            field("matchId"),
            field("teamId"),
            field("playerId"),
            field("OpponentsId"),
        ).from(fieldingCteName)
            .where(field("inningsNumber", Int::class.java).eq(inline(1)))
            .asTable("tid")

        return select(
            field("playerId", Int::class.java),
            field("innings.OpponentsId", Int::class.java),
            count().`as`("innings"),
        ).from(INNINGS)
            .join(MATCHES).on(MATCHES.ID.eq(INNINGS.MATCHID))
            .and(MATCHES.MATCHTYPE.eq(matchType))
            .join(selInningsNumber)
            .on(
                field("innings.matchId").eq(field("tid.matchId"))
                    .and(field("innings.teamId").eq(field("tid.TeamId")))
            )
            .where(MATCHES.MATCHTYPE.eq(matchType))
            .groupBy(field("tid.playerId"), field("innings.OpponentsId"))

    }

    fun createResultsCte(
        searchParameters: SearchParameters,
        teamsCteName: String,
        fieldingCountsName: String,
        fieldingBestName: String,
        fieldingPlayedName: String,
        fieldingInningsName: String,
    ): SelectConditionStep<Record17<Int?, String?, String?, String, String, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int>> {
        val completedInnings =
            field(
                "innings.innings",
                Int::class.java
            ).minus(field("innings.notouts", Int::class.java))
        val strikeRate = field("innings.runs", Int::class.java).cast(Double::class.java)
            .div(field("innings.balls", Int::class.java)).mul(100)
        val average = field("innings.runs", Int::class.java).cast(Double::class.java).div(
            completedInnings
        )
        val bi = sqrt(average.mul(strikeRate))
        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                PLAYERS.FULLNAME.`as`("name"),
                PLAYERS.SORTNAMEPART,
                coalesce(field("${teamsCteName}.teams", String::class.java)).`as`("teams"),
                coalesce(field("O.name", String::class.java)).`as`("opponents"),
                field("${fieldingPlayedName}.matches", Int::class.java),
                field("${fieldingInningsName}.innings", Int::class.java),
                field("${fieldingCountsName}.dismissals", Int::class.java),
                field("${fieldingCountsName}.stumped", Int::class.java),
                field("${fieldingCountsName}.caughtwk", Int::class.java),
                field("${fieldingCountsName}.caughtF", Int::class.java),
                field(
                    "${fieldingCountsName}.caughtWk",
                    Int::class.java
                ).add(field("${fieldingCountsName}.stumped", Int::class.java)).`as`("wicketkeeperdismissals"),
                field(
                    "${fieldingCountsName}.caughtwK",
                    Int::class.java
                ).add(field("${fieldingCountsName}.caughtF", Int::class.java)).`as`("caught"),
                field("${fieldingBestName}.dismissals", Int::class.java).`as`("bestDismissals"),
                field("${fieldingBestName}.caughtF", Int::class.java).`as`("bestCaughtFielder"),
                field("${fieldingBestName}.caughtWk", Int::class.java).`as`("bestCaughtKeeper"),
                field("${fieldingBestName}.stumped", Int::class.java).`as`("bestStumpings"),
            ).from(
                fieldingCountsName
            )
                .join(PLAYERS).on(PLAYERS.ID.eq(field("${fieldingCountsName}.playerid", Int::class.java)))
                .leftJoin(fieldingPlayedName)
                .on(field("${fieldingPlayedName}.playerId").eq(field("${fieldingCountsName}.playerId")))
                .and(field("${fieldingPlayedName}.OpponentsId").eq(field("${fieldingCountsName}.OpponentsId")))
                .leftJoin(fieldingInningsName)
                .on(field("${fieldingInningsName}.playerId").eq(field("${fieldingCountsName}.playerId")))
                .and(field("${fieldingInningsName}.OpponentsId").eq(field("${fieldingCountsName}.OpponentsId")))
                .leftJoin(fieldingBestName)
                .on(field("${fieldingBestName}.playerId").eq(field("${fieldingCountsName}.playerId")))
                .and(field("${fieldingBestName}.matchType").eq(field("${fieldingCountsName}.matchType")))
                .and(field("${fieldingBestName}.OpponentsId").eq(field("${fieldingCountsName}.OpponentsId")))
                .leftJoin(teamsCteName)
                .on(
                    field("${teamsCteName}.playerId", Int::class.java)
                        .eq(
                            field("${fieldingCountsName}.playerid", Int::class.java)
                        )
                ).and(field("${teamsCteName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(field("${fieldingCountsName}.OpponentsId")))
                .where(coalesce(field("${fieldingCountsName}.dismissals").ge(searchParameters.pagingParameters.limit)))


        return cte
    }

    fun totalCountsCte(
        resultsCteName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(resultsCteName)

}