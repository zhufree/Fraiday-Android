package info.free.duangjike.friday

import android.content.Context
import android.content.SharedPreferences
import info.free.duangjike.DuangApplication

/**
 * Created by zhufree on 2018/12/14.
 *
 */

object FridayPreference {
    private const val FRIDAY_PREF = "friday"
    private const val BUBBLE_COLOR = "bubble_color"
    private const val BG_COLOR = "bg_color"
    private const val FONT_TYPE = "font_type"

    fun setBubbleColor(color: Int) {
        setIntValue(FRIDAY_PREF, BUBBLE_COLOR, color)
    }
    fun setBgColor(color: Int) {
        setIntValue(FRIDAY_PREF, BG_COLOR, color)
    }
    fun setFontType(font: Int) {
        setIntValue(FRIDAY_PREF, FONT_TYPE, font)
    }
    fun getBubbleColor(): Int {
        return getIntValue(FRIDAY_PREF, BUBBLE_COLOR)
    }
    fun getBgColor(): Int {
        return getIntValue(FRIDAY_PREF, BG_COLOR)
    }
    fun getFontType(): Int {
        return getIntValue(FRIDAY_PREF, FONT_TYPE)
    }
    /**
     * 工具方法
     */
    private fun getPrivateSharedPreference(name: String): SharedPreferences? {
        return DuangApplication.context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun getBooleanValue(spName: String, key: String): Boolean {
        val sp = getPrivateSharedPreference(spName)
        return sp?.getBoolean(key, false)?:false
    }
    private fun setBooleanValue(spName: String, key: String, value: Boolean) {
        val sp = getPrivateSharedPreference(spName)
        sp?.edit()?.putBoolean(key, value)?.apply()
    }
    private fun getIntValue(spName: String, key: String): Int {
        val sp = getPrivateSharedPreference(spName)
        return sp?.getInt(key, 0)?:0
    }

    private fun setIntValue(spName: String, key: String, value: Int) {
        val sp = getPrivateSharedPreference(spName)
        sp?.edit()?.putInt(key, value)?.apply()
    }

    private fun getLongValue(spName: String, key: String): Long {
        val sp = getPrivateSharedPreference(spName)
        return sp?.getLong(key, 0L)?:0L
    }

    private fun setLongValue(spName: String, key: String, value: Long) {
        val sp = getPrivateSharedPreference(spName)
        sp?.edit()?.putLong(key, value)?.apply()
    }
    private fun getStringValue(spName: String, key: String): String {
        val sp = getPrivateSharedPreference(spName)
        return sp?.getString(key, "")?:""
    }

    private fun setStringValue(spName: String, key: String, value: String) {
        val sp = getPrivateSharedPreference(spName)
        sp?.edit()?.putString(key, value)?.apply()
    }
}
