/*
 * This file is generated by jOOQ.
 */
package com.ksstats.db.tables


import com.ksstats.db.DefaultSchema
import com.ksstats.db.keys.UMPIRESMATCHES__FK_UMPIRESMATCHES_PK_MATCHES
import com.ksstats.db.keys.UMPIRESMATCHES__FK_UMPIRESMATCHES_PK_UMPIRES
import com.ksstats.db.tables.Matches.MatchesPath
import com.ksstats.db.tables.Umpires.UmpiresPath
import com.ksstats.db.tables.records.UmpiresmatchesRecord

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
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
open class Umpiresmatches(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, UmpiresmatchesRecord>?,
    parentPath: InverseForeignKey<out Record, UmpiresmatchesRecord>?,
    aliased: Table<UmpiresmatchesRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<UmpiresmatchesRecord>(
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
         * The reference instance of <code>UmpiresMatches</code>
         */
        val UMPIRESMATCHES: Umpiresmatches = Umpiresmatches()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<UmpiresmatchesRecord> = UmpiresmatchesRecord::class.java

    /**
     * The column <code>UmpiresMatches.PersonId</code>.
     */
    val PERSONID: TableField<UmpiresmatchesRecord, Int?> = createField(DSL.name("PersonId"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>UmpiresMatches.MatchId</code>.
     */
    val MATCHID: TableField<UmpiresmatchesRecord, Int?> = createField(DSL.name("MatchId"), SQLDataType.INTEGER.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<UmpiresmatchesRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<UmpiresmatchesRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<UmpiresmatchesRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>UmpiresMatches</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>UmpiresMatches</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>UmpiresMatches</code> table reference
     */
    constructor(): this(DSL.name("UmpiresMatches"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, UmpiresmatchesRecord>?, parentPath: InverseForeignKey<out Record, UmpiresmatchesRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, UMPIRESMATCHES, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class UmpiresmatchesPath : Umpiresmatches, Path<UmpiresmatchesRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, UmpiresmatchesRecord>?, parentPath: InverseForeignKey<out Record, UmpiresmatchesRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<UmpiresmatchesRecord>): super(alias, aliased)
        override fun `as`(alias: String): UmpiresmatchesPath = UmpiresmatchesPath(DSL.name(alias), this)
        override fun `as`(alias: Name): UmpiresmatchesPath = UmpiresmatchesPath(alias, this)
        override fun `as`(alias: Table<*>): UmpiresmatchesPath = UmpiresmatchesPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getReferences(): List<ForeignKey<UmpiresmatchesRecord, *>> = listOf(UMPIRESMATCHES__FK_UMPIRESMATCHES_PK_UMPIRES, UMPIRESMATCHES__FK_UMPIRESMATCHES_PK_MATCHES)

    private lateinit var _umpires: UmpiresPath

    /**
     * Get the implicit join path to the <code>Umpires</code> table.
     */
    fun umpires(): UmpiresPath {
        if (!this::_umpires.isInitialized)
            _umpires = UmpiresPath(this, UMPIRESMATCHES__FK_UMPIRESMATCHES_PK_UMPIRES, null)

        return _umpires;
    }

    val umpires: UmpiresPath
        get(): UmpiresPath = umpires()

    private lateinit var _matches: MatchesPath

    /**
     * Get the implicit join path to the <code>Matches</code> table.
     */
    fun matches(): MatchesPath {
        if (!this::_matches.isInitialized)
            _matches = MatchesPath(this, UMPIRESMATCHES__FK_UMPIRESMATCHES_PK_MATCHES, null)

        return _matches;
    }

    val matches: MatchesPath
        get(): MatchesPath = matches()
    override fun `as`(alias: String): Umpiresmatches = Umpiresmatches(DSL.name(alias), this)
    override fun `as`(alias: Name): Umpiresmatches = Umpiresmatches(alias, this)
    override fun `as`(alias: Table<*>): Umpiresmatches = Umpiresmatches(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Umpiresmatches = Umpiresmatches(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Umpiresmatches = Umpiresmatches(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Umpiresmatches = Umpiresmatches(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Umpiresmatches = Umpiresmatches(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Umpiresmatches = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Umpiresmatches = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Umpiresmatches = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Umpiresmatches = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Umpiresmatches = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Umpiresmatches = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Umpiresmatches = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Umpiresmatches = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Umpiresmatches = where(DSL.notExists(select))
}
