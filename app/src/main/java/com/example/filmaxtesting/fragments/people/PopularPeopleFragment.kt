package com.example.filmaxtesting.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmaxtesting.adapter.person.PopularPeoplePagingAdapter
import com.example.filmaxtesting.databinding.FragmentPopularPeopleBinding
import com.example.filmaxtesting.viewModel.PopularPeopleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
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

        setupRv()
        loadData()

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
        binding.popularPeopleRv.apply {
            adapter = popularPeopleAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.popularPeopleList.flowOn(Dispatchers.IO).collect {
                popularPeopleAdapter.submitData(it)
            }
        }
    }

}