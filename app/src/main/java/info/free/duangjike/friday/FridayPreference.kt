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
    private const val TEXT_COLOR = "text_color"
    private const val LANG = "language"
    private const val CN_FONT_TYPE = "cn_font_type"
    private const val EN_FONT_TYPE = "en_font_type"
    private const val COLOR_NAME = "color_name"

    fun setBubbleColor(color: Int) {
        setIntValue(FRIDAY_PREF, BUBBLE_COLOR, color)
    }
    fun setBgColor(color: Int) {
        setIntValue(FRIDAY_PREF, BG_COLOR, color)
    }
    fun setTextColor(color: Int) {
        setIntValue(FRIDAY_PREF, TEXT_COLOR, color)
    }

    fun setLang(lang: Int) {
        setIntValue(FRIDAY_PREF, LANG, lang)
    }
    fun setCnFontType(font: Int) {
        setIntValue(FRIDAY_PREF, CN_FONT_TYPE, font)
    }
    fun setEnFontType(font: String) {
        setStringValue(FRIDAY_PREF, EN_FONT_TYPE, font)
    }
    fun getBubbleColor(): Int {
        return getIntValue(FRIDAY_PREF, BUBBLE_COLOR)
    }
    fun getBgColor(): Int {
        return getIntValue(FRIDAY_PREF, BG_COLOR)
    }
    fun getTextColor(): Int {
        return getIntValue(FRIDAY_PREF, TEXT_COLOR)
    }
    fun getLang(): Int {
        return getIntValue(FRIDAY_PREF, LANG)
    }
    fun getCnFontType(): Int {
        return getIntValue(FRIDAY_PREF, CN_FONT_TYPE)
    }
    fun getEnFontType(): String {
        return getStringValue(FRIDAY_PREF, EN_FONT_TYPE)
    }
    fun getColorName(): String {
        return getStringValue(FRIDAY_PREF, COLOR_NAME)
    }
    fun setColorName(name: String) {
        setStringValue(FRIDAY_PREF, COLOR_NAME, name)
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
