package br.com.whatadice.data

import android.content.Context

class SessionManager(context: Context){
    private val prefs = context.getSharedPreferences("apps_prefs", Context.MODE_PRIVATE)

    private companion object{
        const val IS_LOGGED_IN = "is_logged_in"
        const val EMAIL = "email"
    }

    fun login(email:String){
        prefs.edit()
            .putBoolean(IS_LOGGED_IN, true)
            .putString(EMAIL, email)
            .apply()
    }

    fun logout(){
        prefs.edit()
            .putBoolean(IS_LOGGED_IN, false)
            .remove(EMAIL)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(IS_LOGGED_IN, false)

    fun currentEmail(): String? = prefs.getString(EMAIL, null)
}