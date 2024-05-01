package com.ksstats.shared

import org.jooq.SQLDialect

data class DatabaseConnection(val userName: String? = null, val password: String? = null, val connectionString: String, val dialect: SQLDialect)
