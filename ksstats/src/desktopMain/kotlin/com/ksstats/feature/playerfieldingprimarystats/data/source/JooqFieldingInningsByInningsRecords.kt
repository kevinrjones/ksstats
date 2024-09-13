package com.ksstats.feature.playerfieldingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable

object JooqFieldingInningsByInningsRecords {
    fun createTemporaryFieldingCte(
        searchParameters: SearchParameters,
    ): SelectConditionStep<Record17<Int, String, String, String, Int, Int, Int, Int, Int, Int, Int, String, String, String, String, String, String>> {
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

        val strikeRate = field("FieldingDetails.score", Int::class.java).cast(Double::class.java)
            .div(field("FieldingDetails.balls", Int::class.java)).mul(100)

        val cte = select(
            field("players.Id", Int::class.java).`as`("playerid"),
            field("T.Name", String::class.java).`as`("team"),
            field("players.FullName", String::class.java),
            field("players.SortnamePart", String::class.java),
            field("fielding.dismissals", Int::class.java),
            field("fielding.caughtwk", Int::class.java).add(field("fielding.stumped", Int::class.java)).`as`("wicketKeepingDismissals"),
            field("fielding.caughtwk", Int::class.java).add(field("fielding.caughtf", Int::class.java)).`as`("caught"),
            field("fielding.stumped", Int::class.java).`as`("stumpings"),
            field("fielding.caughtwk", Int::class.java).`as`("caughtkeeper"),
            field("fielding.caughtf", Int::class.java).`as`("caughtfielder"),
            field("fielding.InningsNumber", Int::class.java).`as`("innings"),
            field("O.Name", String::class.java).`as`("opponents"),
            field("Matches.matchDesignator", String::class.java),
            field("Matches.matchStartDate", String::class.java).`as`("matchDate"),
            field("Grounds.knownAs", String::class.java).`as`("ground"),
            field("Matches.matchStartDateAsOffset", String::class.java),
            field("matches.CaId", String::class.java).`as`("matchId"),
        ).from(FIELDING)
            .join(TEAMS.`as`("T")).on(field("T.id").eq(FIELDING.TEAMID))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(FIELDING.OPPONENTSID))
            .join(PLAYERS).on(PLAYERS.ID.eq(FIELDING.PLAYERID))
            .join(MATCHES).on(MATCHES.ID.eq(FIELDING.MATCHID))
            .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(groundCondition)
            .and(homeCountryIdCondition)
            .and(dateOrSeasonCondition)
            .and(opponentsIdCondition)
            .and(teamIdCondition)
            .and(matchResultCondition)
            .and(homeOrAwayCondition)
            .where(coalesce(FIELDING.DISMISSALS.ge(searchParameters.pagingParameters.limit)))
            .and(
                FIELDING.MATCHID
                    .`in`(
                        select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                            .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                    )
            )

        return cte
    }

    fun totalCountsCte(
        resultsCteName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(resultsCteName)

}