package com.example.filmaxtesting.fragments.show

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
import com.example.filmaxtesting.adapter.misc.CastAdapter
import com.example.filmaxtesting.adapter.misc.GenreAdapter
import com.example.filmaxtesting.adapter.misc.PostersAdapter
import com.example.filmaxtesting.adapter.shows.SimilarShowsAdapter
import com.example.filmaxtesting.dataClasses.showsDetails.ShowDetailsResponse
import com.example.filmaxtesting.databinding.FragmentDialogShowDetailsBinding
import com.example.filmaxtesting.fragments.PersonDetailsDialogFragment
import com.example.filmaxtesting.viewModel.showDetails.ShowDetailsViewModel
import com.example.filmaxtesting.viewModel.showDetails.ShowDetailsViewModelFactory
import kotlinx.coroutines.launch

class ShowDetailsDialogFragment(private val showId: Int) : DialogFragment() {
    private lateinit var binding: FragmentDialogShowDetailsBinding
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var posterAdapter: PostersAdapter
    private lateinit var viewModel: ShowDetailsViewModel
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarShowsAdapter: SimilarShowsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogShowDetailsBinding.inflate(inflater)
        viewModel = ViewModelProvider(
            this,
            ShowDetailsViewModelFactory(showId)
        ).get(ShowDetailsViewModel::class.java)

        setupGenreRv()
        setupPostersRv()
        setUpCastRv()
        setUpSimilarShowsRv()
        loadData()

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        castAdapter.setOnItemClickListener {item->
            activity?.let {
                val dialog = PersonDetailsDialogFragment(item.id)
                dialog.show(it.supportFragmentManager, "Detail Dialog")
            }
        }

        similarShowsAdapter.setOnItemClickListener {item->
            activity?.let {
                val dialog=ShowDetailsDialogFragment(item.id)
                dialog.show(it.supportFragmentManager,"ShowsDetailFragment")
            }
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

    private fun setUpSimilarShowsRv() {
        similarShowsAdapter = SimilarShowsAdapter()
        binding.similarShowsRv.apply {
            adapter = similarShowsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setUpGeneralViews(item: ShowDetailsResponse) {
        binding.apply {
            title.text = item.name
            releaseDate.text = item.first_air_date
            status.text = item.status
            rating.text = getString(R.string.set_rating, item.vote_average.toString())
            voteCount.text = item.vote_count.toString()
            overView.text = item.overview
            firstAirDate.text = getString(R.string.firstAirDate, item.first_air_date)
            lastAirDate.text = getString(R.string.lastAirDate, item.last_air_date)
            seasonCount.text = getString(R.string.seasonCount, item.number_of_seasons)
            episodeCount.text = getString(R.string.episodeCount, item.number_of_episodes)
            runtime.text = getString(R.string.episodeRunTime, item.episode_run_time[0])

            genreAdapter.submitList(item.genres)

            loadImagesInViews(item)
        }
    }


    private fun loadImagesInViews(item: ShowDetailsResponse) {
        activity?.let {

            if (item.poster_path != null) {
                Glide.with(it)
                    .load(Constants.BASE_IMAGE_PATH + item.poster_path)
                    .transform(RoundedCorners(10))
                    .into(binding.poster)
            } else {
                Glide.with(it)
                    .load(R.drawable.no_image)
                    .transform(RoundedCorners(10))
                    .into(binding.poster)
            }

            item.backdrop_path?.let { path ->
                binding.backDrop.load(Constants.BASE_IMAGE_PATH + path) {
                    crossfade(true)
                    transformations(BlurTransformation(it, 25.0F, 15.0F))
                }
            }
        }

    }

    private fun loadData() {

        lifecycleScope.launch {

            viewModel.showDetails.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getPosters()
                    setUpGeneralViews(response.body()!!)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

            viewModel.posters.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getShowCredits()
                    posterAdapter.submitList(response.body()!!.backdrops)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

            viewModel.credits.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getSimilarShows()
                    castAdapter.submitList(response.body()!!.cast)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

            viewModel.similarShows.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getShowCredits()
                    similarShowsAdapter.submitList(response.body()!!.results)
                    binding.loadingLayout.visibility = View.GONE
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

}