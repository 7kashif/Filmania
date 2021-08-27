package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.filmaxtesting.R
import com.example.filmaxtesting.databinding.FragmentMainBinding
import com.example.filmaxtesting.fragments.movie.MoviesHostFragment
import com.example.filmaxtesting.fragments.people.PopularPeopleFragment
import com.example.filmaxtesting.fragments.show.TvShowsHostFragment

class MainFragment:Fragment() {
    private lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        val moviesFragment = MoviesHostFragment()
        val showsFragment = TvShowsHostFragment()
        val bookMarksFragment = BookMarksFragment()
        val peopleFragment = PopularPeopleFragment()

        setCurrentFragment(moviesFragment)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuMovies -> setCurrentFragment(moviesFragment)
                R.id.menuShows -> setCurrentFragment(showsFragment)
                R.id.menuBookMarks -> setCurrentFragment(bookMarksFragment)
                R.id.menuPeople -> setCurrentFragment(peopleFragment)
            }
            true
        }

        binding.searchFab.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToSearchFragment())
        }

        return binding.root
    }

    private fun setCurrentFragment(fragment : Fragment)  {
        activity?.let {
            it.supportFragmentManager.beginTransaction().apply {
                replace(binding.flFragment.id, fragment)
                commit()
            }
        }
    }
}