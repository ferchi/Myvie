package com.jfsb.myvie.main.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.databinding.ItemEditCollectionBinding

class CollectionEditListAdapter (private val movies: MutableList<Movie>,
                                 private val onMovieClick: (movies: Movie) -> Unit):

    RecyclerView.Adapter<CollectionEditListAdapter.ViewHolder>(){

    lateinit var mContext:Context
    var lastPosition = -1
    var durationS = ""
    var moviesAux:MutableList<Movie> = mutableListOf()

    class ViewHolder (val binding: ItemEditCollectionBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(ItemEditCollectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row)
        val movie = movies[position]

        val name = "${movie.title} (${movie.releaseDate})"

        holder.binding.tvItemCollectionDeleteName.text =  name
        holder.binding.cardItemCollectionDelete.setOnClickListener {
            onMovieClick.invoke(movie)
        }

        if(holder.adapterPosition > lastPosition){
            holder.binding.cardItemCollectionDelete.startAnimation(animation)
            lastPosition = holder.adapterPosition
        }
    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    fun appendMovie(movie: Movie) {
        if(movie !in movies) {
            this.movies.add(movie)
            notifyItemRangeInserted(
                this.movies.size,
                movies.size - 1
            )
        }
    }

    fun clearMovies() {
        this.movies.clear()
        notifyItemRangeInserted(0, 0)
    }

    private fun onError(error:String){
        Toast.makeText(mContext,"No se obtuvo duración : $error", Toast.LENGTH_SHORT).show()
    }
}