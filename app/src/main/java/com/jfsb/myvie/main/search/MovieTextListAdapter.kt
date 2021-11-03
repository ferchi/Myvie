package com.jfsb.myvie.main.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.objects.Utils.getGenre
import com.jfsb.myvie.databinding.ItemMovieListTextBinding

class MovieTextListAdapter (private val movies: MutableList<Movie>,
                            private var movieTextListListener: MovieTextListListener,
                            private val onMovieClick: (movies: Movie) -> Unit
                           ):

    RecyclerView.Adapter<MovieTextListAdapter.ViewHolder>(){

    lateinit var mContext:Context
    var lastPosition = -1
    var durationS = ""
    var moviesAux:MutableList<Movie> = mutableListOf()

    class ViewHolder (val binding: ItemMovieListTextBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(ItemMovieListTextBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isSelected:Boolean = false
        val animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row)
        val movie = movies[position]
        val genders = movie.genresIds

        var gendersName : MutableList<String> = mutableListOf()

        genders.forEach { gender ->
            gendersName.add(getGenre(gender))
        }

        holder.binding.tvItemMovieNameText.text =  "${movie.title} (${movie.releaseDate})"
        try{holder.binding.tvItemMovieGenderText.text = gendersName.joinToString(" , ") }
        catch (e:Exception){
            holder.binding.tvItemMovieGenderText.text = "No disponible"
        }

        holder.binding.tvItemMovieRankingText.text = ((movie.rating/2).toString() + "/5")
        getDuration(movie.id)
        holder.binding.tvItemMovieDurationText.text = durationS

        holder.binding.cardItemMovieContainerText.setOnClickListener { onMovieClick.invoke(movie) }


        if(holder.adapterPosition > lastPosition){
            holder.binding.cardItemMovieContainerText.startAnimation(animation)
            lastPosition = holder.adapterPosition
        }
/*
        holder.binding.checkItemMovieText.setOnClickListener {
            if(movie.isSelected){
                holder.binding.checkItemMovieText.speed = (-1).toFloat()
                holder.binding.checkItemMovieText.playAnimation()
                movie.isSelected = !movie.isSelected
                moviesAux.add(movie)
            } else {
                holder.binding.checkItemMovieText.speed = (1).toFloat()
                holder.binding.checkItemMovieText.playAnimation()
                isSelected = !isSelected
                moviesAux.remove(movie)
            }
            movieTextListListener.onMovieTextListChange(moviesAux)
        }*/

    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    fun clearMovies() {
        this.movies.clear()
        notifyItemRangeInserted(0, 0)
    }

    private fun getDuration(movieId:Long){
        MoviesRepository.getMoreInfoMovie(
            movieId,
            ::onMoreInfoMovieFetched,
            ::onError
        )
    }

    private fun onMoreInfoMovieFetched(duration: Long) {

        durationS = (duration).toString() + " Minutos"
    }

    private fun onError(error:String){
        Toast.makeText(mContext,"No se obtuvo duración : $error", Toast.LENGTH_SHORT).show()
    }
}