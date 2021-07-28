package com.example.filmaxtesting.fragments.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.filmaxtesting.adapter.ShowsViewPagerAdapter
import com.example.filmaxtesting.databinding.FragmentTvShowsHostBinding
import com.google.android.material.tabs.TabLayoutMediator

class TvShowsHostFragment : Fragment() {
    private lateinit var binding: FragmentTvShowsHostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTvShowsHostBinding.inflate(inflater)

        val pagerAdapter= activity?.let { ShowsViewPagerAdapter(it) }
        binding.viewPager.adapter=pagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
            when(position) {
                0-> tab.text="Popular"
                1-> tab.text="Top Rated"
            }
        }.attach()

        return binding.root
    }
}