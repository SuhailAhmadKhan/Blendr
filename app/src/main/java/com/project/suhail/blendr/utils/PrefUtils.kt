package com.project.suhail.blendr.utils

import android.content.Context
import android.content.SharedPreferences
import com.project.suhail.blendr.models.User
import com.google.gson.Gson

object PrefUtils {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setUser(user: User) {
        sharedPreferences.edit().putString(Constants.USER, Gson().toJson(user)).apply()
    }

    fun getUser(): User? {
        return Gson().fromJson(sharedPreferences.getString(Constants.USER, ""), User::class.java)
    }

    fun set(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun get(key: String, defaultValue: String) {
        sharedPreferences.getString(key, defaultValue)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

}