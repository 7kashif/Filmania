package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.adapter.search.SearchAdapter
import com.example.filmaxtesting.databinding.FragmentSearchBinding
import com.example.filmaxtesting.fragments.movie.MoviesDetailDialogFragment
import com.example.filmaxtesting.fragments.people.PersonDetailsDialogFragment
import com.example.filmaxtesting.fragments.show.ShowDetailsDialogFragment
import com.example.filmaxtesting.getQueryTextChangeStateFlow
import com.example.filmaxtesting.viewModel.SearchViewModel
import dagger.internal.SetFactory.empty
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class SearchFragment: Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var searchAdapter : SearchAdapter
    private val viewModel:SearchViewModel by activityViewModels()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchBinding.inflate(inflater)
        setUpRv()
        loadData()
        setUpDebounce()
        setUpAdapterClickListener()

        return binding.root
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.searchResults?.flowOn(Dispatchers.IO)?.collect {
                searchAdapter.submitData(it)
            }
        }
    }

    private fun setUpRv() {
        searchAdapter = SearchAdapter()
        binding.searchRv.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(activity,3)
            setHasFixedSize(true)
        }
    }

    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            delay(500)
            emit(query)
        }
    }

    private fun setUpAdapterClickListener() {
        searchAdapter.setOnItemClickListener {item->
            activity?.let {activity->
                when(item.media_type) {
                    "movie" -> {
                        val dialog = MoviesDetailDialogFragment(item.id)
                        dialog.show(activity.supportFragmentManager,"movieDetailFragment")
                    }
                    "tv" -> {
                        val dialog = ShowDetailsDialogFragment(item.id)
                        dialog.show(activity.supportFragmentManager,"movieDetailFragment")
                    }
                    "person" -> {
                        val dialog = PersonDetailsDialogFragment(item.id)
                        dialog.show(activity.supportFragmentManager,"movieDetailFragment")
                    }
                }
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun setUpDebounce() {
        lifecycleScope.launchWhenCreated {
            binding.searchTv.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if(query.isEmpty()) {
                        searchAdapter.submitData(PagingData.empty())
                        return@filter false
                    } else
                        return@filter true
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emitAll(flowOf(""))
                        }
                }
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    binding.progressbar.visibility = View.VISIBLE
                    viewModel.search(result,binding)
                    loadData()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            searchAdapter.submitData(lifecycle,PagingData.empty())
        }
    }
}