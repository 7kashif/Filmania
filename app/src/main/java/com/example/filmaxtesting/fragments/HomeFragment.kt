package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.movie.SimilarMoviesAdapter
import com.example.filmaxtesting.adapter.shows.SimilarShowsAdapter
import com.example.filmaxtesting.databinding.FragmentHomeBinding
import com.example.filmaxtesting.fragments.movie.*
import com.example.filmaxtesting.fragments.show.PopularShowsFragment
import com.example.filmaxtesting.fragments.show.ShowDetailsDialogFragment
import com.example.filmaxtesting.fragments.show.TopRatedShowsFragment
import com.example.filmaxtesting.fragments.show.TrendingShowsFragment
import com.example.filmaxtesting.viewModel.movie.MovieHostViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel : MovieHostViewModel by activityViewModels()
    private var upComingMoviesAdapter = SimilarMoviesAdapter()
    private var trendingMoviesAdapter = SimilarMoviesAdapter()
    private var popularMoviesAdapter = SimilarMoviesAdapter()
    private var topRatedMoviesAdapter = SimilarMoviesAdapter()
    private var trendingShowsAdapter = SimilarShowsAdapter()
    private var popularShowsAdapter = SimilarShowsAdapter()
    private var topRatedShowsAdapter = SimilarShowsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(inflater)

        setUpMoviesRVs()
        setUpShowsRvs()
        loadData()
        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        callMoviesClickListeners(upComingMoviesAdapter)
        callMoviesClickListeners(trendingMoviesAdapter)
        callMoviesClickListeners(popularMoviesAdapter)
        callMoviesClickListeners(topRatedMoviesAdapter)

        callShowsClickListeners(trendingShowsAdapter)
        callShowsClickListeners(topRatedShowsAdapter)
        callShowsClickListeners(popularShowsAdapter)


        binding.seeAllPopularMovies.setOnClickListener {
            openRelativeFragment(PopularMoviesFragment())
        }

        binding.seeAllPopularShows.setOnClickListener {
            openRelativeFragment(PopularShowsFragment())
        }

        binding.seeAllTopRatedMovies.setOnClickListener {
            openRelativeFragment(TopRatedMoviesFragment())
        }

        binding.seeAllTopRatedShows.setOnClickListener {
            openRelativeFragment(TopRatedShowsFragment())
        }

        binding.seeAllUpComingMovies.setOnClickListener {
            openRelativeFragment(UpComingMoviesFragment())
        }

        binding.seeAllTrendingMovies.setOnClickListener {
            openRelativeFragment(TrendingMoviesFragment())
        }
        binding.seeAllTrendingShows.setOnClickListener {
            openRelativeFragment(TrendingShowsFragment())
        }

    }

    private fun openRelativeFragment(fragment: Fragment) {
        activity.let {
            it?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.flFragment,fragment)
                commit()
            }
        }
    }

    private fun callMoviesClickListeners(adapter: SimilarMoviesAdapter) {
        adapter.setOnItemClickListener { item->
            activity?.let {
                val dialog = MoviesDetailDialogFragment(item.id)
                dialog.show(it.supportFragmentManager,"movieDetailsDialog")
            }
        }
    }

    private fun callShowsClickListeners(adapter: SimilarShowsAdapter) {
        adapter.setOnItemClickListener { item->
            activity?.let {
                val dialog = ShowDetailsDialogFragment(item.id)
                dialog.show(it.supportFragmentManager,"showsDetailsDialog")
            }
        }
    }

    private fun loadData() {
        lifecycleScope.launchWhenCreated {
            viewModel.upComingMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.upcomingMoviesProgressBar.visibility = View.GONE
                    upComingMoviesAdapter.submitList(it.body()!!.results)
                }
            })
            viewModel.trendingMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.trendingMoviesProgressBar.visibility = View.GONE
                    trendingMoviesAdapter.submitList(it.body()!!.results)
                }
            })
            viewModel.trendingShows.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.trendingShowsProgressBar.visibility = View.GONE
                    trendingShowsAdapter.submitList(it.body()!!.results)
                }
            })
            viewModel.popularMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.popularMoviesProgressBar.visibility = View.GONE
                    popularMoviesAdapter.submitList(it.body()!!.results)
                }
            })
            viewModel.popularShows.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.popularShowsProgressBar.visibility = View.GONE
                    popularShowsAdapter.submitList(it.body()!!.results)
                }
            })
            viewModel.topRatedMovies.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.topRatedMoviesProgressBar.visibility = View.GONE
                    topRatedMoviesAdapter.submitList(it.body()!!.results)
                }
            })
            viewModel.topRatedShows.observe(viewLifecycleOwner,{
                if(it.isSuccessful && it.body()!=null) {
                    binding.topRatedShowsProgressBar.visibility = View.GONE
                    topRatedShowsAdapter.submitList(it.body()!!.results)
                }
            })
        }
    }

    private fun setUpMoviesRVs() {
        binding.apply {
            upcomingMoviesRv.apply {
                adapter= upComingMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            trendingMoviesRv.apply {
                adapter= trendingMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            popularMoviesRv.apply {
                adapter= popularMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            topRatedMoviesRv.apply {
                adapter= topRatedMoviesAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
        }
    }

    private fun setUpShowsRvs() {
        binding.apply {
            trendingShowsRv.apply {
                adapter = trendingShowsAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            popularShowsRv.apply {
                adapter = popularShowsAdapter
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
            topRatedShowsRv.apply {
                adapter = topRatedShowsAdapter
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