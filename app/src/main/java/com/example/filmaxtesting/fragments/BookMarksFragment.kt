package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.filmaxtesting.databinding.FragmentBookMarksBinding


class BookMarksFragment : Fragment() {
    private lateinit var binding:FragmentBookMarksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentBookMarksBinding.inflate(inflater)


        return binding.root
    }

}