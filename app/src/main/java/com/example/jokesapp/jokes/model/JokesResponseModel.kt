package com.example.jokesapp.jokes.model


import com.google.gson.annotations.SerializedName

data class JokesResponseModel(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next_page")
    val nextPage: Int,
    @SerializedName("previous_page")
    val previousPage: Int,
    @SerializedName("results")
    val jokesList: ArrayList<Result>,
    @SerializedName("search_term")
    val searchTerm: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("total_jokes")
    val totalJokes: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) {
    data class Result(
        @SerializedName("id")
        val id: String,
        @SerializedName("joke")
        val joke: String
    )
}