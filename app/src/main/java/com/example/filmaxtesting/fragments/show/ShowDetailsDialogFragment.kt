package com.example.filmaxtesting.fragments.show

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmaxtesting.R
import com.example.filmaxtesting.adapter.misc.CastAdapter
import com.example.filmaxtesting.adapter.misc.GenreAdapter
import com.example.filmaxtesting.adapter.misc.PostersAdapter
import com.example.filmaxtesting.adapter.misc.VideosAdapter
import com.example.filmaxtesting.adapter.shows.SimilarShowsAdapter
import com.example.filmaxtesting.dataClasses.showsDetails.ShowDetailsResponse
import com.example.filmaxtesting.databinding.FragmentDialogShowDetailsBinding
import com.example.filmaxtesting.fragments.people.PersonDetailsDialogFragment
import com.example.filmaxtesting.loadBackDrop
import com.example.filmaxtesting.loadPoster
import com.example.filmaxtesting.roomDatabase.BookMark
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.setReadMoreTextView
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import com.example.filmaxtesting.viewModel.showDetails.ShowDetailsViewModel
import com.example.filmaxtesting.viewModel.showDetails.ShowDetailsViewModelFactory
import com.google.android.youtube.player.internal.v
import kotlinx.coroutines.launch

class ShowDetailsDialogFragment(private val showId: Int) : DialogFragment() {
    private lateinit var binding: FragmentDialogShowDetailsBinding
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var posterAdapter: PostersAdapter
    private lateinit var viewModel: ShowDetailsViewModel
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarShowsAdapter: SimilarShowsAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private val videosAdapter = VideosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogShowDetailsBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataBase = BookMarkDatabase.getInstance(application).bookMarkDatabaseDao


        sharedViewModel = ViewModelProvider(
            this, SharedViewModelFactory(dataBase, application)
        )
            .get(SharedViewModel::class.java)

        viewModel = ViewModelProvider(
            this,
            ShowDetailsViewModelFactory(showId)
        ).get(ShowDetailsViewModel::class.java)

        setUpVideosRv()
        setupGenreRv()
        setupPostersRv()
        setUpCastRv()
        setUpSimilarShowsRv()
        loadData()
        setClickListeners()


        return binding.root
    }

    private fun setClickListeners() {
        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        activity?.let { activity ->
            castAdapter.setOnItemClickListener { item ->
                val dialog = PersonDetailsDialogFragment(item.id)
                dialog.show(activity.supportFragmentManager, "Detail Dialog")
            }

            similarShowsAdapter.setOnItemClickListener { item ->
                val dialog = ShowDetailsDialogFragment(item.id)
                dialog.show(activity.supportFragmentManager, "ShowsDetailFragment")
            }

            videosAdapter.setOnItemClickListener { item ->
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=${item.key}")
                )
                val title = getString(R.string.select_an_app)
                val chooser = Intent.createChooser(intent, title)

                try {
                    startActivity(chooser)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(activity, "No related app found.", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setupGenreRv() {
        genreAdapter = GenreAdapter()
        binding.genreRv.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun setUpVideosRv() {
        binding.videosRv.apply {
            adapter = videosAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
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
            title.text=item.name
            releaseDate.text = item.first_air_date
            status.text = item.status
            voteCount.text = item.vote_count.toString()
            rating.text = getString(R.string.set_rating, item.vote_average.toString())
            firstAirDate.text = getString(R.string.firstAirDate, item.first_air_date)
            lastAirDate.text = getString(R.string.lastAirDate, item.last_air_date)
            seasonCount.text = getString(R.string.seasonCount, item.number_of_seasons)
            episodeCount.text = getString(R.string.episodeCount, item.number_of_episodes)
            if (item.episode_run_time.isNotEmpty())
                runtime.text = getString(R.string.episodeRunTime, item.episode_run_time[0])
            else
                runtime.text = getString(R.string.episodeRunTime, 0)

            saveBookMark(item)
            setReadMoreTextView(activity, overView, item.overview)
            genreAdapter.submitList(item.genres)
            loadImagesInViews(item)
        }
    }

    private fun loadImagesInViews(item: ShowDetailsResponse) {
        activity?.let {
            loadPoster(it, item.poster_path, binding.poster)
            loadBackDrop(it, item.backdrop_path, binding.backDrop)
        }
    }

    private fun saveBookMark(item: ShowDetailsResponse) {
        binding.apply {
            lifecycleScope.launchWhenCreated {
                if (sharedViewModel.getBookMarkByItemId(item.id) != null) {
                    bookMarkCB.isChecked = true
                    favoriteTv.text = getString(R.string.added_to_favorites)
                }
            }
            bookMarkCB.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    if (isChecked) {
                        val bookMark = BookMark()
                        bookMark.apply {
                            itemId = item.id
                            title = item.name
                            posterPath = item.poster_path
                            voteAverage = item.vote_average
                            mediaType = "show"
                        }
                        sharedViewModel.createBookMark(bookMark)
                        favoriteTv.text = getString(R.string.added_to_favorites)
                    } else {
                        sharedViewModel.deleteSingleTask(item.id)
                        favoriteTv.text = getString(R.string.add_to_favorites)
                    }
                }
            }
        }
    }

    private fun loadData() {

        lifecycleScope.launch {

            viewModel.showDetails.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    setUpGeneralViews(response.body()!!)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })


            viewModel.posters.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    posterAdapter.submitList(response.body()!!.backdrops)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

            viewModel.videosList.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.results.isEmpty())
                        binding.noVideosTv.visibility = View.VISIBLE
                    videosAdapter.submitList(response.body()!!.results)
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.credits.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    castAdapter.submitList(response.body()!!.cast)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

            viewModel.similarShows.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
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