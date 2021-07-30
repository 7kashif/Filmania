package com.example.filmaxtesting.fragments.show

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
import com.example.filmaxtesting.adapter.TvShowsAdapter
import com.example.filmaxtesting.dataClasses.detail.ItemDetails
import com.example.filmaxtesting.databinding.FragmentTopRatedShowsBinding
import com.example.filmaxtesting.viewModel.PagingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TopRatedShowsFragment : Fragment() {
    private lateinit var binding:FragmentTopRatedShowsBinding
    private lateinit var showsAdapter: TvShowsAdapter
    private val viewModel : PagingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTopRatedShowsBinding.inflate(inflater)

        setUpRv()
        loadData()

        showsAdapter.setOnItemClickListener {
            val item= ItemDetails(
                title=it.name,
                voteAverage = it.vote_average,
                overView = it.overview,
                releaseDate = it.first_air_date,
                language = it.original_language,
                posterPath = it.poster_path
            )
            val bundle=Bundle().apply {
                putParcelable("item",item)
            }
            viewModel.lastFragment="shows"
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment,bundle)
        }

        return binding.root
    }

    private fun setUpRv() {
        showsAdapter= TvShowsAdapter()
        binding.rv.apply {
            adapter=showsAdapter
            layoutManager= GridLayoutManager(activity,3)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch{
            viewModel.topRatedShowsList.collect {
                showsAdapter.submitData(it)
            }
        }
    }

}