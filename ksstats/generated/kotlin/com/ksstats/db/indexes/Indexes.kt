/*
 * This file is generated by jOOQ.
 */
package com.ksstats.db.indexes


import com.ksstats.db.tables.Battingdetails
import com.ksstats.db.tables.Bowlingdetails
import com.ksstats.db.tables.Countries
import com.ksstats.db.tables.Extramatchdetails
import com.ksstats.db.tables.Fielding
import com.ksstats.db.tables.Grounds
import com.ksstats.db.tables.Groundsmatchtypes
import com.ksstats.db.tables.Innings
import com.ksstats.db.tables.Matches
import com.ksstats.db.tables.Matchreferees
import com.ksstats.db.tables.Matchsubtype
import com.ksstats.db.tables.Partnerships
import com.ksstats.db.tables.Players
import com.ksstats.db.tables.Playersmatches
import com.ksstats.db.tables.Playersteams
import com.ksstats.db.tables.Reserveumpires
import com.ksstats.db.tables.Scorers
import com.ksstats.db.tables.Teams
import com.ksstats.db.tables.Teamsmatchtypes
import com.ksstats.db.tables.Tvumpires
import com.ksstats.db.tables.Umpires

import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val BATTINGDETAILS_CAPTAIN: Index = Internal.createIndex(DSL.name("BattingDetails_Captain"), Battingdetails.BATTINGDETAILS, arrayOf(Battingdetails.BATTINGDETAILS.CAPTAIN), false)
val BATTINGDETAILS_MATCHID_PLAYERID: Index = Internal.createIndex(DSL.name("BattingDetails_MatchId_PlayerId"), Battingdetails.BATTINGDETAILS, arrayOf(Battingdetails.BATTINGDETAILS.MATCHID, Battingdetails.BATTINGDETAILS.PLAYERID), false)
val BATTINGDETAILS_MATCHTYPE: Index = Internal.createIndex(DSL.name("BattingDetails_MatchType"), Battingdetails.BATTINGDETAILS, arrayOf(Battingdetails.BATTINGDETAILS.MATCHTYPE), false)
val BATTINGDETAILS_SCORE: Index = Internal.createIndex(DSL.name("BattingDetails_Score"), Battingdetails.BATTINGDETAILS, arrayOf(Battingdetails.BATTINGDETAILS.SCORE), false)
val BATTINGDETAILS_TEAMID_MATCHID: Index = Internal.createIndex(DSL.name("BattingDetails_TeamId_MatchId"), Battingdetails.BATTINGDETAILS, arrayOf(Battingdetails.BATTINGDETAILS.TEAMID, Battingdetails.BATTINGDETAILS.MATCHID), false)
val BATTINGDETAILS_WICKETKEEPER: Index = Internal.createIndex(DSL.name("BattingDetails_WicketKeeper"), Battingdetails.BATTINGDETAILS, arrayOf(Battingdetails.BATTINGDETAILS.WICKETKEEPER), false)
val BOWLINGDETAILS_BALLS: Index = Internal.createIndex(DSL.name("BowlingDetails_Balls"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.BALLS), false)
val BOWLINGDETAILS_DOTS: Index = Internal.createIndex(DSL.name("BowlingDetails_Dots"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.DOTS), false)
val BOWLINGDETAILS_FOURS: Index = Internal.createIndex(DSL.name("BowlingDetails_Fours"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.FOURS), false)
val BOWLINGDETAILS_MAIDENS: Index = Internal.createIndex(DSL.name("BowlingDetails_Maidens"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.MAIDENS), false)
val BOWLINGDETAILS_MATCHTYPE: Index = Internal.createIndex(DSL.name("BowlingDetails_MatchType"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.MATCHTYPE), false)
val BOWLINGDETAILS_NOBALLS: Index = Internal.createIndex(DSL.name("BowlingDetails_NoBalls"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.NOBALLS), false)
val BOWLINGDETAILS_RUNS: Index = Internal.createIndex(DSL.name("BowlingDetails_Runs"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.RUNS), false)
val BOWLINGDETAILS_SIXES: Index = Internal.createIndex(DSL.name("BowlingDetails_Sixes"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.SIXES), false)
val BOWLINGDETAILS_WICKETS: Index = Internal.createIndex(DSL.name("BowlingDetails_Wickets"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.WICKETS), false)
val BOWLINGDETAILS_WIDES: Index = Internal.createIndex(DSL.name("BowlingDetails_Wides"), Bowlingdetails.BOWLINGDETAILS, arrayOf(Bowlingdetails.BOWLINGDETAILS.WIDES), false)
val COUNTRIES_COUNTRYID: Index = Internal.createIndex(DSL.name("Countries_CountryId"), Countries.COUNTRIES, arrayOf(Countries.COUNTRIES.COUNTRYID), false)
val EXTRAMATCHDETAILS_MATCHID: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_MatchId"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.MATCHID), false)
val EXTRAMATCHDETAILS_MATCHID_OPPONENTSID: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_MatchId_OpponentsId"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.MATCHID, Extramatchdetails.EXTRAMATCHDETAILS.OPPONENTSID), false)
val EXTRAMATCHDETAILS_MATCHID_TEAMID: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_MatchId_TeamId"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.MATCHID, Extramatchdetails.EXTRAMATCHDETAILS.TEAMID), false)
val EXTRAMATCHDETAILS_MATCHID_TEAMID_OPPONENTSID: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_MatchId_TeamId_OpponentsId"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.MATCHID, Extramatchdetails.EXTRAMATCHDETAILS.TEAMID, Extramatchdetails.EXTRAMATCHDETAILS.OPPONENTSID), false)
val EXTRAMATCHDETAILS_MATCHTYPE: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_MatchType"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.MATCHTYPE), false)
val EXTRAMATCHDETAILS_OPPONENTSID: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_OpponentsId"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.OPPONENTSID), false)
val EXTRAMATCHDETAILS_TEAMID: Index = Internal.createIndex(DSL.name("ExtraMatchDetails_TeamId"), Extramatchdetails.EXTRAMATCHDETAILS, arrayOf(Extramatchdetails.EXTRAMATCHDETAILS.TEAMID), false)
val FIELDING_CAUGHTF: Index = Internal.createIndex(DSL.name("Fielding_CaughtF"), Fielding.FIELDING, arrayOf(Fielding.FIELDING.CAUGHTF), false)
val FIELDING_CAUGHTWK: Index = Internal.createIndex(DSL.name("Fielding_CaughtWk"), Fielding.FIELDING, arrayOf(Fielding.FIELDING.CAUGHTWK), false)
val FIELDING_DISMISSALS: Index = Internal.createIndex(DSL.name("Fielding_Dismissals"), Fielding.FIELDING, arrayOf(Fielding.FIELDING.DISMISSALS), false)
val FIELDING_MATCHTYPE: Index = Internal.createIndex(DSL.name("Fielding_MatchType"), Fielding.FIELDING, arrayOf(Fielding.FIELDING.MATCHTYPE), false)
val FIELDING_PLAYERID_MATCHID: Index = Internal.createIndex(DSL.name("Fielding_PlayerId_MatchId"), Fielding.FIELDING, arrayOf(Fielding.FIELDING.PLAYERID, Fielding.FIELDING.MATCHID), false)
val FIELDING_STUMPED: Index = Internal.createIndex(DSL.name("Fielding_Stumped"), Fielding.FIELDING, arrayOf(Fielding.FIELDING.STUMPED), false)
val GROUNDS_GROUNDID: Index = Internal.createIndex(DSL.name("Grounds_GroundId"), Grounds.GROUNDS, arrayOf(Grounds.GROUNDS.GROUNDID), false)
val GROUNDSMATCHTYPES_GROUNDID: Index = Internal.createIndex(DSL.name("GroundsMatchTypes_GroundId"), Groundsmatchtypes.GROUNDSMATCHTYPES, arrayOf(Groundsmatchtypes.GROUNDSMATCHTYPES.GROUNDID), false)
val INNINGS_OPPONENTSID: Index = Internal.createIndex(DSL.name("Innings_OpponentsId"), Innings.INNINGS, arrayOf(Innings.INNINGS.OPPONENTSID), false)
val INNINGS_TEAMID: Index = Internal.createIndex(DSL.name("Innings_TeamId"), Innings.INNINGS, arrayOf(Innings.INNINGS.TEAMID), false)
val MATCHES_MATCHTYPE: Index = Internal.createIndex(DSL.name("Matches_MatchType"), Matches.MATCHES, arrayOf(Matches.MATCHES.MATCHTYPE), false)
val MATCHREFEREES_DEBUTMATCHID: Index = Internal.createIndex(DSL.name("MatchReferees_DebutMatchId"), Matchreferees.MATCHREFEREES, arrayOf(Matchreferees.MATCHREFEREES.DEBUTMATCHID), false)
val MATCHREFEREES_UMPIRESID: Index = Internal.createIndex(DSL.name("MatchReferees_UmpiresId"), Matchreferees.MATCHREFEREES, arrayOf(Matchreferees.MATCHREFEREES.REFEREESID), false)
val MATCHSUBTYPE_MATCHTYPE: Index = Internal.createIndex(DSL.name("MatchSubType_MatchType"), Matchsubtype.MATCHSUBTYPE, arrayOf(Matchsubtype.MATCHSUBTYPE.MATCHTYPE), false)
val PARTNERSHIPS_INNINGS: Index = Internal.createIndex(DSL.name("Partnerships_Innings"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.INNINGS), false)
val PARTNERSHIPS_MATCHTYPE: Index = Internal.createIndex(DSL.name("Partnerships_MatchType"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.MATCHTYPE), false)
val PARTNERSHIPS_OPPONENTSID: Index = Internal.createIndex(DSL.name("Partnerships_OpponentsId"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.OPPONENTSID), false)
val PARTNERSHIPS_PLAYERIDS: Index = Internal.createIndex(DSL.name("Partnerships_PlayerIds"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.PLAYERIDS), false)
val PARTNERSHIPS_TEAMID: Index = Internal.createIndex(DSL.name("Partnerships_TeamId"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.TEAMID), false)
val PARTNERSHIPS_UNBROKEN: Index = Internal.createIndex(DSL.name("Partnerships_Unbroken"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.UNBROKEN), false)
val PARTNERSHIPS_WICKET: Index = Internal.createIndex(DSL.name("Partnerships_Wicket"), Partnerships.PARTNERSHIPS, arrayOf(Partnerships.PARTNERSHIPS.WICKET), false)
val PLAYERS_PLAYERID: Index = Internal.createIndex(DSL.name("Players_PlayerId"), Players.PLAYERS, arrayOf(Players.PLAYERS.PLAYERID), false)
val PLAYERSMATCHES__MATCHID: Index = Internal.createIndex(DSL.name("PlayersMatches__MatchId"), Playersmatches.PLAYERSMATCHES, arrayOf(Playersmatches.PLAYERSMATCHES.MATCHID), false)
val PLAYERSMATCHES_PLAYERID: Index = Internal.createIndex(DSL.name("PlayersMatches_PlayerId"), Playersmatches.PLAYERSMATCHES, arrayOf(Playersmatches.PLAYERSMATCHES.PLAYERID), false)
val PLAYERSMATCHES_PLAYERID_MATCHID: Index = Internal.createIndex(DSL.name("PlayersMatches_PlayerId_MatchId"), Playersmatches.PLAYERSMATCHES, arrayOf(Playersmatches.PLAYERSMATCHES.PLAYERID, Playersmatches.PLAYERSMATCHES.MATCHID), false)
val PLAYERSTEAMS_MATCHTYPE: Index = Internal.createIndex(DSL.name("PlayersTeams_MatchType"), Playersteams.PLAYERSTEAMS, arrayOf(Playersteams.PLAYERSTEAMS.MATCHTYPE), false)
val PLAYERSTEAMS_PLAYERID_TEAMID_MATCHTYPE: Index = Internal.createIndex(DSL.name("PlayersTeams_PlayerId_TeamId_MatchType"), Playersteams.PLAYERSTEAMS, arrayOf(Playersteams.PLAYERSTEAMS.PLAYERID, Playersteams.PLAYERSTEAMS.TEAMID, Playersteams.PLAYERSTEAMS.MATCHTYPE), false)
val RESERVEUMPIRES_DEBUTMATCHID: Index = Internal.createIndex(DSL.name("ReserveUmpires_DebutMatchId"), Umpires.UMPIRES, arrayOf(Umpires.UMPIRES.DEBUTMATCHID), false)
val RESERVEUMPIRES_UMPIRESID: Index = Internal.createIndex(DSL.name("ReserveUmpires_UmpiresId"), Reserveumpires.RESERVEUMPIRES, arrayOf(Reserveumpires.RESERVEUMPIRES.RESERVEUMPIRESID), false)
val SCORERS_DEBUTMATCHID: Index = Internal.createIndex(DSL.name("Scorers_DebutMatchId"), Scorers.SCORERS, arrayOf(Scorers.SCORERS.DEBUTMATCHID), false)
val SCORERS_UMPIRESID: Index = Internal.createIndex(DSL.name("Scorers_UmpiresId"), Scorers.SCORERS, arrayOf(Scorers.SCORERS.SCORERSID), false)
val TEAMS_TEAMID: Index = Internal.createIndex(DSL.name("teams_teamid"), Teams.TEAMS, arrayOf(Teams.TEAMS.TEAMID), false)
val TEAMSMATCHTYPES_MATCHTYPE: Index = Internal.createIndex(DSL.name("TeamsMatchTypes_matchtype"), Teamsmatchtypes.TEAMSMATCHTYPES, arrayOf(Teamsmatchtypes.TEAMSMATCHTYPES.MATCHTYPE), false)
val TEAMSMATCHTYPES_TEAMID: Index = Internal.createIndex(DSL.name("TeamsMatchTypes_teamid"), Teamsmatchtypes.TEAMSMATCHTYPES, arrayOf(Teamsmatchtypes.TEAMSMATCHTYPES.TEAMID), false)
val TVUMPIRES_DEBUTMATCHID: Index = Internal.createIndex(DSL.name("TvUmpires_DebutMatchId"), Tvumpires.TVUMPIRES, arrayOf(Tvumpires.TVUMPIRES.DEBUTMATCHID), false)
val TVUMPIRES_UMPIRESID: Index = Internal.createIndex(DSL.name("TvUmpires_UmpiresId"), Tvumpires.TVUMPIRES, arrayOf(Tvumpires.TVUMPIRES.TVUMPIRESID), false)
val UMPIRES_DEBUTMATCHID: Index = Internal.createIndex(DSL.name("Umpires_DebutMatchId"), Umpires.UMPIRES, arrayOf(Umpires.UMPIRES.DEBUTMATCHID), false)
val UMPIRES_UMPIRESID: Index = Internal.createIndex(DSL.name("Umpires_UmpiresId"), Umpires.UMPIRES, arrayOf(Umpires.UMPIRES.UMPIRESID), false)
