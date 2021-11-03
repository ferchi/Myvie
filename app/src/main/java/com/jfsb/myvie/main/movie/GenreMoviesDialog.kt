package com.jfsb.myvie.main.movie

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.jfsb.myvie.api.*
import com.jfsb.myvie.databinding.DialogGenreMoviesBinding
import com.jfsb.myvie.objects.Utils


class GenreMoviesDialog : DialogFragment(){

    private var _binding : DialogGenreMoviesBinding? = null
    val binding get() = _binding!!

    private lateinit var genreMoviesLayoutMgr: Utils.GridLayoutManagerWrapper
    private lateinit var genreMoviesAdapter: MoviesAdapter
    private var genreId:Long = 0
    private var moviesPage = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = DialogGenreMoviesBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)


            genreId = arguments?.getString(GENRE_ID,"0")!!.toLong()
            val genreName = arguments?.getString(GENRE_NAME,"")!!

            binding.tvDialogNameGenre.text = genreName
            getMoviesByGenre()

            genreMoviesLayoutMgr = Utils.GridLayoutManagerWrapper(requireContext(), 2)
            binding.rvDialogGenre.layoutManager = genreMoviesLayoutMgr

            binding.btnGenreBackPage.setOnClickListener { btn ->
                binding.btnGenreBackPage.speed = (2).toFloat()
                binding.btnGenreBackPage.playAnimation()
                changePageMovies(btn)
            }
            binding.btnGenreNextPage.setOnClickListener { btn ->
                binding.btnGenreNextPage.speed = (2).toFloat()
                binding.btnGenreNextPage.playAnimation()
                changePageMovies(btn)
            }

            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }


    private fun getMoviesByGenre() {
        MoviesRepository.getMoviesByGenre(
            genreId,
            moviesPage,
            ::onGetMoviesFetched,
            ::onError
        )
    }

    private fun onGetMoviesFetched(movies: List<Movie>) {
        genreMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> Utils.showMovieDetails(movie, requireContext()) }
        genreMoviesAdapter.appendMovies(movies)
        binding.rvDialogGenre.adapter = genreMoviesAdapter

    }

    private fun changePageMovies(btn:View) {
            when (btn.id) {
                binding.btnGenreBackPage.id -> {
                    if(moviesPage>1) {
                    moviesPage--
                    binding.tvGenrePage.text = moviesPage.toString()
                    getMoviesByGenre()
                    }
                }
                binding.btnGenreNextPage.id -> {
                    moviesPage++
                    binding.tvGenrePage.text = moviesPage.toString()
                    getMoviesByGenre()
                }
            }

    }

    private fun onError() {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val GENRE_ID = "extra_genre_id"
        const val GENRE_NAME = "extra_genre_name"
    }
}