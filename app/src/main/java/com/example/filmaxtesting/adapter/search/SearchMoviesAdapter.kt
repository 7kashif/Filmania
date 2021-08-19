package com.example.filmaxtesting.adapter.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.dataClasses.movies.Movies
import com.example.filmaxtesting.databinding.ListItemBinding

class SearchMoviesAdapter: ListAdapter<Movies, SearchMoviesAdapter.ImageViewHolder>(diffCallBack) {

    inner class ImageViewHolder(val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root)

    companion object{
        val diffCallBack=object : DiffUtil.ItemCallback<Movies>() {
            override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem==newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem=getItem(position)!!

        holder.binding.apply {
            holder.itemView.apply {
                listItemTitle.text=currentItem.title
                val posterPath= Constants.BASE_IMAGE_PATH + currentItem.poster_path
                Glide.with(this).load(posterPath).into(listItemPoster)
                val rating=currentItem.vote_average
                voteCount.text="$rating"
                voteCount.background=when(rating) {
                    in 8.0..10.0 -> ResourcesCompat.getDrawable(resources, R.drawable.green_bg,null)
                    in 5.0..7.9 -> ResourcesCompat.getDrawable(resources, R.drawable.yellow_bg,null)
                    else -> ResourcesCompat.getDrawable(resources, R.drawable.red_bg,null)
                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(currentItem)
                    }
                }
            }
        }
    }

    //setting up click listener on recyclerview items
    private var onItemClickListener: ((Movies) -> Unit)? = null
    fun setOnItemClickListener(listener: (Movies) -> Unit) {
        onItemClickListener = listener
    }
}