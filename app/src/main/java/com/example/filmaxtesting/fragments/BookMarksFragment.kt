package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.misc.BookMarksAdapter
import com.example.filmaxtesting.databinding.FragmentBookMarksBinding
import com.example.filmaxtesting.fragments.movie.MoviesDetailDialogFragment
import com.example.filmaxtesting.fragments.show.ShowDetailsDialogFragment
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory


class BookMarksFragment : Fragment() {
    private lateinit var binding:FragmentBookMarksBinding
    private lateinit var  bookMarkAdapter: BookMarksAdapter
    private lateinit var sharedViewModel:SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentBookMarksBinding.inflate(inflater)
        val application= requireNotNull(this.activity).application
        val dataBase=BookMarkDatabase.getInstance(application).bookMarkDatabaseDao
        sharedViewModel=ViewModelProvider(this
            ,SharedViewModelFactory(dataBase,application))
            .get(SharedViewModel::class.java)

        setUpRv()

        bookMarkAdapter.setOnItemClickListener {item->
            activity?.let {activity->
                when(item.mediaType) {
                    "movie" -> {
                        val dialog = MoviesDetailDialogFragment(item.itemId)
                        dialog.show(activity.supportFragmentManager,"MovieDetailsFragment")
                    }
                    "show" -> {
                        val dialog = ShowDetailsDialogFragment(item.itemId)
                        dialog.show(activity.supportFragmentManager,"MovieDetailsFragment")
                    }
                }
            }
        }

        sharedViewModel.allBookMarks.observe(viewLifecycleOwner,{
            bookMarkAdapter.submitList(it)
            if(it.isNotEmpty()) {
                binding.favoriteText.visibility = View.GONE
                binding.bookMarkIcon.visibility = View.GONE
            } else {
                binding.bookMarkIcon.visibility = View.VISIBLE
                binding.favoriteText.visibility = View.VISIBLE
            }
        })

        binding.popUpMenu.setOnClickListener {
            showMenu(it)
        }

        return binding.root
    }

    private fun setUpRv(){
        bookMarkAdapter= BookMarksAdapter()
        binding.bookMarkRv.apply{
            adapter=bookMarkAdapter
            layoutManager=GridLayoutManager(activity,3)
            setHasFixedSize(true)
        }
    }

    private fun showMenu(v:View) {
        PopupMenu(activity,v).apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.clearAllBookmarks -> sharedViewModel.onClear()
                }
                true
            }
            inflate(R.menu.bookmark_clear_menu)
            show()
        }
    }
}