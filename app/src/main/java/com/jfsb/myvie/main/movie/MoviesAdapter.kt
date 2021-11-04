package com.jfsb.myvie.main.movie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Movie
import xyz.damonbaker.strikedimageview.StrikedImageView

class MoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun appendMovies(movies: List<Movie>) {
        Log.d("movies", itemCount.toString())
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val strikedImage: StrikedImageView = itemView.findViewById(R.id.item_movie_seen)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar_movie_poster)

        fun bind(movie: Movie) {

            if(movie.posterPath != null){
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                    .transform(CenterCrop())
                    .into(poster)}
            else{
                Glide.with(itemView)
                    .load("https://firebasestorage.googleapis.com/v0/b/myvieapp.appspot.com/o/camera_0.png?alt=media&token=530d3f07-5c1f-4243-9301-169ce948f691")
                    .fitCenter()
                    .into(poster)
            }
            strikedImage.setOnClickListener {
                strikedImage.isStriked = false
            }
            ratingBar.rating = movie.rating/2
            itemView.setOnClickListener { onMovieClick.invoke(movie) }
        }

    }
}