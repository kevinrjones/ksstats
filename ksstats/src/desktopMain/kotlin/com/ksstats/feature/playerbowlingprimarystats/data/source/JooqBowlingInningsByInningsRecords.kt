package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.Record1
import org.jooq.Record17
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*
import java.time.LocalDate

object JooqBowlingInningsByInningsRecords {

    fun createTemporaryBowlingCte(searchParameters: SearchParameters): SelectJoinStep<Record17<Int?, String?, Any, Int, Int, Int, Int, Int, Int, Int, Int, String?, LocalDate?, Long?, String?, Int?, String?>> {
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
            field("T.Name", String::class).`as`("Team"),
            coalesce(field("bd.Balls", Int::class.java), 0).`as`("Balls"),
            coalesce(field("bd.Maidens", Int::class.java), 0).`as`("Maidens"),
            coalesce(field("bd.Dots", Int::class.java), 0).`as`("Dots"),
            coalesce(field("bd.Runs", Int::class.java), 0).`as`("Runs"),
            coalesce(field("bd.Wickets", Int::class.java), 0).`as`("Wickets"),
            field("bd.InningsOrder", Int::class.java).`as`("InningsNumber"),
            iif(
                field("bd.balls", Int::class.java).eq(0),
                0,
                field("bd.runs", Int::class.java).div(cast(field("bd.balls", Int::class.java), Double::class.java))
                    .mul(6),
            ).`as`("econ"),
            field("O.Name", Int::class.java).`as`("Opponents"),
            GROUNDS.KNOWNAS.`as`("Ground"),
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.SERIESDATE,
            MATCHES.BALLSPEROVER,
            MATCHES.CAID,
        ).from(
            BOWLINGDETAILS.`as`("bd")
                .join(TEAMS.`as`("T")).on(field("T.Id", Int::class.java).eq(field("bd.teamId", Int::class.java)))
                .and(field("bd.MatchType", String::class.java).eq(searchParameters.matchType.value))
                .join(TEAMS.`as`("O")).on(field("O.Id", Int::class.java).eq(field("bd.teamId", Int::class.java)))
                .join(PLAYERS).on(PLAYERS.ID.eq(field("bd.playerId", Int::class.java)))
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