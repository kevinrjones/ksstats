/*
 * This file is generated by jOOQ.
 */
package com.ksstats.db.tables


import com.ksstats.db.DefaultSchema
import com.ksstats.db.indexes.PLAYERSTEAMS_MATCHTYPE
import com.ksstats.db.indexes.PLAYERSTEAMS_PLAYERID_TEAMID_MATCHTYPE
import com.ksstats.db.keys.PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_MATCHES
import com.ksstats.db.keys.PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_PLAYERS
import com.ksstats.db.keys.PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_TEAMS
import com.ksstats.db.tables.Matches.MatchesPath
import com.ksstats.db.tables.Players.PlayersPath
import com.ksstats.db.tables.Teams.TeamsPath
import com.ksstats.db.tables.records.PlayersteamsRecord

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Index
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Path
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Playersteams(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, PlayersteamsRecord>?,
    parentPath: InverseForeignKey<out Record, PlayersteamsRecord>?,
    aliased: Table<PlayersteamsRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<PlayersteamsRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>PlayersTeams</code>
         */
        val PLAYERSTEAMS: Playersteams = Playersteams()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<PlayersteamsRecord> = PlayersteamsRecord::class.java

    /**
     * The column <code>PlayersTeams.PlayerId</code>.
     */
    val PLAYERID: TableField<PlayersteamsRecord, Int?> = createField(DSL.name("PlayerId"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>PlayersTeams.TeamId</code>.
     */
    val TEAMID: TableField<PlayersteamsRecord, Int?> = createField(DSL.name("TeamId"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>PlayersTeams.MatchType</code>.
     */
    val MATCHTYPE: TableField<PlayersteamsRecord, String?> = createField(DSL.name("MatchType"), SQLDataType.VARCHAR(20).nullable(false), this, "")

    /**
     * The column <code>PlayersTeams.DebutId</code>.
     */
    val DEBUTID: TableField<PlayersteamsRecord, Int?> = createField(DSL.name("DebutId"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>PlayersTeams.Debut</code>.
     */
    val DEBUT: TableField<PlayersteamsRecord, Long?> = createField(DSL.name("Debut"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>PlayersTeams.ActiveUntil</code>.
     */
    val ACTIVEUNTIL: TableField<PlayersteamsRecord, Long?> = createField(DSL.name("ActiveUntil"), SQLDataType.BIGINT.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<PlayersteamsRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<PlayersteamsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<PlayersteamsRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>PlayersTeams</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>PlayersTeams</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>PlayersTeams</code> table reference
     */
    constructor(): this(DSL.name("PlayersTeams"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, PlayersteamsRecord>?, parentPath: InverseForeignKey<out Record, PlayersteamsRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, PLAYERSTEAMS, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class PlayersteamsPath : Playersteams, Path<PlayersteamsRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, PlayersteamsRecord>?, parentPath: InverseForeignKey<out Record, PlayersteamsRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<PlayersteamsRecord>): super(alias, aliased)
        override fun `as`(alias: String): PlayersteamsPath = PlayersteamsPath(DSL.name(alias), this)
        override fun `as`(alias: Name): PlayersteamsPath = PlayersteamsPath(alias, this)
        override fun `as`(alias: Table<*>): PlayersteamsPath = PlayersteamsPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getIndexes(): List<Index> = listOf(PLAYERSTEAMS_MATCHTYPE, PLAYERSTEAMS_PLAYERID_TEAMID_MATCHTYPE)
    override fun getReferences(): List<ForeignKey<PlayersteamsRecord, *>> = listOf(PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_PLAYERS, PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_TEAMS, PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_MATCHES)

    private lateinit var _players: PlayersPath

    /**
     * Get the implicit join path to the <code>Players</code> table.
     */
    fun players(): PlayersPath {
        if (!this::_players.isInitialized)
            _players = PlayersPath(this, PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_PLAYERS, null)

        return _players;
    }

    val players: PlayersPath
        get(): PlayersPath = players()

    private lateinit var _teams: TeamsPath

    /**
     * Get the implicit join path to the <code>Teams</code> table.
     */
    fun teams(): TeamsPath {
        if (!this::_teams.isInitialized)
            _teams = TeamsPath(this, PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_TEAMS, null)

        return _teams;
    }

    val teams: TeamsPath
        get(): TeamsPath = teams()

    private lateinit var _matches: MatchesPath

    /**
     * Get the implicit join path to the <code>Matches</code> table.
     */
    fun matches(): MatchesPath {
        if (!this::_matches.isInitialized)
            _matches = MatchesPath(this, PLAYERSTEAMS__FK_PLAYERSTEAMS_PK_MATCHES, null)

        return _matches;
    }

    val matches: MatchesPath
        get(): MatchesPath = matches()
    override fun `as`(alias: String): Playersteams = Playersteams(DSL.name(alias), this)
    override fun `as`(alias: Name): Playersteams = Playersteams(alias, this)
    override fun `as`(alias: Table<*>): Playersteams = Playersteams(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Playersteams = Playersteams(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Playersteams = Playersteams(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Playersteams = Playersteams(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Playersteams = Playersteams(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Playersteams = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Playersteams = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Playersteams = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Playersteams = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Playersteams = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Playersteams = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Playersteams = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Playersteams = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Playersteams = where(DSL.notExists(select))
}
