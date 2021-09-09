package com.example.filmaxtesting

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.devs.readmoreoption.ReadMoreOption
import com.example.filmaxtesting.databinding.AboutViewBinding
import com.example.filmaxtesting.databinding.ExpandedViewBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Constants {

    const val BASE_URL = "https://api.themoviedb.org"
    const val END_POPULAR_MOVIES_URL = "/3/movie/popular"
    const val END_TOP_RATED_MOVIES_URL = "/3/movie/top_rated"
    const val END_POPULAR_SHOWS_URL = "/3/tv/popular"
    const val END_TOP_RATED_SHOWS_URL = "/3/tv/top_rated"
    const val END_SEARCH_MOVIES_URL = "/3/search/movie"
    const val API_KEY = "f5202ed4328c007861005ae7fbc2ad86"
    const val BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/w500/"

}

fun isNetworkAvailable(context: Context): Boolean {

    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val capabilities = manager?.getNetworkCapabilities(manager.activeNetwork) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

object ViewDialog {
    fun expandImageDialog(activity: Activity?, path: String, inflater: LayoutInflater) {
        val binding = ExpandedViewBinding.inflate(inflater)

        val dialog = Dialog(activity!!, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        Glide.with(activity)
            .load(Constants.BASE_IMAGE_PATH + path)
            .transform(RoundedCorners(10))
            .into(binding.expandedImage)

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showAbout(activity: Activity?, inflater: LayoutInflater) {
        val binding = AboutViewBinding.inflate(inflater)
        val dialog = Dialog(activity!!,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog.apply {
            setContentView(binding.root)
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}

fun setReadMoreTextView(activity: Activity?, view: TextView, message: String) {
    val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(activity)
        .textLength(4, ReadMoreOption.TYPE_LINE)
        .moreLabel("read more")
        .lessLabel("read less")
        .moreLabelColor(Color.RED)
        .lessLabelColor(Color.YELLOW)
        .labelUnderLine(true)
        .expandAnimation(true)
        .build()

    readMoreOption.addReadMoreTo(view,message)
}

fun loadPoster(activity: Activity, posterPath:String?, view:ImageView) {
    if (posterPath != null) {
        Glide.with(activity)
            .load(Constants.BASE_IMAGE_PATH + posterPath)
            .transform(RoundedCorners(10))
            .into(view)
    } else {
        Glide.with(activity)
            .load(R.drawable.no_image)
            .transform(RoundedCorners(10))
            .into(view)
    }
}

fun loadBackDrop(activity: Activity,backDropPath:String?, view:ImageView) {
    backDropPath?.let { path ->
        view.load(Constants.BASE_IMAGE_PATH + path) {
            crossfade(true)
            transformations(BlurTransformation(activity, 25.0F, 15.0F))
        }
    }
}

fun androidx.appcompat.widget.SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }
        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })
    return query
}
