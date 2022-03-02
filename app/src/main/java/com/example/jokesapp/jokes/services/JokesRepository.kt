package com.example.jokesapp.jokes.services

import android.content.Context
import android.content.SharedPreferences
import com.example.jokesapp.jokes.model.JokesResponseModel
import com.example.jokesapp.network.NetworkManager
import com.example.jokesapp.network.TaskCallback
import com.example.jokesapp.utils.Constants
import com.example.jokesapp.utils.Result
import com.example.jokesapp.utils.awaitResult
import com.example.jokesapp.utils.isNetworkAvailable
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class JokesRepository {
    val completableJob = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.IO + completableJob)
    private val foregroundScope = CoroutineScope(Dispatchers.Main)

    private val jokesServices : JokesServices by lazy {
        NetworkManager.baseUrl(Constants.BASE_URL).serviceClass(JokesServices::class.java).build()
    }

    fun getJokesList(context: Context, page : Int, pageSize : Int, searchedJokeName : String, callback: TaskCallback<JokesResponseModel>) {
        when {
            isNetworkAvailable(context) -> {
                backgroundScope.launch {
                    when (val result : Result<JokesResponseModel> = jokesServices.getJokes(page, pageSize, searchedJokeName).awaitResult()) {

                        /**Successful Network Request*/

                        /**Successful Network Request*/
                        is Result.Ok -> foregroundScope.launch {
                            updateJokesInStorage(context, result.value)
                            callback.onComplete(result.value)
                        }


                        /**Error on the Network Result*/

                        /**Error on the Network Result*/
                        is Result.Error -> foregroundScope.launch { callback.onException(result.exception) }


                        /**Exception on the Network Result*/

                        /**Exception on the Network Result*/
                        is Result.Exception -> foregroundScope.launch { callback.onException(result.exception) }
                    }
                }
            }
            getJokesFromStorage(context) != null -> {
                callback.onComplete(getJokesFromStorage(context)!!)
            }
            else -> {
                callback.onException(SocketTimeoutException())
            }
        }
    }


    private fun updateJokesInStorage(context: Context, jokesResponseModel: JokesResponseModel) {
        val sharedPreference: SharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERNCE_NAME, Context.MODE_PRIVATE)
        sharedPreference.edit().putString(Constants.JOKES_RESPONSE, Gson().toJson(jokesResponseModel)).apply()
    }

    private fun getJokesFromStorage(context: Context) : JokesResponseModel? {
        try {
            val sharedPreference: SharedPreferences =
                context.getSharedPreferences(Constants.SHARED_PREFERNCE_NAME, Context.MODE_PRIVATE)
            return Gson().fromJson(sharedPreference.getString(Constants.JOKES_RESPONSE, null), JokesResponseModel::class.java)
        } catch (e : Exception) {
            return null
        }
    }
}