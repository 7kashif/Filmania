package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmaxtesting.adapter.movie.SimilarMoviesAdapter
import com.example.filmaxtesting.databinding.FragmentMoviesHostBinding
import com.example.filmaxtesting.viewModel.movie.MovieHostViewModel

class MoviesHostFragment : Fragment() {
    private lateinit var binding: FragmentMoviesHostBinding
    private val viewModel : MovieHostViewModel by activityViewModels()
    private var upComingMoviesAdapter = SimilarMoviesAdapter()
    private var trendingMoviesAdapter = SimilarMoviesAdapter()
    private var popularMoviesAdapter = SimilarMoviesAdapter()
    private var topRatedMoviesAdapter = SimilarMoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMoviesHostBinding.inflate(inflater)
        setUpRVs()

        lifecycleScope.launchWhenCreated {
            viewModel.upComingMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null)
                    upComingMoviesAdapter.submitList(it.body()!!.results)
            })
            viewModel.trendingMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null)
                    trendingMoviesAdapter.submitList(it.body()!!.results)
            })
            viewModel.popularMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null)
                    popularMoviesAdapter.submitList(it.body()!!.results)
            })
            viewModel.topRatedMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null)
                    topRatedMoviesAdapter.submitList(it.body()!!.results)
            })
        }

        return binding.root
    }

    private fun setUpRVs() {
        binding.apply {
            upcomingRv.apply {
                adapter= upComingMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            trendingRv.apply {
                adapter= trendingMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            popularRv.apply {
                adapter= popularMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            topRatedRv.apply {
                adapter= topRatedMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
        }
    }

}






//        val pagerAdapter= activity?.let { MoviesViewPagerAdapter(it) }
//        binding.viewPager.adapter=pagerAdapter
//
//        TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
//            when(position) {
//                0-> tab.text="Popular"
//                1-> tab.text="Top Rated"
//            }
//        }.attach()