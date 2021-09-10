package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.movie.MoviesAdapter
import com.example.filmaxtesting.databinding.FragmentPagingBinding
import com.example.filmaxtesting.viewModel.movie.UpComingMoviesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UpComingMoviesFragment : Fragment() {
    private lateinit var binding: FragmentPagingBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private val pagingViewModel: UpComingMoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagingBinding.inflate(inflater)
        binding.title.text = getString(R.string.upcoming)
        pagingViewModel.getUpcomingMovies()

        setUpRv()
        loadData()

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

        binding.swipeRefreshLayout.setOnRefreshListener {
            moviesAdapter.refresh()
        }

        moviesAdapter.setOnItemClickListener { item ->
            activity?.let {
                val dialog = MoviesDetailDialogFragment(item.id)
                dialog.show(it.supportFragmentManager, "Detail Dialog")
            }
        }


        return binding.root
    }

    private fun setUpRv() {
        moviesAdapter = MoviesAdapter()
        binding.rv.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(activity, 3)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            pagingViewModel.moviesList?.flowOn(Dispatchers.IO)?.collect { pagingData ->
                moviesAdapter.submitData(pagingData)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pagingViewModel.viewModelJOb.cancel()
    }

}