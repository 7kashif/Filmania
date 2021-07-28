package com.example.filmaxtesting.adapter.movie

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.filmaxtesting.fragments.movie.PopularMoviesFragment
import com.example.filmaxtesting.fragments.movie.TopRatedMoviesFragment

class MoviesViewPagerAdapter(fa:FragmentActivity):FragmentStateAdapter(fa) {
    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> PopularMoviesFragment()
            else -> TopRatedMoviesFragment()
        }
    }
}