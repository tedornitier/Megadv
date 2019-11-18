package app.alessandrotedd.megadv.api

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.Session
import app.alessandrotedd.megadv.Session.Companion.getSessionKey
import app.alessandrotedd.megadv.activities.DataLoadingActivity
import app.alessandrotedd.megadv.activities.LoadingActivity
import app.alessandrotedd.megadv.activities.LoginActivity
import app.alessandrotedd.megadv.activities.ReportActivity
import app.alessandrotedd.megadv.api.API.Type.LOGIN
import app.alessandrotedd.megadv.api.dataModel.AdsModel
import app.alessandrotedd.megadv.api.dataModel.FacebookModel
import app.alessandrotedd.megadv.api.dataModel.LoginModel
import app.alessandrotedd.megadv.api.dataModel.ReportModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit
import javax.security.auth.callback.Callback


class API {

    companion object {
        private const val CONNECTION_TIMEOUT_SECONDS = 20L
        private const val API_PATH = "https://www.megadv.it/API"
        private const val VERSION = "v1"

        private val client = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

        /**
         * Provides a shortcut for the API path
         * @param request The API request
         */
        private fun getPath(request: String): String {
            return "$API_PATH/$VERSION/$request"
        }

        /**
         * Handles a connection error by communicating the error to the user, giving an option to retry
         */
        private fun connectionErrorHandler(
            e: Exception,
            activity: LoadingActivity,
            url: String,
            type: Type
        ) {
            // log error
            if (e.message != null)
                Log.e(url, e.message!!)

            // show no connection error with a "retry" option
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                activity.getString(R.string.noConnectionErrorMessage),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(activity.getString(R.string.retry)) {
                if (type == LOGIN)
                    (activity as LoginActivity).loginButton.callOnClick() // retry login
                else
                    getData(type, activity as DataLoadingActivity) // retry loading data
            }.show()

            // stop the loading animation
            activity.setLoading(false)
        }


        /**
         * Gets data from the server and returns it in a callback function in the activity provided that handles the data
         * @see DataLoadingActivity.dataReady
         * @param type the request type
         * @param activity the activity that contains the callback function
         */
        fun getData(type: Type, activity: DataLoadingActivity) {
            // start loading animation
            activity.setLoading(true)

            // check cache in order to load data offline before the actual server response
            checkCache(type, activity)

            // set request with URL, providing sessionKey to authenticate the user
            val url = getPath(type.cacheName) + "?sessionKey=" + getSessionKey(activity)
            val request = Request.Builder().url(url).build()

            // send the request to server
            client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // connection failed, handle error
                    connectionErrorHandler(e, activity, url, type)
                }

                override fun onResponse(call: Call, response: Response) {
                    // connection successful, get content response
                    val content = response.body()?.string() ?: return onFailure(call, IOException())

                    activity.runOnUiThread {
                        try {
                            // convert JSON response to list and sent it to the activity that will handle it
                            @Suppress("UNCHECKED_CAST")
                            activity.dataReady(
                                ArrayList((Gson().fromJson(
                                    content,
                                    type.modelArrayClass
                                ) as Array<Any>).toList())
                            )
                            // cache JSON response
                            cacheJSON(type, content, activity)

                        } catch (e: Exception) {
                            // stop loading animation
                            activity.setLoading(false)
                            // logout user
                            Session.logout(activity)
                        }
                    }
                }
            })
        }

        /**
         * Cache JSON response asynchronously, in order to load it offline the next time
         * @param type The request type
         * @param content the JSON response
         */
        private fun cacheJSON(type: Type, content: String, context: Context) {
            GlobalScope.launch {
                val editor = context.getSharedPreferences("cache", 0).edit()
                editor.putString(type.cacheName, content)
                editor.apply()
            }
        }

        /**
         * @return true if there's a Session.key stored, false otherwise
         */
        fun asyncCheckIfUserIsLoggedIn(activity: LoginActivity) {
            // get stored session key
            Session.key = getSessionKey(activity)
            // if there's no session key
            if (Session.key.isEmpty())
                return // exit check

            // it's already logged in, go to ReportActivity
            val intent = Intent(activity, ReportActivity::class.java)
            activity.startActivity(intent)
            return
        }

        /**
         * Checks JSON cache in order to load data offline before an actual server response
         * @param type the request type
         * @param activity the activity that will load the data from cache
         */
        private fun checkCache(type: Type, activity: DataLoadingActivity) {
            // load cache in a JSON format based on the request type
            val sharedPref = activity.getSharedPreferences("cache", 0)
            val content = sharedPref.getString(type.cacheName, "")
            // if there's no cache stored exit the function
            if (content == "")
                return
            // otherwise, convert JSON to a list which will be handled by the activity
            @Suppress("UNCHECKED_CAST")
            activity.dataReady(
                ArrayList((Gson().fromJson(content, type.modelArrayClass) as Array<Any>).toList())
            )
        }

        /**
         * Calculates and returns the MD5 hash of a string
         */
        private fun String.toMD5(): String {
            val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        /**
         * Send a login request to the server in order to authenticate the user
         * @param username The user's email
         * @param password The user's password
         */
        fun login(username: String, password: String, activity: LoginActivity) {
            // start loading animation
            activity.setLoading(true)

            // set request with URL, providing username and password to get a session key
            val url = getPath("login") + "?user=$username&password=" + password.toMD5()
            val request = Request.Builder().url(url).build()

            // send the request to the server
            client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // connection failed, handle error
                    connectionErrorHandler(e, activity, url, LOGIN)
                }

                override fun onResponse(call: Call, response: Response) {
                    // stop loading animation
                    activity.setLoading(false)

                    // connection successful, get content response
                    val content = response.body()?.string() ?:
                        return onFailure(call, IOException("Empty content at url $url"))
                    // convert JSON response to login data
                    val loginResponse = Gson().fromJson(content, LoginModel::class.java)

                    activity.runOnUiThread {
                        if (loginResponse.loggedIn) {
                            // show toast on first login of the day
                            if (loginResponse.creditsEarned > 0)
                                Toast.makeText(
                                    activity,
                                    String.format(Locale.getDefault(), activity.getString(R.string.firstLoginMessage), loginResponse.creditsEarned),
                                    Toast.LENGTH_LONG
                                ).show()
                            // store the session key
                            storeSessionKey(activity, loginResponse.sessionKey)

                            // redirect to report activity
                            val intent = Intent(activity, ReportActivity::class.java)
                            activity.startActivity(intent)
                        }
                        // if the credentials supplied are invalid, show error
                        else
                            Toast.makeText(activity, activity.getString(R.string.loginError), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        /**
         * Store the user session key used to authenticate in file asynchronously via shared preferences and in memory
         */
        private fun storeSessionKey(context: Context, sessionKey: String) {
            // store in file
            GlobalScope.launch {
                val editor = context.getSharedPreferences("settings", 0).edit()
                editor.putString("sessionKey", sessionKey)
                editor.apply()
            }
            // store in memory
            Session.key = sessionKey
        }
    }

    enum class Type(val cacheName: String, val modelArrayClass: Class<*>) {
        REPORT("report", Array<ReportModel>::class.java),
        ADS("ads", Array<AdsModel>::class.java),
        FACEBOOK("facebook", Array<FacebookModel>::class.java),
        LOGIN("login", Array<LoginModel>::class.java)
    }

}