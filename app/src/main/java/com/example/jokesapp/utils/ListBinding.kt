package com.example.jokesapp.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.jokes.adapter.JokesAdapter
import com.example.jokesapp.jokes.model.JokesResponseModel

object ListBinding {
    @BindingAdapter("jokesList")
    @JvmStatic fun setJokeList(recyclerView: RecyclerView, items: ArrayList<JokesResponseModel.Result>) {
        if(items.isNullOrEmpty())
            return
        with(recyclerView.adapter as JokesAdapter) {
            setJokesList(items)
        }
    }
}