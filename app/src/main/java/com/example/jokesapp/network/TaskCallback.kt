package com.example.jokesapp.network

import okhttp3.Response

interface TaskCallback<T> {
    /**
     * Given task was completed
     */
    fun onComplete(result: T)

    /**
     * Error happens in the task
     */
    fun onError(errorResponse : Response, responseCode : Int)

    /**
     * Exception happens in the task
     */
    fun onException(t: Throwable?)
}