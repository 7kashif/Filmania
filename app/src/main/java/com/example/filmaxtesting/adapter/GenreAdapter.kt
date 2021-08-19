package com.example.filmaxtesting.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filmaxtesting.dataClasses.movieDetails.Genre
import com.example.filmaxtesting.databinding.GenreItemBinding

class GenreAdapter: ListAdapter<Genre, GenreAdapter.ItemViewHolder>(diffCallBack) {
    inner class ItemViewHolder(val binding:GenreItemBinding):RecyclerView.ViewHolder(binding.root)

    companion object{
        val diffCallBack=object:DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.id==newItem.id
            }
            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            GenreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentGenre=getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                genreTitle.text=currentGenre.name
            }
        }
    }
}