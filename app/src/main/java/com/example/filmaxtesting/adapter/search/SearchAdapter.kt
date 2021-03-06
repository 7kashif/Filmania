package com.example.filmaxtesting.adapter.search


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.R
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResults
import com.example.filmaxtesting.databinding.ListItemBinding

class SearchAdapter: PagingDataAdapter<MultiSearchResults, SearchAdapter.ImageViewHolder>(diffCallBack) {

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


                when (currentItem.media_type) {
                    "movie" -> {
                        listItemTitle.text = currentItem.title
                        if (currentItem.poster_path != null )
                            Glide.with(this).load(Constants.BASE_IMAGE_PATH + currentItem.poster_path).into(listItemPoster)
                        else
                            Glide.with(this).load(R.drawable.no_image).into(listItemPoster)
                    }
                    "tv" -> {
                        listItemTitle.text = currentItem.name
                        if (currentItem.poster_path != null )
                            Glide.with(this).load(Constants.BASE_IMAGE_PATH + currentItem.poster_path).into(listItemPoster)
                        else
                            Glide.with(this).load(R.drawable.no_image).into(listItemPoster)
                    }
                    "person" -> {
                        voteCount.visibility= View.GONE
                        listItemTitle.text = currentItem.name
                        if (currentItem.profile_path != null )
                            Glide.with(this).load(Constants.BASE_IMAGE_PATH + currentItem.profile_path).into(listItemPoster)
                        else
                            Glide.with(this).load(R.drawable.no_image).into(listItemPoster)
                    }
                }

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
    private var onItemClickListener: ((MultiSearchResults) -> Unit)? = null
    fun setOnItemClickListener(listener: (MultiSearchResults) -> Unit) {
        onItemClickListener = listener
    }
}