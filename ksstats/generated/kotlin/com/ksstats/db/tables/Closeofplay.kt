/*
 * This file is generated by jOOQ.
 */
package com.ksstats.db.tables


import com.ksstats.db.DefaultSchema
import com.ksstats.db.keys.CLOSEOFPLAY__FK_CLOSEOFPLAY_PK_MATCHES
import com.ksstats.db.keys.CLOSEOFPLAY__PK_CLOSEOFPLAY
import com.ksstats.db.tables.Matches.MatchesPath
import com.ksstats.db.tables.records.CloseofplayRecord

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
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
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Closeofplay(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, CloseofplayRecord>?,
    parentPath: InverseForeignKey<out Record, CloseofplayRecord>?,
    aliased: Table<CloseofplayRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<CloseofplayRecord>(
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
         * The reference instance of <code>CloseOfPlay</code>
         */
        val CLOSEOFPLAY: Closeofplay = Closeofplay()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<CloseofplayRecord> = CloseofplayRecord::class.java

    /**
     * The column <code>CloseOfPlay.Id</code>.
     */
    val ID: TableField<CloseofplayRecord, Int?> = createField(DSL.name("Id"), SQLDataType.INTEGER.identity(true), this, "")

    /**
     * The column <code>CloseOfPlay.MatchId</code>.
     */
    val MATCHID: TableField<CloseofplayRecord, Int?> = createField(DSL.name("MatchId"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>CloseOfPlay.Day</code>.
     */
    val DAY: TableField<CloseofplayRecord, Int?> = createField(DSL.name("Day"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>CloseOfPlay.Note</code>.
     */
    val NOTE: TableField<CloseofplayRecord, String?> = createField(DSL.name("Note"), SQLDataType.VARCHAR(250).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<CloseofplayRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<CloseofplayRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<CloseofplayRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>CloseOfPlay</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>CloseOfPlay</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>CloseOfPlay</code> table reference
     */
    constructor(): this(DSL.name("CloseOfPlay"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, CloseofplayRecord>?, parentPath: InverseForeignKey<out Record, CloseofplayRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, CLOSEOFPLAY, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class CloseofplayPath : Closeofplay, Path<CloseofplayRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, CloseofplayRecord>?, parentPath: InverseForeignKey<out Record, CloseofplayRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<CloseofplayRecord>): super(alias, aliased)
        override fun `as`(alias: String): CloseofplayPath = CloseofplayPath(DSL.name(alias), this)
        override fun `as`(alias: Name): CloseofplayPath = CloseofplayPath(alias, this)
        override fun `as`(alias: Table<*>): CloseofplayPath = CloseofplayPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getIdentity(): Identity<CloseofplayRecord, Int?> = super.getIdentity() as Identity<CloseofplayRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<CloseofplayRecord> = CLOSEOFPLAY__PK_CLOSEOFPLAY
    override fun getReferences(): List<ForeignKey<CloseofplayRecord, *>> = listOf(CLOSEOFPLAY__FK_CLOSEOFPLAY_PK_MATCHES)

    private lateinit var _matches: MatchesPath

    /**
     * Get the implicit join path to the <code>Matches</code> table.
     */
    fun matches(): MatchesPath {
        if (!this::_matches.isInitialized)
            _matches = MatchesPath(this, CLOSEOFPLAY__FK_CLOSEOFPLAY_PK_MATCHES, null)

        return _matches;
    }

    val matches: MatchesPath
        get(): MatchesPath = matches()
    override fun `as`(alias: String): Closeofplay = Closeofplay(DSL.name(alias), this)
    override fun `as`(alias: Name): Closeofplay = Closeofplay(alias, this)
    override fun `as`(alias: Table<*>): Closeofplay = Closeofplay(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Closeofplay = Closeofplay(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Closeofplay = Closeofplay(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Closeofplay = Closeofplay(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Closeofplay = Closeofplay(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Closeofplay = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Closeofplay = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Closeofplay = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Closeofplay = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Closeofplay = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Closeofplay = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Closeofplay = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Closeofplay = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Closeofplay = where(DSL.notExists(select))
}
