package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable
import java.time.LocalDate

object JooqBowlingMatchTotalsRecords {

    fun createTemporaryBowlingCte(searchParameters: SearchParameters): SelectJoinStep<Record19<Int?, String?, String?, Any, Int, String?, LocalDate?, Long?, String?, Int?, String?, Int, Int, Serializable, Serializable, Serializable, Serializable, Serializable, Serializable>> {
        val teamIdCondition = if (searchParameters.teamId != 0) {
            and(BOWLINGDETAILS.TEAMID.eq(searchParameters.teamId))
        } else null
        val opponentsIdCondition = if (searchParameters.opponentsId != 0) {
            and(BOWLINGDETAILS.OPPONENTSID.eq(searchParameters.opponentsId))
        } else null
        val groundCondition = if (searchParameters.groundId != 0) {
            and(BOWLINGDETAILS.GROUNDID.eq(searchParameters.groundId))
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
            PLAYERS.ID,
            PLAYERS.FULLNAME,
            PLAYERS.SORTNAMEPART,
            field("T.Name", String::class).`as`("Team"),
            field("O.Name", Int::class.java).`as`("Opponents"),
            GROUNDS.KNOWNAS.`as`("Ground"),
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.SERIESDATE,
            MATCHES.BALLSPEROVER,
            MATCHES.CAID,
            field("bd.InningsOrder", Int::class.java).`as`("InningsNumber"),
            rowNumber().over().partitionBy(field("bd.MatchId"), field("bd.playerid"))
                .orderBy(field("bd.MatchId"), field("bd.playerid")).`as`("rn"),
            coalesce(
                sum(
                    field("bd.Balls", Int::class.java)
                ).over().partitionBy(field("bd.MatchId"), field("bd.playerid"))
                    .orderBy(field("bd.MatchId"), field("bd.playerid")), 0
            ).`as`("Balls"),
            coalesce(
                sum(field("bd.Maidens", Int::class.java)).over().partitionBy(field("bd.MatchId"), field("bd.playerid"))
                    .orderBy(field("bd.MatchId"), field("bd.playerid")), 0
            ).`as`("Maidens"),
            coalesce(
                sum(
                    field("bd.Dots", Int::class.java)
                ).over().partitionBy(field("bd.MatchId"), field("bd.playerid"))
                    .orderBy(field("bd.MatchId"), field("bd.playerid")), 0
            ).`as`("Dots"),
            coalesce(
                sum(field("bd.Runs", Int::class.java)).over().partitionBy(field("bd.MatchId"), field("bd.playerid"))
                    .orderBy(field("bd.MatchId"), field("bd.playerid")), 0
            ).`as`("Runs"),
            coalesce(
                sum(field("bd.Wickets", Int::class.java)).over().partitionBy(field("bd.MatchId"), field("bd.playerid"))
                    .orderBy(field("bd.MatchId"), field("bd.playerid")), 0
            ).`as`("Wickets"),
            coalesce(
                sum(field("bd.SyntheticBestBowling", Int::class.java)).over()
                    .partitionBy(field("bd.MatchId"), field("bd.playerid"))
                    .orderBy(field("bd.MatchId"), field("bd.playerid")), 0
            ).`as`("match_synbb"),
        ).from(
            BOWLINGDETAILS.`as`("bd")
                .join(PLAYERS).on(PLAYERS.ID.eq(field("bd.playerId", Int::class.java)))
                .join(TEAMS.`as`("T")).on(field("T.Id", Int::class.java).eq(field("bd.teamId", Int::class.java)))
                .join(TEAMS.`as`("O")).on(field("O.Id", Int::class.java).eq(field("bd.opponentsId", Int::class.java)))
                .and(field("bd.MatchType", String::class.java).eq(searchParameters.matchType.value))
                .join(MATCHES).on(
                    MATCHES.ID.eq(field("bd.matchId", Int::class.java))
                        .and(MATCHES.MATCHTYPE.eq(field("bd.matchType", String::class.java)))
                )
                .join(GROUNDS).on(GROUNDS.ID.eq(field("matches.locationId", Int::class.java)))
                .and(teamIdCondition)
                .and(opponentsIdCondition)
                .and(groundCondition)
                .and(field("bd.didbowl").eq(true))
                .and(
                    field("bd.MatchId").`in`(
                        select(MATCHES.ID)
                            .from(MATCHES)
                            .where(
                                field("id").`in`(
                                    select(BOWLINGDETAILS.MATCHID)
                                        .from(BOWLINGDETAILS)
                                )
                            )
                            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
                            .and(homeCountryIdCondition)
                            .and(dateOrSeasonCondition)
                    )
                )
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

    fun totalCountsCte(
        resultsCteName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(resultsCteName)


}