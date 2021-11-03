package com.jfsb.myvie.main.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.objects.Utils
import com.jfsb.myvie.databinding.FragmentSearchMovieBinding


class SearchMovieFragment : Fragment(), SearchView.OnQueryTextListener, MovieTextListListener {
    var _binding : FragmentSearchMovieBinding? = null
    val binding get() = _binding!!

    private lateinit var searchLayoutMgr: LinearLayoutManager
    private lateinit var movieTextAdapter: MovieTextListAdapter
    private lateinit var movieImageAdapter: MovieImageListAdapter
    private var searchQuery : String = "minions"
    private var searchMoviesPage = 1
    private var movieId:Long = 0
    private var isTextMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchMovieBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLayoutMgr = Utils.LinearLayoutManagerWrapper(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.ivSearchMovieMode.speed = (3).toFloat()
        binding.ivSearchMovieMode.playAnimation()

        binding.svMovies.setOnQueryTextListener(this)

        binding.rvSearchMovies.layoutManager = searchLayoutMgr
        movieTextAdapter = MovieTextListAdapter(mutableListOf(),this){ movie -> Utils.showMovieDetails(movie, requireContext()) }
        binding.rvSearchMovies.adapter = movieTextAdapter

        binding.ivSearchMovieMode.setOnClickListener {
            if(isTextMode == true){
                binding.ivSearchMovieMode.speed = (-3).toFloat()
                binding.ivSearchMovieMode.playAnimation()
                changeListMode(false)
                Log.d("modo", "Modo imagen" )
            } else {
                binding.ivSearchMovieMode.speed = (3).toFloat()
                binding.ivSearchMovieMode.playAnimation()
                changeListMode(true)
                Log.d("modo", "Modo texto" )
            }
        }

    }
    private fun changeListMode(isTextMode:Boolean){
        val query = binding.svMovies.query
        this.isTextMode = isTextMode

        if(isTextMode)
        {
            movieTextAdapter = MovieTextListAdapter(mutableListOf(),this){ movie -> Utils.showMovieDetails(movie, requireContext()) }
            binding.rvSearchMovies.adapter = movieTextAdapter

            if(!binding.svMovies.query.isNullOrEmpty()) {
                searchQuery = query.toString()
                searchMoviesPage = 1
                movieTextAdapter.clearMovies()
                searchMovie(searchQuery)
            }
        }
        else
        {
            movieImageAdapter = MovieImageListAdapter(mutableListOf(),this){ movie -> Utils.showMovieDetails(movie, requireContext()) }
            binding.rvSearchMovies.adapter = movieImageAdapter

            if(!binding.svMovies.query.isNullOrEmpty()) {
                searchQuery = query.toString()
                searchMoviesPage = 1
                movieImageAdapter.clearMovies()
                searchMovie(searchQuery)
            }
        }
    }

    private fun searchMovie(movieQuery:String) {
        MoviesRepository.searchMovie(
            searchMoviesPage,
            movieQuery,
            ::onSearchMovieFetched,
            ::onError
        )
    }

    private fun onSearchMovieFetched(movies: List<Movie>) {
        if(isTextMode) {
            movieTextAdapter.appendMovies(movies)}
        else{
            movieImageAdapter.appendMovies(movies)
        }
        attachMoviesOnScrollListener()
    }
    private fun attachMoviesOnScrollListener() {
        binding.rvSearchMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = searchLayoutMgr.itemCount
                val visibleItemCount = searchLayoutMgr.childCount
                val firstVisibleItem = searchLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.rvSearchMovies.removeOnScrollListener(this)
                    searchMoviesPage++
                    searchMovie(searchQuery)
                }
            }
        })
    }

    private fun onError(error:String = ""){
        Toast.makeText(requireContext(),error, Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchQuery = query!!
        searchMoviesPage = 1
        if(isTextMode) {
            movieTextAdapter.clearMovies()}
        else{
            movieImageAdapter.clearMovies()
        }

        searchMovie(searchQuery)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
       return false
    }
    /*
    override fun onMovieTextListChange(movies: MutableList<Movie>){
        Toast.makeText(requireContext(),movies.toString(), Toast.LENGTH_SHORT).show()
    }*/
}