package com.example.filmaxtesting.fragments.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.shows.TvShowsAdapter
import com.example.filmaxtesting.databinding.FragmentPagingBinding
import com.example.filmaxtesting.viewModel.show.TopRatedShowsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TopRatedShowsFragment : Fragment() {
    private lateinit var binding:FragmentPagingBinding
    private lateinit var showsAdapter: TvShowsAdapter
    private val pagingViewModel : TopRatedShowsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPagingBinding.inflate(inflater)
        pagingViewModel.getShows()
        binding.title.text = getString(R.string.top_rated_shows)

        setUpRv()
        loadData()

        lifecycleScope.launch {
            showsAdapter.loadStateFlow.map {
                it.refresh
            }.distinctUntilChanged()
                .collect {
                    if (it is LoadState.Loading) {
                        binding.linearLayout.visibility = View.VISIBLE
                    }  else
                        binding.linearLayout.visibility = View.GONE
                }
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        showsAdapter.setOnItemClickListener {item->
            activity?.let {
                val dialog = ShowDetailsDialogFragment(item.id)
                dialog.show(it.supportFragmentManager,"ShowDetailsDialog")
            }
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
        lifecycleScope.launch(Dispatchers.IO){
            pagingViewModel.showsList?.flowOn(Dispatchers.IO)?.collect {
                showsAdapter.submitData(it)
            }
        }
    }

}