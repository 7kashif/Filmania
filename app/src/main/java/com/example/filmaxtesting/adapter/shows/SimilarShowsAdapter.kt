package com.example.filmaxtesting.adapter.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.dataClasses.tvShows.TvShows
import com.example.filmaxtesting.databinding.SimilarListItemBinding

class SimilarShowsAdapter:ListAdapter<TvShows,SimilarShowsAdapter.ItemViewHolder>(diffCallback) {

    inner class ItemViewHolder(val binding: SimilarListItemBinding) :RecyclerView.ViewHolder(binding.root)

    companion object{
        val diffCallback =object :DiffUtil.ItemCallback<TvShows> () {
            override fun areItemsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem.id==newItem.id
            }
            override fun areContentsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            SimilarListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentShow=getItem(position)!!

        holder.binding.apply {
            holder.itemView.apply {
                listItemTitle.text=currentShow.name
                Glide.with(this).load(Constants.BASE_IMAGE_PATH + currentShow.poster_path).into(listItemPoster)
                val rating=currentShow.vote_average
                voteCount.text="$rating"
                voteCount.background=when(rating) {
                    in 8.0..10.0 -> ResourcesCompat.getDrawable(resources, R.drawable.green_bg,null)
                    in 5.0..7.9 -> ResourcesCompat.getDrawable(resources, R.drawable.yellow_bg,null)
                    else -> ResourcesCompat.getDrawable(resources, R.drawable.red_bg,null)
                }

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(currentShow)
                    }
                }
            }
        }
    }

    //setting up click listener on recyclerview items
    private var onItemClickListener: ((TvShows) -> Unit)? = null
    fun setOnItemClickListener(listener: (TvShows) -> Unit) {
        onItemClickListener = listener
    }
}