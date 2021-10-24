package com.jfsb.myvie.main.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.FragmentSearchMovieBinding
import com.jfsb.myvie.databinding.FragmentSearchParentBinding
import com.jfsb.myvie.main.fragments.ViewPagerAdapter


class SearchParentFragment : Fragment() {
    private var _binding : FragmentSearchParentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchParentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        childFragmentManager.beginTransaction()
            .add(R.id.flProfile, ProfileFragment())
            .commit()*/
        setUpTabs()
    }

    private fun setUpTabs() {
        //val adapter = ViewPagerAdapter((activity as AppCompatActivity?)!!.supportFragmentManager)

        //Utilizar el childFragmentManager para evitar errores dentro del tab layout
        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(SearchMovieFragment(), "Películas")
        adapter.addFragment(SearchActorFragment(), "Personas")

        binding.viewPager.adapter = adapter
        binding.tabs.setViewPager(binding.viewPager)
        binding.tabs.setTitles("Películas","Personas")


        //binding.tabs.getTabAt(0)!!//.setIcon(R.drawable.myvie_logo)
        //binding.tabs.getTabAt(1)!!//.setIcon(R.drawable.ic_person_24)

    }
}