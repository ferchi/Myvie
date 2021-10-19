package com.jfsb.myvie.api

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.jfsb.myvie.main.movie.MovieInfoActivity
import java.util.ArrayList

import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_BACKDROP
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_GENRES
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_ID
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_OVERVIEW
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_POSTER
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_RATING
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_RELEASE_DATE
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_TITLE

object Utils {
     fun showMovieDetails(movie: MovieResponse, context: Context) {
        val intent = Intent(context, MovieInfoActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        intent.putStringArrayListExtra(MOVIE_GENRES, movie.genresIds as ArrayList<String>?)
        intent.putExtra(MOVIE_ID, movie.id)

        context.startActivity(intent)
    }

}