package com.example.filmaxtesting.fragments.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.adapter.shows.TvShowsAdapter
import com.example.filmaxtesting.databinding.FragmentTopRatedShowsBinding
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.viewModel.TopRatedShowsViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TopRatedShowsFragment : Fragment() {
    private lateinit var binding:FragmentTopRatedShowsBinding
    private lateinit var showsAdapter: TvShowsAdapter
    private val pagingViewModel : TopRatedShowsViewModel by activityViewModels()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTopRatedShowsBinding.inflate(inflater)
        val application= requireNotNull(this.activity).application
        val dataBase= BookMarkDatabase.getInstance(application).bookMarkDatabaseDao
        sharedViewModel= ViewModelProvider(this
            , SharedViewModelFactory(dataBase,application))
            .get(SharedViewModel::class.java)

        setUpRv()
        loadData()

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
        lifecycleScope.launch{
            pagingViewModel.topRatedShowsList.flowOn(Dispatchers.IO).collect {
                showsAdapter.submitData(it)
            }
        }
    }

}