package com.jfsb.myvie.main.people

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.api.*
import com.jfsb.myvie.databinding.DialogPeopleMoviesBinding
import com.jfsb.myvie.main.movie.ActorAdapter
import com.jfsb.myvie.main.movie.MoviesAdapter
import java.util.*


class PeopleMoviesDialog : DialogFragment(){

    private var _binding : DialogPeopleMoviesBinding? = null
    val binding get() = _binding!!

    private lateinit var peopleMoviesLayoutMgr: GridLayoutManager
    private lateinit var peopleMoviesAdapter: MoviesAdapter
    private var peopleId:Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = DialogPeopleMoviesBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            peopleId = arguments?.getInt(PEOPLE_ID,0)!!
            val peopleName = arguments?.getString(PEOPLE_NAME,"")!!

            binding.tvDialogNamePeople.text = peopleName
            getPeopleMovies()

            peopleMoviesLayoutMgr = GridLayoutManager(requireContext(),2)
            binding.rvDialogPeople.layoutManager = peopleMoviesLayoutMgr

            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }


    private fun getPeopleMovies() {
        MoviesRepository.getPeopleMovies(
            peopleId,
            ::onGetPeopleMoviesFetched,
            ::onError
        )
    }

    private fun onGetPeopleMoviesFetched(castList: List<Movie>, crewList: List<Movie>) {

        peopleMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> Utils.showMovieDetails(movie, requireContext()) }
        peopleMoviesAdapter.appendMovies(castList)
        peopleMoviesAdapter.appendMovies(crewList)
        binding.rvDialogPeople.adapter = peopleMoviesAdapter
        //attachPeopleMoviesOnScrollListener()
    }

    private fun attachPeopleMoviesOnScrollListener() {
        binding.rvDialogPeople.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = peopleMoviesLayoutMgr.itemCount
                val visibleItemCount = peopleMoviesLayoutMgr.childCount
                val firstVisibleItem = peopleMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.rvDialogPeople.removeOnScrollListener(this)
                    getPeopleMovies()
                }
            }
        })
    }

    private fun onError() {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val PEOPLE_ID = "extra_people_id"
        const val PEOPLE_NAME = "extra_people_name"
    }
}