package com.jfsb.myvie.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.api.MovieResponse
import com.jfsb.myvie.main.movie.MoviesAdapter
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.databinding.FragmentHomeBinding
import com.jfsb.myvie.main.movie.MovieInfoActivity
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_BACKDROP
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_GENRES
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_ID
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_OVERVIEW
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_POSTER
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_RATING
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_RELEASE_DATE
import com.jfsb.myvie.main.movie.MovieInfoActivity.Companion.MOVIE_TITLE
import java.util.ArrayList


class HomeFragment : Fragment() {
    var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!

    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private var popularMoviesPage = 1
    private lateinit var popularMoviesAdapter: MoviesAdapter

    private lateinit var newMoviesLayoutMgr: LinearLayoutManager
    private var newMoviesPage = 1
    private lateinit var newMoviesAdapter: MoviesAdapter

    private lateinit var topMoviesLayoutMgr: LinearLayoutManager
    private var topMoviesPage = 1
    private lateinit var topMoviesAdapter: MoviesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MoviesRepository.getPopularMovies(
            onSuccess = ::onPopularMoviesFetched,
            onError = ::onError
        )

        MoviesRepository.getNewMovies(
            onSuccess = ::onNewMoviesFetched,
            onError = ::onError
        )

        MoviesRepository.getNewMovies(
            onSuccess = ::onTopMoviesFetched,
            onError = ::onError
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularMoviesLayoutMgr = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        newMoviesLayoutMgr = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        topMoviesLayoutMgr = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.popularMovies.layoutManager = popularMoviesLayoutMgr
        popularMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.popularMovies.adapter = popularMoviesAdapter

        binding.newMovies.layoutManager = newMoviesLayoutMgr
        newMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> showMovieDetails(movie) }
        binding.newMovies.adapter = newMoviesAdapter

        binding.topMovies.layoutManager = topMoviesLayoutMgr
        topMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> showMovieDetails(movie) }
        binding.topMovies.adapter = topMoviesAdapter

        getPopularMovies()
        getNewMovies()
        getTopMovies()
    }
    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
            popularMoviesPage,
            ::onPopularMoviesFetched,
            ::onError
        )
    }
    private fun getNewMovies() {
        MoviesRepository.getPopularMovies(
            newMoviesPage,
            ::onNewMoviesFetched,
            ::onError
        )
    }
    private fun getTopMovies() {
        MoviesRepository.getTopMovies(
            topMoviesPage,
            ::onTopMoviesFetched,
            ::onError
        )
    }
    private fun attachPopularMoviesOnScrollListener() {
        binding.popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }
    private fun attachNewMoviesOnScrollListener() {
        binding.newMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = newMoviesLayoutMgr.itemCount
                val visibleItemCount = newMoviesLayoutMgr.childCount
                val firstVisibleItem = newMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.newMovies.removeOnScrollListener(this)
                    newMoviesPage++
                    getNewMovies()
                }
            }
        })
    }
    private fun attachTopMoviesOnScrollListener() {
        binding.topMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topMoviesLayoutMgr.itemCount
                val visibleItemCount = topMoviesLayoutMgr.childCount
                val firstVisibleItem = topMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.topMovies.removeOnScrollListener(this)
                    topMoviesPage++
                    getTopMovies()
                }
            }
        })
    }

    private fun onPopularMoviesFetched(movies: List<MovieResponse>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }
    private fun onNewMoviesFetched(movies: List<MovieResponse>) {
        newMoviesAdapter.appendMovies(movies)
        attachNewMoviesOnScrollListener()
    }
    private fun onTopMoviesFetched(movies: List<MovieResponse>) {
        topMoviesAdapter.appendMovies(movies)
        attachTopMoviesOnScrollListener()
    }

    private fun onError() {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
    }

    private fun showMovieDetails(movie: MovieResponse) {
        val intent = Intent(requireContext(), MovieInfoActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        intent.putStringArrayListExtra(MOVIE_GENRES, movie.genresIds as ArrayList<String>?)
        intent.putExtra(MOVIE_ID, movie.id)

        startActivity(intent)
    }
}