package com.jfsb.myvie.main.movie

import com.jfsb.myvie.api.Movie

interface MovieTextListListener {
    fun onMovieTextListChange(movies: MutableList<Movie>){

    }
}