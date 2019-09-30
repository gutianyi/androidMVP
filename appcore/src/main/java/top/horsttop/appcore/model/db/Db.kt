package top.horsttop.appcore.model.db

import android.database.Cursor

/**
 * Created by horsttop on 2018/4/16.
 */

object Db {

    init {
        throw AssertionError("No instances.")
    }

    val BOOLEAN_FALSE   = 0
    val BOOLEAN_TRUE    = 1

    fun getString(cursor: Cursor, columnName: String): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName))
    }

    fun getBoolean(cursor: Cursor, columnName: String): Boolean {
        return getInt(cursor, columnName) == BOOLEAN_TRUE
    }

    fun getLong(cursor: Cursor, columnName: String): Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName))
    }

    fun getInt(cursor: Cursor, columnName: String): Int {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName))
    }

}