package com.example.filmaxtesting.adapter.misc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.relatedImages.Backdrop
import com.example.filmaxtesting.databinding.PostersListItemBinding

class PostersAdapter:ListAdapter<Backdrop, PostersAdapter.PosterItemHolder>(diffCallBack) {

    inner class PosterItemHolder(val binding:PostersListItemBinding):RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallBack=object : DiffUtil.ItemCallback<Backdrop>() {
            override fun areItemsTheSame(oldItem: Backdrop, newItem: Backdrop): Boolean {
                return oldItem.file_path==newItem.file_path
            }
            override fun areContentsTheSame(oldItem: Backdrop, newItem: Backdrop): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterItemHolder {
        return PosterItemHolder(
            PostersListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PosterItemHolder, position: Int) {
        val item=getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                Glide.with(this)
                    .load(Constants.BASE_IMAGE_PATH + item.file_path)
                    .into(posterItem)
            }
        }
    }
}