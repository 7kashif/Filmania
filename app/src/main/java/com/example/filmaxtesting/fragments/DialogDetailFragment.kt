package com.example.filmaxtesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.CastAdapter
import com.example.filmaxtesting.adapter.GenreAdapter
import com.example.filmaxtesting.adapter.PostersAdapter
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.databinding.FragmentDialogMovieDetailBinding
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.viewModel.movieDetails.MovieDetailsViewModel
import com.example.filmaxtesting.viewModel.movieDetails.MovieDetailsViewModelFactory
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import kotlinx.coroutines.launch

class DialogDetailFragment(private val movieId: Int) : DialogFragment() {
    private lateinit var binding: FragmentDialogMovieDetailBinding
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var posterAdapter: PostersAdapter
    private lateinit var castAdapter: CastAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogMovieDetailBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataBase = BookMarkDatabase.getInstance(application).bookMarkDatabaseDao
        viewModel = ViewModelProvider(
            this,
            MovieDetailsViewModelFactory(movieId)
        ).get(MovieDetailsViewModel::class.java)

        sharedViewModel = ViewModelProvider(
            this, SharedViewModelFactory(dataBase, application)
        )
            .get(SharedViewModel::class.java)

        setupGenreRv()
        setupPostersRv()
        setUpCastRv()

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        lifecycleScope.launch {
            viewModel.movieDetail.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getPosters(movieId)
                    genreAdapter.submitList(response.body()!!.genres)
                    setUpViews(response.body()!!)
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })
            viewModel.posters.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getCredits(movieId)
                    posterAdapter.submitList(response.body()!!.backdrops)
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })
            viewModel.credits.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getCredits(movieId)
                    setUpCastAndCrew(response.body()!!)

                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }

    private fun setupGenreRv() {
        genreAdapter = GenreAdapter()
        binding.genreRv.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun setupPostersRv() {
        posterAdapter = PostersAdapter()
        binding.postersRv.adapter = posterAdapter
    }

    private fun setUpCastRv() {
        castAdapter = CastAdapter()
        binding.castRv.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setUpViews(item: MovieDetailsResponse) {
        binding.apply {
            title.text = item.title
            releaseDate.text = item.release_date
            status.text = item.status
            val hrs: Int = item.runtime / 60
            val min: Int = item.runtime % 60
            runtime.text = "${hrs}h ${min}m"
            rating.text = "${item.vote_average}/10"
            voteCount.text = item.vote_count.toString()
            overView.text = item.overview

            activity?.let {

                Glide.with(it)
                    .load(Constants.BASE_IMAGE_PATH + item.poster_path)
                    .transform(RoundedCorners(8))
                    .into(poster)

                backDrop.load(Constants.BASE_IMAGE_PATH + item.backdrop_path) {
                    crossfade(true)
                    transformations(BlurTransformation(it, 25.0F, 15.0F))
                }

            }
        }
    }

    private fun setUpCastAndCrew(item: CreditsResponse) {
        castAdapter.submitList(item.cast)

        val crew = item.crew
        var directorName = ""
        var screenPlay = ""
        crew.forEach {
            if (it.job == "Director")
                directorName = it.name
            if (it.job == "Screenplay")
                screenPlay = it.name
        }

        binding.apply {
            director.text = "Director : $directorName"
            writer.text = "Writer : $screenPlay"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }
}   