package com.example.filmaxtesting.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmaxtesting.adapter.person.PopularPeoplePagingAdapter
import com.example.filmaxtesting.databinding.FragmentPopularPeopleBinding
import com.example.filmaxtesting.isNetworkAvailable
import com.example.filmaxtesting.viewModel.PopularPeopleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PopularPeopleFragment : Fragment() {
    private lateinit var binding: FragmentPopularPeopleBinding
    private lateinit var popularPeopleAdapter: PopularPeoplePagingAdapter
    private val viewModel: PopularPeopleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularPeopleBinding.inflate(inflater)
        viewModel.getPopularPeople()
        setupRv()

        if(isNetworkAvailable(requireActivity())) {
            binding.dataLayout.visibility = View.VISIBLE
            loadData()
            lifecycleScope.launch {
                popularPeopleAdapter.loadStateFlow.map {
                    it.refresh
                }.distinctUntilChanged()
                    .collect {
                        if (it is LoadState.Loading) {
                            binding.linearLayout.visibility = View.VISIBLE
                        }  else
                            binding.linearLayout.visibility = View.GONE
                    }
            }
        }
        else {
            binding.noNetwork.visibility = View.VISIBLE
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }

        popularPeopleAdapter.setOnItemClickListener { item ->
            activity?.let {
                val dialog = PersonDetailsDialogFragment(item.id)
                dialog.show(it.supportFragmentManager, "PersonDetailDialog")
            }
        }

        return binding.root
    }

    private fun setupRv() {
        popularPeopleAdapter = PopularPeoplePagingAdapter()
        binding.rv.apply {
            adapter = popularPeopleAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.peopleList?.flowOn(Dispatchers.IO)?.collect {
                popularPeopleAdapter.submitData(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.viewModelJob.cancel()
    }

}