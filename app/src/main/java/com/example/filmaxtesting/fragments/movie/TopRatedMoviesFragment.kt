package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.movie.MoviesAdapter
import com.example.filmaxtesting.databinding.FragmentTopRatedMoviesBinding
import com.example.filmaxtesting.viewModel.MovieViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TopRatedMoviesFragment : Fragment() {
    private lateinit var binding: FragmentTopRatedMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private val viewModel: MovieViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTopRatedMoviesBinding.inflate(inflater)

        setUpRv()
        loadData()

        moviesAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putParcelable("movie",it)
            }
            findNavController().navigate(R.id.action_mainFragment_to_moviesDetailFragment,bundle)
        }

        return binding.root
    }

    private fun setUpRv() {
        moviesAdapter= MoviesAdapter()
        binding.rv.apply {
            adapter=moviesAdapter
            layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch{
            viewModel.topRatedMoviesList.collect {
                moviesAdapter.submitData(it)
            }
        }
    }
}