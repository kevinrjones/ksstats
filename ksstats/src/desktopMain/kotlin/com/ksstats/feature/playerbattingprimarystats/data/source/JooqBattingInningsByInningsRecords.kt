package com.ksstats.feature.playerbattingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable

object JooqBattingInningsByInningsRecords {
    fun createTemporaryBattingCte(
        searchParameters: SearchParameters,
    ): SelectConditionStep<Record22<String, String, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String, String, String, String, Int, String, Serializable, String, String>> {
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

        val strikeRate = field("BattingDetails.score", Int::class.java).cast(Double::class.java)
            .div(field("BattingDetails.balls", Int::class.java)).mul(100)

        val cte = select(
            field("players.FullName", String::class.java),
            field("players.SortnamePart", String::class.java),
            field("players.Id", Int::class.java).`as`("playerid"),
            field("BattingDetails.score", Int::class.java),
            field("BattingDetails.notOut", Int::class.java),
            field("BattingDetails.position", Int::class.java),
            field("BattingDetails.balls", Int::class.java),
            field("BattingDetails.fours", Int::class.java),
            field("BattingDetails.sixes", Int::class.java),
            field("BattingDetails.minutes", Int::class.java),
            field("BattingDetails.captain", Int::class.java),
            field("BattingDetails.wicketKeeper", Int::class.java),
            field("T.Name", String::class.java).`as`("team"),
            field("O.Name", String::class.java).`as`("opponents"),
            field("Matches.matchDesignator", String::class.java),
            field("Matches.matchStartDate", String::class.java).`as`("matchDate"),
            field("Matches.matchStartDateAsOffset", String::class.java),
            field("BattingDetails.inningsOrder", Int::class.java).`as`("inningsNumber"),
            field("BattingDetails.dismissal", String::class.java),
            iif(field("BattingDetails.balls", Int::class.java).eq(0), 0, strikeRate).`as`("sr"),
            field("Grounds.knownAs", String::class.java).`as`("ground"),
            field("matches.CaId", String::class.java).`as`("matchId"),
        ).from(BATTINGDETAILS)
            .join(TEAMS.`as`("T")).on(field("T.id").eq(BATTINGDETAILS.TEAMID))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(BATTINGDETAILS.OPPONENTSID))
            .join(PLAYERS).on(PLAYERS.ID.eq(BATTINGDETAILS.PLAYERID))
            .join(MATCHES).on(MATCHES.ID.eq(BATTINGDETAILS.MATCHID))
            .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(groundCondition)
            .and(homeCountryIdCondition)
            .and(dateOrSeasonCondition)
            .and(opponentsIdCondition)
            .and(teamIdCondition)
            .and(matchResultCondition)
            .and(homeOrAwayCondition)
            .where(coalesce(BATTINGDETAILS.SCORE.ge(searchParameters.pagingParameters.limit)))
            .and(
                BATTINGDETAILS.MATCHID
                    .`in`(
                        select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                            .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                    )
            )

        return cte
    }
}