package com.example.jokesapp.jokes.services

import com.example.jokesapp.jokes.model.JokesResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface JokesServices {
    @Headers("Accept: application/json")
    @GET("search")
    fun getJokes(
        @Query("page") page : Int,
        @Query("limit") pageLimit : Int,
        @Query("term") searchedJoke : String
    ) : Call<JokesResponseModel>
}