package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.db.tables.Matches.Companion.MATCHES
import com.ksstats.db.tables.references.*
import org.jooq.*
import org.jooq.impl.DSL.*
import java.math.BigDecimal

object JooqBowlingSeriesRecords {
    fun createTemporaryBowlingCte(
        searchParameters: SearchParameters,
    ): SelectJoinStep<Record20<Int, String, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Double, Int, String>> {

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

        val cte =
            select(
                field("bd.MatchId", Int::class.java),
                field("bd.matchtype", String::class.java),
                field("bd.playerid", Int::class.java),
                field("bd.didbowl", Int::class.java),
                field("bd.inningsnumber", Int::class.java),
                field("bd.teamid", Int::class.java),
                field("bd.opponentsid", Int::class.java),
                field("bd.balls", Int::class.java),
                field("bd.maidens", Int::class.java),
                field("bd.runs", Int::class.java),
                field("bd.wickets", Int::class.java),
                field("bd.noballs", Int::class.java),
                field("bd.wides", Int::class.java),
                field("bd.dots", Int::class.java),
                field("bd.fours", Int::class.java),
                field("bd.sixes", Int::class.java),
                field("bd.tenfor", Int::class.java),
                field("bd.SyntheticBestBowling", Double::class.java),
                field("m.SeriesNumber", Int::class.java),
                field("m.SeriesDate", String::class.java),
            ).from(
                select()
                    .from(BOWLINGDETAILS)
                    .where(
                        BOWLINGDETAILS.MATCHID.`in`(
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
                    .and(BOWLINGDETAILS.PLAYERID.ne(1)).asTable("bd")
                    .join(MATCHES.`as`("m")).on(field("m.Id").eq(field("bd.matchId")))
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

    fun createResultsCte(
        searchParameters: SearchParameters,
        bowlCteName: String,
        fivesCteName: String,
        tensCteName: String,
        bbmCteName: String,
    ): SelectJoinStep<Record> {
        val strikeRate = field("innings.balls", Int::class.java).cast(Double::class.java)
            .div(field("innings.wickets", Int::class.java))
        val economy = field("innings.runs", Int::class.java).cast(Double::class.java)
            .div(field("innings.balls", Int::class.java)).mul(6)
        val average = field("innings.runs", Int::class.java).cast(Double::class.java).div(
            field("innings.wickets", Int::class.java)
        )
        val runsPerBall = field("innings.runs", Int::class.java).cast(Double::class.java).mul(100).div(
            field("innings.balls", Int::class.java)
        )
        val bi = sqrt(average.mul(runsPerBall))
        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                field("innings.seriesDate", Int::class.java).`as`("year"),
                PLAYERS.FULLNAME.`as`("name"),
                PLAYERS.SORTNAMEPART,
                field("innings.matches", Int::class.java),
                field("innings.innings", Int::class.java),
                field("innings.balls", Int::class.java),
                field("innings.maidens", Int::class.java),
                field("innings.runs", Int::class.java),
                field("innings.wickets", Int::class.java),
                field("innings.noballs", Int::class.java),
                field("innings.wides", Int::class.java),
                field("innings.dots", Int::class.java),
                field("innings.fours", Int::class.java),
                field("innings.sixes", Int::class.java),
                field("bocrto_fives.fivefor", Int::class.java),
                field("bocrto_tens.tenfor", Int::class.java),
                field("innings.bbi", Double::class.java),
                field("bocrto_bbm.bbm", Double::class.java),
                field("T.Name", String::class.java).`as`("team"),
                field("O.Name", String::class.java).`as`("opponents"),
                field("bocrto_bbm.bbm", Double::class.java),
                iif(
                    field("innings.wickets", Int::class.java).eq(0),
                    0, average
                ).`as`("avg"),

                iif(field("innings.balls", Int::class.java).eq(0), 0, economy).`as`("econ"),
                iif(field("innings.wickets", Int::class.java).eq(0), 0, strikeRate).`as`("sr"),

                iif(
                    field("innings.wickets", Int::class.java).eq(0)
                        .or(field("innings.balls").eq(0)),
                    0, bi
                ).`as`("bi"),
            ).from(
                select(
                    count(
                        `when`(field("inningsnumber").eq(1), 1)
                    ).`as`("matches"),
                    sum(field("didbowl", Int::class.java)).`as`("innings"),
                    field("playerId"),
                    field("matchType"),
                    field("seriesNumber"),
                    field("seriesDate"),
                    field("teamId"),
                    field("opponentsId"),
                    sum(field("balls", Int::class.java)).`as`("balls"),
                    sum(field("maidens", Int::class.java)).`as`("maidens"),
                    sum(field("wickets", Int::class.java)).`as`("wickets"),
                    sum(field("runs", Int::class.java)).`as`("runs"),
                    sum(field("wides", Int::class.java)).`as`("wides"),
                    sum(field("noballs", Int::class.java)).`as`("noballs"),
                    sum(field("fours", Int::class.java)).`as`("fours"),
                    sum(field("sixes", Int::class.java)).`as`("sixes"),
                    sum(field("dots", Int::class.java)).`as`("dots"),
                    max(field("syntheticBestBowling", Int::class.java)).`as`("bbi"),
                ).from(bowlCteName)
                    .groupBy(field("matchtype"), field("playerid"), field("seriesNumber"), field("seriesDate"))
                    .having(sum(field("didbowl", Int::class.java)).cast(Int::class.java).gt(0))
                    .asTable("innings")
                    .join(PLAYERS).on(PLAYERS.ID.eq(field("innings.playerid", Int::class.java)))
                    .join(TEAMS.`as`("T")).on(field("T.id").eq(field("innings.teamId")))
                    .join(TEAMS.`as`("O")).on(field("O.id").eq(field("innings.opponentsId")))
                    .leftJoin(fivesCteName)
                    .on(
                        field("${fivesCteName}.playerId", Int::class.java)
                            .eq(field("innings.playerId", Int::class.java))
                    )
                    .and(field("${fivesCteName}.seriesNumber", Int::class.java)
                        .eq(field("innings.seriesNumber", Int::class.java)))
                    .leftOuterJoin(tensCteName)
                    .on(
                        field("${tensCteName}.playerId", Int::class.java)
                            .eq(field("innings.playerId", Int::class.java))
                    )
                    .and(field("${tensCteName}.seriesNumber", Int::class.java)
                        .eq(field("innings.seriesNumber", Int::class.java)))
                    .leftOuterJoin(bbmCteName)
                    .on(
                        field("${bbmCteName}.playerId", Int::class.java)
                            .eq(field("innings.playerId", Int::class.java))
                    )
                    .and(field("${bbmCteName}.seriesNumber", Int::class.java)
                        .eq(field("innings.seriesNumber", Int::class.java)))

                    // todo: pass across the correct limit
                    .where(coalesce(field("innings.wickets").ge(searchParameters.pagingParameters.limit)))
            )

        return cte
    }

    fun totalCountsCte(
        resultsCteName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(resultsCteName)


    fun createFives(
        tmpBowl: String,
        fivesLimit: Int,
    ): SelectHavingStep<Record3<Int, Int, BigDecimal>> {
        val cte = select(
            field("playerid", Int::class.java),
            field("seriesNumber", Int::class.java),
            sum(
                `when`(
                    field("wickets", Int::class.java).ge(fivesLimit), 1
                )
            ).`as`("fivefor"),
        ).from(tmpBowl).where(field("didbowl", Int::class.java).eq(1))
            .groupBy(field("playerid"),field("seriesNumber"))

        return cte
    }

    fun createTens(
        tmpBowl: String,
    ): SelectHavingStep<Record3<Int, Int, Int>> {

        val cte = select(
            field("playerid", Int::class.java),
            field("seriesNumber", Int::class.java),
            count().`as`("tenfor"),
        ).from(tmpBowl)
            .where(field("inningsnumber", Int::class.java).eq(1))
            .and(field("tenfor", Int::class.java).eq(1))
            .groupBy(field("playerid"),field("seriesNumber"))


        return cte
    }


    fun createBbmAll(
        tmpBowl: String,
    ): SelectHavingStep<Record4<Int, Int, Int, Double>> {


        val runs = value(0.1)
            .div((sum(field("runs", Int::class.java)).cast(Double::class.java)))
        val wickets = sum(field("wickets", Int::class.java))
        val bbm = runs.add(wickets)


        val cte = select(
            field("playerid", Int::class.java),
            field("seriesNumber", Int::class.java),
            field("matchId", Int::class.java),
            iif(
                sum(field("runs", Int::class.java)).cast(Int::class.java).eq(0),
                0.0,
                bbm
            ).`as`("bbm")

        )
            .from(tmpBowl)
            .where(field("balls", Int::class.java).gt(0))
            .and(field("didbowl", Int::class.java).eq(1))
            .groupBy(field("playerId"), field("matchId"),field("seriesNumber"))


        return cte
    }

    fun createBbm(
        tmpBbm: String,
    ): SelectHavingStep<Record3<Int, Int, Double>> {

        val cte = select(
            field("playerid", Int::class.java),
            field("seriesNumber", Int::class.java),
            max(field("bbm", Double::class.java)).`as`("bbm")
        ).from(tmpBbm)
            .groupBy(field("playerid"),field("seriesNumber"))

        return cte
    }


}