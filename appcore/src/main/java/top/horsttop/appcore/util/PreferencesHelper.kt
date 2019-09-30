package top.horsttop.appcore.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Created by horsttop on 16/1/2.
 */
class PreferencesHelper @Inject constructor(var context: Application) {


    private val mPref: SharedPreferences

    init {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 轻量存String
     * @param key
     * @param value
     */
    fun saveConfig(key: String, value: String?) {
        mPref.edit().putString(key, value).apply()
    }

    /**
     * 轻量存int
     * @param key
     * @param value
     */
    fun saveConfig(key: String, value: Int) {
        mPref.edit().putInt(key, value).apply()
    }


    /**
     * 轻量存long
     * @param key
     * @param value
     */
    fun saveConfig(key: String, value: Long) {
        mPref.edit().putLong(key, value).apply()
    }

    /**
     * 取String,默认为""
     * @param key
     * @return
     */
    fun getStringConfig(key: String): String {
        return mPref.getString(key, "")
    }

    /**
     * 取int,默认为0
     * @param key
     * @return
     */
    fun getIntConfig(key: String): Int {
        return mPref.getInt(key, 0)
    }

    /**
     * 取long,默认为0
     * @param key
     * @return
     */
    fun getLongConfig(key: String): Long {
        return mPref.getLong(key, 0)
    }

    /**
     * 清除所有项
     */
    fun clear() {
        mPref.edit().clear().apply()
    }

    companion object {
        val PREF_FILE_NAME = "horsttop"
    }
}
