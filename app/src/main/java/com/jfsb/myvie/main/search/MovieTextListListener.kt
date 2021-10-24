package com.jfsb.myvie.main.search

import com.jfsb.myvie.api.Movie

interface MovieTextListListener {
    fun onMovieTextListChange(movies: MutableList<Movie>){

    }
}