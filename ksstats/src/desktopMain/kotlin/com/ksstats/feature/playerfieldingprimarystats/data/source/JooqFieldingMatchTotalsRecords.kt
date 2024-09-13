package com.ksstats.feature.playerfieldingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.io.Serializable

object JooqFieldingMatchTotalsRecords {
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
            field("f1.dismissals", Int::class.java)
                .add(field("f2.dismissals", Int::class.java)).`as`("dismissals"),
            field("f1.caughtwk", Int::class.java)
                .add(field("f1.stumped", Int::class.java))
                .add(field("f2.caughtwk", Int::class.java))
                .add(field("f2.stumped", Int::class.java))
                .`as`("wicketKeepingDismissals"),
            field("f1.caughtwk", Int::class.java)
                .add(field("f1.caughtf", Int::class.java))
                .add(field("f2.caughtwk", Int::class.java))
                .add(field("f2.caughtf", Int::class.java))
                .`as`("caught"),
            field("f1.stumped", Int::class.java)
                .add(field("f2.stumped", Int::class.java))
                .`as`("stumpings"),
            field("f1.caughtwk", Int::class.java)
                .add(field("f2.caughtwk", Int::class.java))
                .`as`("caughtkeeper"),
            field("f1.caughtf", Int::class.java)
                .add(field("f2.caughtf", Int::class.java))
                .`as`("caughtfielder"),
            field("f1.InningsNumber", Int::class.java).`as`("innings"),
            field("O.Name", String::class.java).`as`("opponents"),
            field("Matches.matchDesignator", String::class.java),
            field("Matches.matchStartDate", String::class.java).`as`("matchDate"),
            field("Grounds.knownAs", String::class.java).`as`("ground"),
            field("Matches.matchStartDateAsOffset", String::class.java),
            field("matches.CaId", String::class.java).`as`("matchId"),
        ).from(FIELDING.`as`("f1"))
            .leftJoin(FIELDING.`as`("f2"))
            .on(field("f1.matchId").eq(field("f2.matchId")))
            .and(field("f1.playerId").eq(field("f2.playerId")))
            .join(TEAMS.`as`("T")).on(field("T.id").eq(field("f1.teamid")))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(field("f1.opponentsId")))
            .join(PLAYERS).on(PLAYERS.ID.eq(field("f1.playerId", Int::class.java)))
            .join(MATCHES).on(MATCHES.ID.eq(field("f1.matchId", Int::class.java)))
            .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(groundCondition)
            .and(homeCountryIdCondition)
            .and(dateOrSeasonCondition)
            .and(opponentsIdCondition)
            .and(teamIdCondition)
            .and(matchResultCondition)
            .and(homeOrAwayCondition)
            .where(coalesce(field("f1.dismissals").add(field("f2.dismissals"))
                .ge(searchParameters.pagingParameters.limit)))
            .and(field("f1.inningsNumber").eq(1))
            .and(field("f2.inningsNumber").eq(2))
            .and(field("f1.matchType").eq(searchParameters.matchType.value))
            .and(
                field("f1.matchId")
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