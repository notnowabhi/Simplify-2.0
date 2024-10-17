package com.example.simplify1

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_FIRST_NAME = "first_name"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun storeFirstName(context: Context, firstName: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.apply()
    }

    fun getFirstName(context: Context): String? {
        return getPreferences(context).getString(KEY_FIRST_NAME, null)
    }
}
