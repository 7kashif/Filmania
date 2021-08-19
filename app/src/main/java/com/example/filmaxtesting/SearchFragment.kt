package com.example.filmaxtesting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.adapter.search.SearchMoviesAdapter
import com.example.filmaxtesting.databinding.FragmentSearchBinding
import com.example.filmaxtesting.fragments.DialogDetailFragment
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.viewModel.SearchViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val viewModel:SearchViewModel by viewModels()
    private lateinit var moviesAdapter: SearchMoviesAdapter
    private var keepRvEmpty:Boolean=true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchBinding.inflate(inflater)
        val application= requireNotNull(this.activity).application
        val dataBase= BookMarkDatabase.getInstance(application).bookMarkDatabaseDao
        val sharedViewModel= ViewModelProvider(this
            , SharedViewModelFactory(dataBase,application))
            .get(SharedViewModel::class.java)

        setupRv()

       lifecycleScope.launch {
           binding.searchTv.getQueryTextChangeStateFlow()
               .debounce(300)
               .filter { query->
                   if(query.isEmpty()) {
                       moviesAdapter.submitList(emptyList())
                       return@filter false
                   } else
                       return@filter true
               }
               .distinctUntilChanged()
               .flatMapConcat { query ->
                   dataFromNetwork(query)
                       .catch {
                           emitAll(flowOf(""))
                       }
               }
               .flowOn(Dispatchers.Default)
               .collect{
                   keepRvEmpty=false
                   searchMovies(it)
               }
       }

        viewModel.moviesResponse.observe(viewLifecycleOwner,{
            if(it.isSuccessful && it.body()!=null && !keepRvEmpty){
                binding.progressbar.visibility=View.GONE
                binding.searchRv.isVisible=true
                moviesAdapter.submitList(it.body()!!.results)
            }
        })

        moviesAdapter.setOnItemClickListener {
            val dialog= DialogDetailFragment(it.id)
            activity?.supportFragmentManager?.let { fragmentManager ->
                dialog.show(fragmentManager,"DetailsDialog")
            }
        }

        return binding.root
    }


    private fun searchMovies(value:String) {
        binding.progressbar.visibility=View.VISIBLE
        viewModel.searchMovies(value)
    }

    private fun setupRv(){
        moviesAdapter= SearchMoviesAdapter()
        binding.searchRv.apply {
            adapter=moviesAdapter
            layoutManager=GridLayoutManager(activity,3)
            setHasFixedSize(true)
        }
    }

    private fun dataFromNetwork(query:String) : Flow<String> {
        return flow {
            delay(1000)
            emit(query)
        }
    }

    override fun onPause() {
        super.onPause()
        moviesAdapter.submitList(emptyList())
        keepRvEmpty=true
    }
}