package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args:DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDetailBinding.inflate(inflater)

        val item=args.item
        binding.apply {
            ratingTV.text=getString(R.string.set_rating,item.voteAverage.toString())
            titleTV.text=getString(R.string.set_title,item.title)
            overViewTV.text=getString(R.string.set_overview,item.overView)
            releaseDateTV.text=getString(R.string.set_release_date,item.releaseDate)
            originalLanguageTV.text=getString(R.string.set_language, item.language)

        }

        val posterPath= Constants.BASE_IMAGE_PATH + item.posterPath
        Glide.with(this).load(posterPath).into(binding.posterIV)

        return binding.root
    }
}