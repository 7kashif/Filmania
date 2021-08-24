package com.example.filmaxtesting

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.filmaxtesting.databinding.ExpandedViewBinding

object Constants {

    const val BASE_URL="https://api.themoviedb.org"
    const val END_POPULAR_MOVIES_URL="/3/movie/popular"
    const val END_TOP_RATED_MOVIES_URL="/3/movie/top_rated"
    const val END_POPULAR_SHOWS_URL="/3/tv/popular"
    const val END_TOP_RATED_SHOWS_URL="/3/tv/top_rated"
    const val END_SEARCH_MOVIES_URL="/3/search/movie"
    const val API_KEY="f5202ed4328c007861005ae7fbc2ad86"
    const val BASE_IMAGE_PATH="https://image.tmdb.org/t/p/w500/"

}


object ViewDialog {
    fun expandImageDialog(activity:Activity?, path:String,inflater:LayoutInflater) {
        val binding=ExpandedViewBinding.inflate(inflater)

        val dialog= Dialog(activity!!,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)

        Glide.with(activity)
            .load(Constants.BASE_IMAGE_PATH + path)
            .transform(RoundedCorners(10))
            .into(binding.expandedImage)

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}