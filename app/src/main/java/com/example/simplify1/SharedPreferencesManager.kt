package com.example.simplify1

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LOCATION_NAME = "location_name"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Store the first name in SharedPreferences
    fun storeFirstName(context: Context, firstName: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.apply()
    }

    // Get the first name from SharedPreferences
    fun getFirstName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_FIRST_NAME, null)
    }

    // Store the location name in SharedPreferences
    fun storeLocationName(context: Context, locationName: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_LOCATION_NAME, locationName)
        editor.apply()
    }

    // Get the location name from SharedPreferences
    fun getLocationName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_LOCATION_NAME, null)
    }

    // Clear all stored data
    fun clearData(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}
