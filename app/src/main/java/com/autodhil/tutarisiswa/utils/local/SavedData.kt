package com.pajaga.utils.local

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

object SavedData {
    private lateinit var sharedPref: SharedPreferences
//    private val gson = Gson()

    fun init(activity: FragmentActivity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    fun setString(key: String, params: String) {
        with (sharedPref.edit()) {
            putString(key, params)
            commit()
        }
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, "")
    }

    fun setInt(key: String, params: Int) {
        with (sharedPref.edit()) {
            putInt(key, params)
            commit()
        }
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, 0)
    }

    fun setFloat(key: String, params: Float) {
        with (sharedPref.edit()) {
            putFloat(key, params)
            commit()
        }
    }

    fun getFloat(key: String): Float {
        return sharedPref.getFloat(key, 0F)
    }

    fun setBoolean(key: String, params: Boolean) {
        with (sharedPref.edit()) {
            putBoolean(key, params)
            commit()
        }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

//    fun setObject(params: ExamplesModel) {
//        val json = gson.toJson(params)
//        with (sharedPref.edit()) {
//            putString(Constant.examplesKeySavedDataObject, json)
//            commit()
//        }
//
//    }
//
//    fun getObject(): ExamplesModel? {
//        val json: String? = sharedPref.getString(Constant.examplesKeySavedDataObject, "")
//        return gson.fromJson(json, ExamplesModel::class.java)
//    }
}