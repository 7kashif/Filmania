package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.ViewDialog
import com.example.filmaxtesting.adapter.movie.SimilarMoviesAdapter
import com.example.filmaxtesting.adapter.shows.SimilarShowsAdapter
import com.example.filmaxtesting.databinding.FragmentHomeBinding
import com.example.filmaxtesting.fragments.movie.MoviesDetailDialogFragment
import com.example.filmaxtesting.fragments.show.ShowDetailsDialogFragment
import com.example.filmaxtesting.isNetworkAvailable
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
        setupClickListeners()

        if(isNetworkAvailable(requireActivity())) {
            binding.dataLayout.visibility = View.VISIBLE
            loadData()
        }
        else {
            binding.noNetwork.visibility = View.VISIBLE
        }

        binding.aboutButton.setOnClickListener {
            showPopUpMenu(it,inflater)
        }

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
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPopularMoviesFragment())
        }

        binding.seeAllPopularShows.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPopularShowsFragment())
        }

        binding.seeAllTopRatedMovies.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTopRatedMoviesFragment())
        }

        binding.seeAllTopRatedShows.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTopRatedShowsFragment())
        }

        binding.seeAllUpComingMovies.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUpComingMoviesFragment())
        }

        binding.seeAllTrendingMovies.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTrendingMoviesFragment())
        }
        binding.seeAllTrendingShows.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTrendingShowsFragment())
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

    private fun showPopUpMenu(view: View, inflater: LayoutInflater) {
        PopupMenu(activity,view).apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuAbout -> ViewDialog.showAbout(activity,inflater)
                }
                true
            }
            inflate(R.menu.about_menu)
            show()
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