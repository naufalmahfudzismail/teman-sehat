package id.depok.posyandu.data.preferences

import android.content.Context
import android.preference.PreferenceManager

@Suppress("DEPRECATION")
object AppPreference {

    val PREF_KEY_COOKIES = "cookies"
    val PREF_KEY_USERNAME = "username"
    val PREF_KEY_PASSWORD = "password"


    fun saveString(context: Context, key: String, value: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)

        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        return pref.getString(key, "").toString()
    }

    fun getStringContext(context: Context, key: String): String {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        return pref.getString(key, "").toString()
    }

    fun saveStringSet(context: Context, key: String, value: Set<String>) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

        val editor = pref.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getStringSet(context: Context, key: String): MutableSet<String>?{
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return pref.getStringSet(key, HashSet())
    }

    fun saveLong(context: Context, key: String, value: Long) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(context: Context, key: String): Long {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return pref.getLong(key, 0)
    }

    fun saveInt(context: Context, key: String, value: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(context: Context, key: String): Int {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return pref.getInt(key, -1)
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String): Boolean {
        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return  pref.getBoolean(key, false)
    }
}