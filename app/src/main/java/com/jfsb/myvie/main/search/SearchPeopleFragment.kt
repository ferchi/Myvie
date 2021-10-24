package com.jfsb.myvie.main.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.api.People
import com.jfsb.myvie.api.Utils
import com.jfsb.myvie.databinding.FragmentSearchPeopleBinding


class SearchActorFragment : Fragment(), SearchView.OnQueryTextListener, MovieTextListListener {
    var _binding : FragmentSearchPeopleBinding? = null
    val binding get() = _binding!!

    private lateinit var searchLayoutMgr: LinearLayoutManager
    private lateinit var peopleTextAdapter: PeopleTextListAdapter
    private lateinit var peopleImageAdapter: PeopleImageListAdapter

    private var searchQuery : String = "minions"
    private var searchPeoplePage = 1
    private var isTextMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchPeopleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLayoutMgr = Utils.GridLayoutManagerWrapper(requireContext(), 2)


        binding.ivSearchPeopleMode.speed = (3).toFloat()
        binding.ivSearchPeopleMode.playAnimation()

        binding.svPeople.setOnQueryTextListener(this)

        binding.rvSearchPeople.layoutManager = searchLayoutMgr
        peopleTextAdapter = PeopleTextListAdapter(mutableListOf()){ people -> Utils.showPeopleDetails(people.idPeople, people.namePeople, requireActivity()) }
        binding.rvSearchPeople.adapter = peopleTextAdapter

        binding.ivSearchPeopleMode.setOnClickListener {
            if(isTextMode == true){
                binding.ivSearchPeopleMode.speed = (-3).toFloat()
                binding.ivSearchPeopleMode.playAnimation()
                changeListMode(false)
                Log.d("modo", "Modo imagen" )
            } else {
                binding.ivSearchPeopleMode.speed = (3).toFloat()
                binding.ivSearchPeopleMode.playAnimation()
                changeListMode(true)
                Log.d("modo", "Modo texto" )
            }
        }

    }
    private fun changeListMode(isTextMode:Boolean){
        val query = binding.svPeople.query
        this.isTextMode = isTextMode

        if(isTextMode)
        {
            peopleTextAdapter = PeopleTextListAdapter(mutableListOf()){ people -> Utils.showPeopleDetails(people.idPeople, people.namePeople, requireActivity()) }
            binding.rvSearchPeople.adapter = peopleTextAdapter

            if(!binding.svPeople.query.isNullOrEmpty()) {
                searchQuery = query.toString()
                searchPeoplePage = 1
                peopleTextAdapter.clearPeople()
                searchPeople(searchQuery)
            }
        }
        else
        {
           peopleImageAdapter = PeopleImageListAdapter(mutableListOf()){people -> Utils.showPeopleDetails(people.idPeople, people.namePeople, requireActivity()) }
            binding.rvSearchPeople.adapter = peopleImageAdapter

            if(!binding.svPeople.query.isNullOrEmpty()) {
                searchQuery = query.toString()
                searchPeoplePage = 1
                peopleImageAdapter.clearPeople()
                searchPeople(searchQuery)
            }
        }
    }

    private fun searchPeople(movieQuery:String) {
        MoviesRepository.searchPeople(
            searchPeoplePage,
            movieQuery,
            ::onSearchPeopleFetched,
            ::onError
        )
    }

    private fun onSearchPeopleFetched(people: List<People>) {
        if(isTextMode) {
            peopleTextAdapter.appendPeople(people)}
        else{
            peopleImageAdapter.appendPeople(people)
        }
        attachMoviesOnScrollListener()
    }
    private fun attachMoviesOnScrollListener() {
        binding.rvSearchPeople.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = searchLayoutMgr.itemCount
                val visibleItemCount = searchLayoutMgr.childCount
                val firstVisibleItem = searchLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.rvSearchPeople.removeOnScrollListener(this)
                    searchPeoplePage++
                    searchPeople(searchQuery)
                }
            }
        })
    }

    private fun onError(error:String = ""){
        Toast.makeText(requireContext(),error, Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchQuery = query!!
        searchPeoplePage = 1
        if(isTextMode) {
            peopleTextAdapter.clearPeople()}
        else{
            peopleImageAdapter.clearPeople()
        }

        searchPeople(searchQuery)
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