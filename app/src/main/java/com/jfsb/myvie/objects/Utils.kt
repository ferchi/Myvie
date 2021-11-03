package com.jfsb.myvie.objects

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.jfsb.myvie.main.people.PeopleMoviesDialog

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.main.people.PeopleMoviesDialog.Companion.PEOPLE_ID
import com.jfsb.myvie.main.people.PeopleMoviesDialog.Companion.PEOPLE_NAME


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

   fun showPeopleDetails(idPeople: Int, namePeople:String, context: FragmentActivity) {
      val peopleMoviesDialog = PeopleMoviesDialog()
      val args = Bundle()

      args.putInt(PEOPLE_ID, idPeople)
      args.putString(PEOPLE_NAME, namePeople)

      peopleMoviesDialog.arguments = args

      peopleMoviesDialog.show(context.supportFragmentManager, "Crear")
   }

   fun showPeopleDetails(idPeople: Int, namePeople:String, context: AppCompatActivity) {
      val peopleMoviesDialog = PeopleMoviesDialog()
      val args = Bundle()

      args.putInt(PEOPLE_ID, idPeople)
      args.putString(PEOPLE_NAME, namePeople)

      peopleMoviesDialog.arguments = args

      peopleMoviesDialog.show(context.supportFragmentManager, "Crear")
   }

   class GridLayoutManagerWrapper : GridLayoutManager {
      constructor(context: Context?, spanCount:Int) : super(context, spanCount) {}
      constructor(context: Context?, spanCount:Int, orientation: Int, reverseLayout: Boolean) : super(
         context,
         spanCount,
         orientation,
         reverseLayout
      ) {
      }

      constructor(
         context: Context?,
         attrs: AttributeSet?,
         defStyleAttr: Int,
         defStyleRes: Int
      ) : super(context, attrs, defStyleAttr, defStyleRes) {
      }

      override fun supportsPredictiveItemAnimations(): Boolean {
         return false
      }
   }

   class LinearLayoutManagerWrapper : LinearLayoutManager {
      constructor(context: Context?) : super(context) {}
      constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
         context,
         orientation,
         reverseLayout
      ) {
      }

      constructor(
         context: Context?,
         attrs: AttributeSet?,
         defStyleAttr: Int,
         defStyleRes: Int
      ) : super(context, attrs, defStyleAttr, defStyleRes) {
      }

      override fun supportsPredictiveItemAnimations(): Boolean {
         return false
      }
   }


}