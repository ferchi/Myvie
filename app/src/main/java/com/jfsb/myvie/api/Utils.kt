package com.jfsb.myvie.api

import android.content.Context
import android.content.Intent
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
     fun showMovieDetails(movie: Movie, context: Context) {
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

   fun getGenre(index:String):String{
      when(index){
         "28"-> return "Acción"
         "12"-> return "Aventura"
         "16"->return "Animación"
         "35"->return "Comedia"
         "80"->return "Crimen"
         "99"->return "Documental"
         "18"->return "Drama"
         "10751"->return "Familia"
         "14"->return "Fantasía"
         "36"->return "Historia"
         "27"->return "Terror"
         "10402"->return "Música"
         "9648"->return "Misterio"
         "10749"->return "Romance"
         "878"->return "Ciencia ficción"
         "10770"->return "Película de TV"
         "53"->return "Suspense"
         "10752"->return "Bélica"
         "37"-> return "Western"
      }
      return "NINGUNO"
   }

   fun showPeopleDetails(people: People, context: Context) {
   }



}