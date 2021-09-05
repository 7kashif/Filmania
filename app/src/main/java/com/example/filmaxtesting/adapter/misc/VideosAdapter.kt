package com.example.filmaxtesting.adapter.misc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.dataClasses.credits.Cast
import com.example.filmaxtesting.dataClasses.video.Video
import com.example.filmaxtesting.databinding.VideoListItemBinding

class VideosAdapter : ListAdapter<Video, VideosAdapter.VideoItemView>(diffCallBack) {

    inner class VideoItemView(val binding: VideoListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemView {
        return VideoItemView(
            VideoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoItemView, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                Glide.with(this)
                    .load("https://i.ytimg.com/vi/${item.key}/0.jpg")
                    .into(thumbnail)
                title.text = item.name

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    //setting up click listener on recyclerview items
    private var onItemClickListener: ((Video) -> Unit)? = null
    fun setOnItemClickListener(listener: (Video) -> Unit) {
        onItemClickListener = listener
    }
}