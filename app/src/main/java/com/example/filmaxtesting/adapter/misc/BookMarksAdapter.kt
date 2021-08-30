package com.example.filmaxtesting.adapter.misc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.databinding.ListItemBinding
import com.example.filmaxtesting.roomDatabase.BookMark

class BookMarksAdapter:ListAdapter<BookMark, BookMarksAdapter.ImageViewHolder>(diffCallBack) {

    inner class ImageViewHolder(val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem=getItem(position)!!

        holder.binding.apply {
            holder.itemView.apply {
                listItemTitle.text=currentItem.title
                val posterPath= Constants.BASE_IMAGE_PATH + currentItem.posterPath
                Glide.with(this).load(posterPath).into(listItemPoster)
                val rating=currentItem.voteAverage
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

    companion object{
        private val diffCallBack=object : DiffUtil.ItemCallback<BookMark>() {
            override fun areItemsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
                return oldItem.bookMarkId==newItem.bookMarkId
            }
            override fun areContentsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
                return oldItem==newItem
            }
        }
    }

    //setting up click listener on recyclerview items
    private var onItemClickListener: ((BookMark) -> Unit)? = null
    fun setOnItemClickListener(listener: (BookMark) -> Unit) {
        onItemClickListener = listener
    }
}