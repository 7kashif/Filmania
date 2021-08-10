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
import com.example.filmaxtesting.ViewDialog
import com.example.filmaxtesting.adapter.shows.TvShowsAdapter
import com.example.filmaxtesting.dataClasses.detail.ItemDetails
import com.example.filmaxtesting.databinding.FragmentPopularShowsBinding
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.viewModel.PopularShowsViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PopularShowsFragment : Fragment() {
    private lateinit var binding:FragmentPopularShowsBinding
    private lateinit var showsAdapter: TvShowsAdapter
    private val pagingViewModel : PopularShowsViewModel by activityViewModels()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPopularShowsBinding.inflate(inflater)
        val application= requireNotNull(this.activity).application
        val dataBase= BookMarkDatabase.getInstance(application).bookMarkDatabaseDao
        sharedViewModel= ViewModelProvider(this
            , SharedViewModelFactory(dataBase,application))
            .get(SharedViewModel::class.java)

        setUpRv()
        loadData()

        showsAdapter.setOnItemClickListener {
            val item= ItemDetails(
                title=it.name,
                voteAverage = it.vote_average,
                overView = it.overview,
                releaseDate = it.first_air_date,
                language = it.original_language,
                posterPath = it.poster_path,
                backDropPath = it.backdrop_path
            )
            ViewDialog.showDetailDialog(activity,item,sharedViewModel)
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
            pagingViewModel.popularShowsList.flowOn(Dispatchers.IO).collect {
                showsAdapter.submitData(it)
            }
        }
    }

}