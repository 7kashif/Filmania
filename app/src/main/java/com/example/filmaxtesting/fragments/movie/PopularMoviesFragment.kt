package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.movie.MoviesAdapter
import com.example.filmaxtesting.dataClasses.detail.ItemDetails
import com.example.filmaxtesting.databinding.FragmentPopularMoviesBinding
import com.example.filmaxtesting.viewModel.PagingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class PopularMoviesFragment : Fragment() {
    private lateinit var binding: FragmentPopularMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private val viewModel: PagingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPopularMoviesBinding.inflate(inflater)

        setUpRv()
        loadData()

        moviesAdapter.setOnItemClickListener {
            val item=ItemDetails(
                title=it.title,
                voteAverage = it.vote_average,
                overView = it.overview,
                releaseDate = it.release_date,
                language = it.original_language,
                posterPath = it.poster_path
            )
            val bundle=Bundle().apply {
                putParcelable("item",item)
            }
            viewModel.lastFragment="movie"
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment,bundle)
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
            viewModel.popularMoviesList.collect {
                moviesAdapter.submitData(it)
            }
        }
    }

}