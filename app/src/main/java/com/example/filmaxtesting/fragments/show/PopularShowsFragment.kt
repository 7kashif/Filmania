package com.example.filmaxtesting.fragments.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.filmaxtesting.adapter.TvShowsAdapter
import com.example.filmaxtesting.databinding.FragmentPopularShowsBinding
import com.example.filmaxtesting.viewModel.TvShowsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PopularShowsFragment : Fragment() {
    private lateinit var binding:FragmentPopularShowsBinding
    private lateinit var showsAdapter: TvShowsAdapter
    private val viewModel : TvShowsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPopularShowsBinding.inflate(inflater)

        setUpRv()
        loadData()

        return binding.root
    }

    private fun setUpRv() {
        showsAdapter= TvShowsAdapter()
        binding.rv.apply {
            adapter=showsAdapter
            layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch{
            viewModel.popularShowsList.collect {
                showsAdapter.submitData(it)
            }
        }
    }

}