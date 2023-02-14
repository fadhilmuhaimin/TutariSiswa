package com.autodhil.tutarisiswa.utils.local

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.autodhil.tutarisiswa.ui.auth.AutentikasiActivity

class SessionManager(internal var _context: Context) {

    internal var pref: SharedPreferences

    // Editor for Shared preferences
    internal var editor: SharedPreferences.Editor

    // Shared pref mode
    internal var PRIVATE_MODE = 0

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    val username: String?
        get() = pref.getString(KEY_USERNAME, null)

    val usernameSiswa: String?
        get() = pref.getString(KEY_USERNAME_SISWA, null)

    val name: String?
        get() = pref.getString(KEY_NAME, null)

    val nameSiswa: String?
        get() = pref.getString(KEY_NAME_SISWA, null)

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)

    fun createLoginSession() {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)

        // commit changes
        editor.commit()
    }

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()

        // After logout redirect user to Loing ActivityLog
        val i = Intent(_context, AutentikasiActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // Add new Flag to start new ActivityLog
//        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring Login ActivityLog
        _context.startActivity(i)

    }


    fun setUsername(username: String) {
        editor.putString(KEY_USERNAME, username)
        editor.commit()
    }

    fun setUsernameSiswa(username: String?) {
        editor.putString(KEY_USERNAME_SISWA, username)
        editor.commit()
    }
    fun setName(username: String) {
        editor.putString(KEY_NAME, username)
        editor.commit()
    }

    fun setNameSiswa(username: String) {
        editor.putString(KEY_NAME_SISWA, username)
        editor.commit()
    }

    companion object{
        private val PREF_NAME = "Tutari_Pref"
        // All Shared Preferences Keys
        private val IS_LOGIN = "IsLoggedIn"
        val KEY_USERNAME        = "username"
        val KEY_USERNAME_SISWA        = "usernameSiswa"
        val KEY_NAME        = "name"
        val KEY_NAME_SISWA        = "nameSiswa"

        private var instance: SessionManager? = null

        fun with(context: Context): SessionManager {

            if (instance == null) {
                instance = SessionManager(context)
            }
            return instance as SessionManager
        }
    }
}