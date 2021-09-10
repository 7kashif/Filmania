package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.movie.MoviesAdapter
import com.example.filmaxtesting.databinding.FragmentPagingBinding
import com.example.filmaxtesting.viewModel.movie.TopRatedMoviesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TopRatedMoviesFragment : Fragment() {
    private lateinit var binding: FragmentPagingBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private val pagingViewModel: TopRatedMoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPagingBinding.inflate(inflater)
        binding.title.text = getString(R.string.top_rated_movies)
        pagingViewModel.getMovies()

        setUpRv()
        loadData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            moviesAdapter.refresh()
        }

        lifecycleScope.launch {
            moviesAdapter.loadStateFlow.map {
                it.refresh
            }.distinctUntilChanged()
                .collect {
                    if (it is LoadState.Loading) {
                        binding.rv.isVisible = false
                        binding.linearLayout.visibility = View.VISIBLE
                    }  else {
                        binding.rv.isVisible = true
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.linearLayout.visibility = View.GONE
                    }
                }
        }

        moviesAdapter.setOnItemClickListener {item ->
            activity?.let {
                val dialog= MoviesDetailDialogFragment(item.id)
                dialog.show(it.supportFragmentManager,"Detail Dialog")
            }
        }

        return binding.root
    }

    private fun setUpRv() {
        moviesAdapter= MoviesAdapter()
        binding.rv.apply {
            adapter=moviesAdapter
            layoutManager= GridLayoutManager(activity,3)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO){
            pagingViewModel.moviesList?.flowOn(Dispatchers.IO)?.collect {
                moviesAdapter.submitData(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pagingViewModel.viewModelJob.cancel()
    }

}