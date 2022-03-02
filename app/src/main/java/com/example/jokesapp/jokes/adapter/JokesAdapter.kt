package com.example.jokesapp.jokes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.R
import com.example.jokesapp.databinding.ItemJokeBinding
import com.example.jokesapp.jokes.model.JokesResponseModel

class JokesAdapter : RecyclerView.Adapter<JokesAdapter.JokesViewHolder>() {
    private var jokesList : ArrayList<JokesResponseModel.Result> = arrayListOf()

    fun setJokesList(jokesList : ArrayList<JokesResponseModel.Result>) {
        this.jokesList = jokesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return JokesViewHolder(ItemJokeBinding.bind(inflater.inflate(R.layout.item_joke, parent, false)))
    }

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        val joke = jokesList[position]
        holder.binding.jokesView.text = joke.joke
    }

    override fun getItemCount(): Int {
        return jokesList.size
    }

    class JokesViewHolder(val binding : ItemJokeBinding) : RecyclerView.ViewHolder(binding.root)
}