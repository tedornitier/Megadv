package app.alessandrotedd.megadv

import android.app.Activity
import android.content.Intent
import app.alessandrotedd.megadv.activities.LoadingActivity
import app.alessandrotedd.megadv.activities.LoginActivity
import app.alessandrotedd.megadv.api.API

class Session {
    companion object {
        /**
         * Session key, used to authenticate the user. If it's empty the user is logged out
         */
        var key = ""

        /**
         * Loads the session key from memory or file
         * @return a previously stored sessionKey
         */
        fun getSessionKey(activity: Activity): String {
            // check memory
            if (key != "")
                return key

            // check shared preferences
            val sharedPref = activity.getSharedPreferences("settings", 0)
            key = sharedPref.getString("sessionKey", "") ?: ""
            return key
        }

        /**
         * Logs out the user by removing the session key, both in memory and in file
         */
        fun logout(activity: LoadingActivity) {
            // remove session key
            val sharedPref = activity.getSharedPreferences("settings", 0)
            val editor = sharedPref.edit()
            editor.remove("sessionKey")
            editor.apply()
            key = ""

            // remove JSON cache
            val cache = activity.getSharedPreferences("cache", 0)
            val cacheEditor = cache.edit()
            cacheEditor.remove(API.Type.REPORT.cacheName)
            cacheEditor.remove(API.Type.ADS.cacheName)
            cacheEditor.remove(API.Type.FACEBOOK.cacheName)
            cacheEditor.apply()

            // redirect to LoginActivity
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}