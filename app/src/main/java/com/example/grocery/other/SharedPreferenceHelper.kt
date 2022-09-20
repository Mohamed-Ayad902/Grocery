package com.example.grocery.other

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceHelper
@Inject
constructor(
    private val sharedPref: SharedPreferences,
) {

    private fun getBooleanValue(): Boolean {
        return sharedPref.getBoolean(Constants.KEY_IS_FIRST_TIME, true)
    }

    private fun changeBooleanValueToFalse() {
        val editor = sharedPref.edit()
        editor.putBoolean(Constants.KEY_IS_FIRST_TIME, false).apply()
    }

    fun checkIfFirstAppOpened(): Boolean {
        val isFirstLogApp = getBooleanValue()
        if (isFirstLogApp)
            changeBooleanValueToFalse()
        return isFirstLogApp
    }
}