package com.ksstats.feature.playerbattingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable

object JooqBattingMatchTotalsRecords {
    fun createTemporaryBattingCte(
        searchParameters: SearchParameters,
    ): SelectConditionStep<Record15<Int, String, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Serializable, Int, Int>> {

        val strikeRate =
            coalesce(field("bd1.score", Int::class.java), field("bd2.score", Int::class.java)).cast(Double::class.java)
                .div(coalesce(field("bd1.balls", Int::class.java), field("bd1.balls", Int::class.java))).mul(100)

        val cte = select(
            field("bd1.MatchId", Int::class.java),
            field("bd1.MatchType", String::class.java),
            field("bd1.TeamId", Int::class.java),
            field("bd1.OpponentsId", Int::class.java),
            field("bd1.PlayerId", Int::class.java),
            field("bd1.Score", Int::class.java).`as`("bat1"),
            field("bd2.Score", Int::class.java).`as`("bat2"),
            (coalesce(field("bd1.score", Int::class.java), 0)
                    + coalesce(field("bd2.score", Int::class.java), 0)).`as`("playerscore"),
            (coalesce(field("bd1.Balls", Int::class.java), 0)
                    + coalesce(field("bd2.Balls", Int::class.java), 0)).`as`("balls"),
            (coalesce(field("bd1.fours", Int::class.java), 0)
                    + coalesce(field("bd2.fours", Int::class.java), 0)).`as`("fours"),
            (coalesce(field("bd1.sixes", Int::class.java), 0)
                    + coalesce(field("bd2.sixes", Int::class.java), 0)).`as`("sixes"),
            (coalesce(field("bd1.minutes", Int::class.java), 0)
                    + coalesce(field("bd2.minutes", Int::class.java), 0)).`as`("minutes"),
            iif(
                coalesce(field("bd1.balls", Int::class.java), field("bd2.balls", Int::class.java)).eq(0),
                0,
                strikeRate
            ).`as`("sr"),
            field("bd1.captain", Int::class.java),
            field("bd1.wicketkeeper", Int::class.java),
        ).from(BATTINGDETAILS.`as`("bd1"))
            .leftOuterJoin(BATTINGDETAILS.`as`("bd2"))
            .on(field("bd1.matchId").eq(field("bd2.matchId")))
            .and(field("bd1.playerId").eq(field("bd2.playerId")))
            .and(field("bd1.matchType").eq(field("bd2.matchType")))
            .and(field("bd1.inningsNumber").ne(field("bd2.inningsNumber")))
            .where(field("bd1.matchType").eq(searchParameters.matchType.value))
            .and(field("bd1.inningsNumber").eq(1))
            .and(
                coalesce(field("bd1.score", Int::class.java), field("bd2.score"))
                    .ge(searchParameters.pagingParameters.limit)
            )

        return cte
    }

    fun createTemporaryBattingDetailsCte(
        tmp_bat_name: String,
        searchParameters: SearchParameters,
    ): SelectOnConditionStep<Record19<String, String, Int, Int, Int, Int, Int, Int, Int, Int, Double, Int, Int, String, String, String, String, String, String>> {
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
            field("Players.FullName", String::class.java),
            field("Players.SortnamePart", String::class.java),
            field("Players.Id", Int::class.java).`as`("playerid"),
            field("bat1", Int::class.java),
            field("bat2", Int::class.java),
            field("playerScore", Int::class.java).`as`("score"),
            field("balls", Int::class.java).`as`("balls"),
            field("sixes", Int::class.java).`as`("sixes"),
            field("fours", Int::class.java).`as`("fours"),
            field("minutes", Int::class.java).`as`("minutes"),
            field("sr", Double::class.java),
            field("captain", Int::class.java),
            field("wicketkeeper", Int::class.java),
            field("T.Name", String::class.java).`as`("team"),
            field("O.Name", String::class.java).`as`("opponents"),
            field("grounds.knownas", String::class.java).`as`("ground"),
            field("Matches.matchDesignator", String::class.java),
            field("Matches.matchStartDate", String::class.java).`as`("matchDate"),
            field("Matches.matchStartDateAsOffset", String::class.java),
        ).from(tmp_bat_name)
            .join(PLAYERS).on(PLAYERS.ID.eq(field("${tmp_bat_name}.playerId", Int::class.java)))
            .join(TEAMS.`as`("T")).on(field("T.id").eq(field("${tmp_bat_name}.teamId", Int::class.java)))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(field("${tmp_bat_name}.opponentsId", Int::class.java)))
            .join(MATCHES).on(MATCHES.ID.eq(field("${tmp_bat_name}.matchId", Int::class.java)))
            .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
            .and(field("${tmp_bat_name}.matchType", String::class.java).eq(searchParameters.matchType.value))
            .and(field("${tmp_bat_name}.matchType", String::class.java).eq(MATCHES.MATCHTYPE))
            .and(groundCondition)
            .and(homeCountryIdCondition)
            .and(dateOrSeasonCondition)
            .and(opponentsIdCondition)
            .and(teamIdCondition)
            .and(matchResultCondition)
            .and(homeOrAwayCondition)
            .and(
                field("${tmp_bat_name}.matchId", Int::class.java)
                    .`in`(
                        select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                            .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                    )
            )
            .and(field("Players.playerId").ne(0))

        return cte

    }
}