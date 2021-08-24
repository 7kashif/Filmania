package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.adapter.movie.MoviesAdapter
import com.example.filmaxtesting.databinding.FragmentTopRatedMoviesBinding
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.viewModel.TopRatedMoviesViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TopRatedMoviesFragment : Fragment() {
    private lateinit var binding: FragmentTopRatedMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private val pagingViewModel: TopRatedMoviesViewModel by activityViewModels()
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTopRatedMoviesBinding.inflate(inflater)
        val application= requireNotNull(this.activity).application
        val dataBase= BookMarkDatabase.getInstance(application).bookMarkDatabaseDao
        sharedViewModel= ViewModelProvider(this
            , SharedViewModelFactory(dataBase,application)
        )
            .get(SharedViewModel::class.java)

        setUpRv()
        loadData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
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
        lifecycleScope.launch{
            pagingViewModel.topRatedMoviesList.flowOn(Dispatchers.Default).collect {
                moviesAdapter.submitData(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

}