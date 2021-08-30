package com.example.filmaxtesting.adapter.shows

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.filmaxtesting.fragments.show.PopularShowsFragment
import com.example.filmaxtesting.fragments.show.TopRatedShowsFragment

class ShowsViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> PopularShowsFragment()
            else -> TopRatedShowsFragment()
        }
    }
}