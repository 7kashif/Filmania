package com.example.filmaxtesting.adapter.search


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResults
import com.example.filmaxtesting.databinding.ListItemBinding

class SearchMoviesAdapter: PagingDataAdapter<MultiSearchResults, SearchMoviesAdapter.ImageViewHolder>(diffCallBack) {

    inner class ImageViewHolder(val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root)

    companion object{
        val diffCallBack=object : DiffUtil.ItemCallback<MultiSearchResults>() {
            override fun areItemsTheSame(oldItem: MultiSearchResults, newItem: MultiSearchResults): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: MultiSearchResults, newItem: MultiSearchResults): Boolean {
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

                voteCount.visibility= View.GONE

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(currentItem)
                    }
                }
            }
        }
    }

    //setting up click listener on recyclerview items
    private var onItemClickListener: ((MultiSearchResults) -> Unit)? = null
    fun setOnItemClickListener(listener: (MultiSearchResults) -> Unit) {
        onItemClickListener = listener
    }
}