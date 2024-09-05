package com.ksstats.feature.playerbattingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable

object JooqBattingByYearOfMatchStartRecords {

    fun createTemporaryBattingCte(
        searchParameters: SearchParameters,
    ): SelectJoinStep<Record18<Int, String, Int, Int, Int, Int, Int, Int, Int, Int, Double, Int, Int, Int, Int, Int, Int, Int?>> {

        val teamIdCondition = if (searchParameters.teamId != 0) {
            and(BATTINGDETAILS.TEAMID.eq(searchParameters.teamId))
        } else null
        val opponentsIdCondition = if (searchParameters.opponentsId != 0) {
            and(BATTINGDETAILS.OPPONENTSID.eq(searchParameters.opponentsId))
        } else null
        val groundCondition = if (searchParameters.groundId != 0) {
            and(BATTINGDETAILS.GROUNDID.eq(searchParameters.groundId))
        } else null
        val homeCountryIdCondition = if (searchParameters.hostCountryId != 0) {
            and(MATCHES.HOMECOUNTRYID.eq(searchParameters.hostCountryId))
        } else null
        val dateOrSeasonCondition = if (searchParameters.season != "All") {
            and(MATCHES.MATCHSTARTYEAR.eq(searchParameters.season))
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
                field("bd.MatchId", Int::class.java),
                field("bd.matchtype", String::class.java),
                field("bd.playerid", Int::class.java),
                field("bd.teamid", Int::class.java),
                field("bd.opponentsid", Int::class.java),
                field("bd.inningsnumber", Int::class.java),
                field("bd.dismissaltype", Int::class.java),
                field("bd.score", Int::class.java),
                field("bd.position", Int::class.java),
                field("bd.notout", Int::class.java),
                field("bd.NotOutAdjustedScore", Double::class.java),
                field("bd.balls", Int::class.java),
                field("bd.fours", Int::class.java),
                field("bd.sixes", Int::class.java),
                field("bd.hundred", Int::class.java),
                field("bd.fifty", Int::class.java),
                field("bd.duck", Int::class.java),
                field("Matches.MatchStartYear", Int::class.java),
            ).from(
                select()
                    .from(BATTINGDETAILS)
                    .where(
                        BATTINGDETAILS.MATCHID.`in`(
                            select(MATCHES.ID)
                                .from(MATCHES)
                                .where(
                                    field("id").`in`(
                                        select(BATTINGDETAILS.MATCHID)
                                            .from(BATTINGDETAILS)
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
                    .and(BATTINGDETAILS.PLAYERID.ne(1)).asTable("bd")
                    .join(MATCHES).on(MATCHES.ID.eq(field("bd.matchId", Int::class.java)))
                    .join(EXTRAMATCHDETAILS).on(
                        EXTRAMATCHDETAILS.MATCHID.eq(field("bd.MATCHID", Int::class.java))
                            .and(
                                EXTRAMATCHDETAILS.TEAMID.eq(field("bd.TEAMID", Int::class.java))
                            )
                            .and(matchResultCondition)
                            .and(homeOrAwayCondition)
                    )
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


    fun createResultsCte(
        batCteName: String,
        teamsCteName: String,
        searchParameters: SearchParameters,
    ): SelectJoinStep<Record20<Int?, String?, String?, String, String, Int, Int, Int, Int, Double, Int, Int, Int, Int, Int, Int, String, Serializable, Serializable, Serializable>> {
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
                field("teams", String::class.java).`as`("team"),
                field("o.name", String::class.java).`as`("opponents"),
                field("innings.matches", Int::class.java),
                field("innings.innings", Int::class.java),
                field("innings.notouts", Int::class.java),
                field("innings.runs", Int::class.java),
                field("innings.highestscore", Double::class.java),
                field("innings.hundreds", Int::class.java),
                field("innings.fifties", Int::class.java),
                field("innings.ducks", Int::class.java),
                field("innings.fours", Int::class.java),
                field("innings.sixes", Int::class.java),
                field("innings.balls", Int::class.java),
                field("innings.MatchStartYear", String::class.java),
                iif(
                    field("innings.innings", Int::class.java).minus(field("innings.notouts", Int::class.java)).eq(0),
                    0, average
                ).`as`("avg"),
                iif(field("innings.balls", Int::class.java).eq(0), 0, strikeRate).`as`("sr"),
                iif(
                    field("innings.innings", Int::class.java).minus(field("innings.notouts", Int::class.java)).eq(0)
                        .or(field("innings.balls").eq(0)),
                    0, bi
                ).`as`("bi"),
            ).from(
                select(
                    count(
                        `when`(field("inningsnumber").eq(1), 1)
                    ).`as`("matches"),
                    count(
                        `when`(
                            field("dismissaltype").ne(11)
                                .and(field("dismissaltype").ne(14))
                                .and(field("dismissaltype").ne(15)),
                            1
                        )
                    ).`as`("innings"),
                    field("MatchStartYear"),
                    field("playerId"),
                    field("matchType"),
                    sum(field("score", Int::class.java)).`as`("runs"),
                    sum(field("notout", Int::class.java)).`as`("notouts"),
                    sum(field("hundred", Int::class.java)).`as`("hundreds"),
                    sum(field("fifty", Int::class.java)).`as`("fifties"),
                    max(field("NotOutAdjustedScore", Int::class.java)).`as`("HighestScore"),
                    sum(field("duck", Int::class.java)).`as`("ducks"),
                    sum(field("fours", Int::class.java)).`as`("fours"),
                    sum(field("sixes", Int::class.java)).`as`("sixes"),
                    sum(field("balls", Int::class.java)).`as`("balls"),
                ).from(batCteName)
                    .groupBy(field("matchType"),field("playerid"), field("MatchStartYear"),)
                    .asTable("innings")
                    .join(PLAYERS).on(PLAYERS.ID.eq(field("innings.playerid", Int::class.java)))
                    .leftJoin(teamsCteName)
                    .on(
                        field("${teamsCteName}.playerId", Int::class.java)
                            .eq(
                                field("innings.playerid", Int::class.java)
                            )
                    ).and(field("${teamsCteName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId))
                    .where(coalesce(field("innings.runs").ge(searchParameters.pagingParameters.limit)))
            )

        return cte
    }

    fun totalCountsCte(
        resultsCteName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(resultsCteName)

}