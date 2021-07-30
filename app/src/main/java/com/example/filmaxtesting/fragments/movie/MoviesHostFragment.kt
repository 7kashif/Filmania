package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.filmaxtesting.adapter.movie.MoviesViewPagerAdapter
import com.example.filmaxtesting.databinding.FragmentMoviesHostBinding
import com.example.filmaxtesting.viewModel.PagingViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MoviesHostFragment : Fragment() {
    private lateinit var binding: FragmentMoviesHostBinding
    private val viewModel:PagingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMoviesHostBinding.inflate(inflater)

        val pagerAdapter= activity?.let { MoviesViewPagerAdapter(it) }
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