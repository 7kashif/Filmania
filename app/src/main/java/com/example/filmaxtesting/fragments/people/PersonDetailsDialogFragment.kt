package com.example.filmaxtesting.fragments.people

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
import coil.transform.CircleCropTransformation
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.ViewDialog
import com.example.filmaxtesting.adapter.misc.PersonImagesAdapter
import com.example.filmaxtesting.adapter.person.PersonRelatedMediaAdapter
import com.example.filmaxtesting.dataClasses.personDetails.PersonDetailsResponse
import com.example.filmaxtesting.databinding.FragmentDialogPersonDetailBinding
import com.example.filmaxtesting.fragments.movie.MoviesDetailDialogFragment
import com.example.filmaxtesting.fragments.show.ShowDetailsDialogFragment
import com.example.filmaxtesting.setReadMoreTextView
import com.example.filmaxtesting.viewModel.personDetails.PersonDetailsViewModel
import com.example.filmaxtesting.viewModel.personDetails.PersonDetailsViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.internal.trimSubstring

class PersonDetailsDialogFragment(private val personId: Int) : DialogFragment() {
    private lateinit var binding: FragmentDialogPersonDetailBinding
    private lateinit var viewModel: PersonDetailsViewModel
    private lateinit var imagesAdapter: PersonImagesAdapter
    private lateinit var relatedMediaAdapter: PersonRelatedMediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogPersonDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, PersonDetailsViewModelFactory(personId)).get(
            PersonDetailsViewModel::class.java
        )

        setImagesRv()
        setRelatedMediaRv()
        loadData()
        setClickListeners(inflater)

        return binding.root
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.personDetails.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getImages()
                    setUpViews(response.body()!!)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()

            })
            viewModel.images.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    viewModel.getRelatedMedia()
                    imagesAdapter.submitList(response.body()!!.profiles)
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })
            viewModel.relatedMedia.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful && response.body() != null) {
                    relatedMediaAdapter.submitList(response.body()!!.cast)
                    binding.loadingLayout.visibility = View.GONE
                } else
                    Toast.makeText(activity, "Response Failed", Toast.LENGTH_SHORT).show()
            })

        }
    }

    private fun setUpViews(item: PersonDetailsResponse) {
        binding.apply {
            profilePic.load(Constants.BASE_IMAGE_PATH + item.profile_path) {
                transformations(CircleCropTransformation())
            }
            name.text = item.name
            field.text = item.known_for_department

            item.birthday?.let {
                val bday = it.trimSubstring(0, 4).toInt()
                age.text = (2021 - bday).toString()
                birthday.text = it
            }
            if (item.birthday == null) {
                age.text = "-"
                birthday.text = "-"
            }
            if (item.place_of_birth != null)
                from.text = item.place_of_birth
            else
                from.text = "-"

            setReadMoreTextView(activity,biography,item.biography)
        }
    }

    private fun setImagesRv() {
        imagesAdapter = PersonImagesAdapter()
        binding.imagesRv.apply {
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setRelatedMediaRv() {
        relatedMediaAdapter = PersonRelatedMediaAdapter()
        binding.famousMoviesShows.apply {
            adapter = relatedMediaAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setClickListeners(inflater: LayoutInflater) {
        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        imagesAdapter.setOnItemClickListener { item ->
            ViewDialog.expandImageDialog(activity, item.file_path, inflater)
        }

        relatedMediaAdapter.setOnItemClickListener { item ->
            activity?.let { activity ->
                if (item.media_type == "movie") {
                    val dialog = MoviesDetailDialogFragment(item.id)
                    dialog.show(activity.supportFragmentManager, "MovieDetailsDialog")
                } else {
                    val dialog = ShowDetailsDialogFragment(item.id)
                    dialog.show(activity.supportFragmentManager, "MovieDetailsDialog")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

}
