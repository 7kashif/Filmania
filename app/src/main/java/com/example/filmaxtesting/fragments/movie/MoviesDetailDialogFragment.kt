package com.example.filmaxtesting.fragments.movie

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
import com.example.filmaxtesting.adapter.movie.SimilarMoviesAdapter
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.databinding.FragmentDialogMovieDetailBinding
import com.example.filmaxtesting.fragments.people.PersonDetailsDialogFragment
import com.example.filmaxtesting.loadBackDrop
import com.example.filmaxtesting.loadPoster
import com.example.filmaxtesting.roomDatabase.BookMark
import com.example.filmaxtesting.roomDatabase.BookMarkDatabase
import com.example.filmaxtesting.setReadMoreTextView
import com.example.filmaxtesting.viewModel.movieDetails.MovieDetailsViewModel
import com.example.filmaxtesting.viewModel.movieDetails.MovieDetailsViewModelFactory
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModelFactory
import kotlinx.coroutines.launch

class MoviesDetailDialogFragment(private val movieId: Int) : DialogFragment() {
    private lateinit var binding: FragmentDialogMovieDetailBinding
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var posterAdapter: PostersAdapter
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter
    private val videosAdapter = VideosAdapter()


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
        setUpVideosRv()
        setupPostersRv()
        setUpCastRv()
        setUpSimilarMoviesRv()
        loadData()

        videosAdapter.setOnItemClickListener { item ->
            activity?.let {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=${item.key}")
                )
                val title = getString(R.string.select_an_app)
                val chooser = Intent.createChooser(intent, title)
                try {
                    startActivity(chooser)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(it, "No related app found.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        similarMoviesAdapter.setOnItemClickListener { item ->
            activity?.let {
                val dialog = MoviesDetailDialogFragment(item.id)
                dialog.show(it.supportFragmentManager, "Detail Dialog")
            }
        }

        castAdapter.setOnItemClickListener { item ->
            activity?.let {
                val dialog = PersonDetailsDialogFragment(item.id)
                dialog.show(it.supportFragmentManager, "Detail Dialog")
            }
        }

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }


        return binding.root
    }

    private fun setUpSimilarMoviesRv() {
        similarMoviesAdapter = SimilarMoviesAdapter()
        binding.similarMoviesRv.apply {
            adapter = similarMoviesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
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

    private fun setUpVideosRv() {
        binding.videosRv.apply {
            adapter = videosAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setUpViews(item: MovieDetailsResponse) {
        binding.apply {
            title.text=item.title
            releaseDate.text = item.release_date
            status.text = item.status
            val hrs: Int = item.runtime / 60
            val min: Int = item.runtime % 60
            runtime.text = getString(R.string.movieRuntime, hrs.toString(), min.toString())
            rating.text = getString(R.string.set_rating, item.vote_average.toString())
            voteCount.text = item.vote_count.toString()
            setReadMoreTextView(activity, overView, item.overview)

            lifecycleScope.launchWhenCreated {
                if (sharedViewModel.getBookMarkByItemId(item.id) != null) {
                    binding.bookMarkCB.isChecked = true
                    favoriteTv.text = getString(R.string.added_to_favorites)
                }
            }
            saveBookMark(item)
            activity?.let {
                loadPoster(it, item.poster_path, poster)
                loadBackDrop(it, item.backdrop_path, backDrop)
            }
        }
    }

    private fun saveBookMark(item: MovieDetailsResponse) {
        binding.apply {
            bookMarkCB.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    if (isChecked) {
                        val bookMark = BookMark()
                        bookMark.apply {
                            itemId = item.id
                            title = item.title
                            posterPath = item.poster_path
                            voteAverage = item.vote_average
                            mediaType = "movie"
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

    private fun setUpCastAndCrew(item: CreditsResponse) {
        castAdapter.submitList(item.cast)

        val crew = item.crew
        var directorName = " N/A"
        var screenPlay = " N/A"
        crew.forEach {
            if (it.job == "Director")
                directorName = it.name
            if (it.job == "Screenplay")
                screenPlay = it.name
        }

        binding.apply {
            director.text = getString(R.string.director, directorName)
            writer.text = getString(R.string.screenplay, screenPlay)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.movieDetail.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    genreAdapter.submitList(response.body()!!.genres)
                    setUpViews(response.body()!!)
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.posters.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    posterAdapter.submitList(response.body()!!.backdrops)
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
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
                    setUpCastAndCrew(response.body()!!)
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.similarMoviesList.observe(viewLifecycleOwner, {
                if (it.isSuccessful && it.body() != null) {
                    similarMoviesAdapter.submitList(it.body()!!.results)
                    binding.loadingLayout.visibility = View.GONE
                } else {
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }
}   