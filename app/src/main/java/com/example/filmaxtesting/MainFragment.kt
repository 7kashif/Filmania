package com.example.filmaxtesting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.filmaxtesting.databinding.FragmentMainBinding
import com.example.filmaxtesting.fragments.BookMarksFragment
import com.example.filmaxtesting.fragments.movie.MoviesHostFragment
import com.example.filmaxtesting.fragments.show.TvShowsHostFragment
import com.example.filmaxtesting.viewModel.PagingViewModel

class MainFragment : Fragment() {
    private lateinit var binding:FragmentMainBinding
    private val viewModel:PagingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMainBinding.inflate(inflater)

        val moviesFragment= MoviesHostFragment()
        val showsFragment= TvShowsHostFragment()
        val bookMarksFragment= BookMarksFragment()

        when(viewModel.lastFragment) {
            "shows" -> setCurrentFragment(showsFragment)
            "bookmarks" -> setCurrentFragment(bookMarksFragment)
            else -> setCurrentFragment(moviesFragment)
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menuMovies -> setCurrentFragment(moviesFragment)
                R.id.menuShows -> setCurrentFragment(showsFragment)
                R.id.menuBookMarks -> setCurrentFragment(bookMarksFragment)
            }
            true
        }

        return binding.root
    }

    private fun setCurrentFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
        replace(R.id.flFragment,fragment)
        commit()
    }

}