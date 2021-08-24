package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filmaxtesting.databinding.FragmentSearchBinding
import com.example.filmaxtesting.viewModel.SearchViewModel


class SearchFragment: Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val viewModel:SearchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchBinding.inflate(inflater)





        return binding.root
    }

}