package com.example.filmaxtesting.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.databinding.FragmentMoviesDetailBinding


class MoviesDetailFragment : Fragment() {
    private lateinit var binding:FragmentMoviesDetailBinding
    private val args:MoviesDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMoviesDetailBinding.inflate(inflater)

        val movieItem=args.movie
        binding.apply {
            movie=movieItem
            ratingTV.text=getString(R.string.set_rating,movieItem.vote_average.toString())
            titleTV.text=getString(R.string.set_title,movieItem.title)
            overViewTV.text=getString(R.string.set_overview,movieItem.overview)
            releaseDateTV.text=getString(R.string.set_release_date,movieItem.release_date)
            originalLanguageTV.text=getString(R.string.set_language, movieItem.original_language)

        }

        val posterPath= Constants.BASE_IMAGE_PATH + movieItem.poster_path
        Glide.with(this).load(posterPath).into(binding.posterIV)

        return binding.root
    }
}