package com.example.filmaxtesting.adapter.person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.dataClasses.personDetails.personRelatedMovieAndShows.PersonCast
import com.example.filmaxtesting.databinding.SimilarListItemBinding

class PersonRelatedMediaAdapter :
    ListAdapter<PersonCast, PersonRelatedMediaAdapter.ItemViewHolder>(diffCallBack) {

    inner class ItemViewHolder(val binding: SimilarListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<PersonCast>() {
            override fun areItemsTheSame(oldItem: PersonCast, newItem: PersonCast): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PersonCast, newItem: PersonCast): Boolean {
                return oldItem == newItem
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
        val currentItem = getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                if (currentItem.media_type == "movie")
                    listItemTitle.text = currentItem.title
                else
                    listItemTitle.text = currentItem.name

                if (currentItem.poster_path != null)
                    Glide.with(this).load(Constants.BASE_IMAGE_PATH + currentItem.poster_path)
                        .into(listItemPoster)
                else
                    Glide.with(this).load(R.drawable.no_image).into(listItemPoster)

                val rating = currentItem.vote_average
                voteCount.text = "$rating"
                voteCount.background = when (rating) {
                    in 8.0..10.0 -> ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.green_bg,
                        null
                    )
                    in 5.0..7.9 -> ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.yellow_bg,
                        null
                    )
                    else -> ResourcesCompat.getDrawable(resources, R.drawable.red_bg, null)
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
    private var onItemClickListener: ((PersonCast) -> Unit)? = null
    fun setOnItemClickListener(listener: (PersonCast) -> Unit) {
        onItemClickListener = listener
    }
}