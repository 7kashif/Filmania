package com.example.filmaxtesting.adapter.person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.movies.Movies
import com.example.filmaxtesting.dataClasses.popularPeople.Person
import com.example.filmaxtesting.databinding.PopularPeopleListItemBinding


class PopularPeoplePagingAdapter:PagingDataAdapter<Person,PopularPeoplePagingAdapter.ListItemViewHolder>(diffCallback) {

    inner class ListItemViewHolder(val binding : PopularPeopleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id==newItem.id
            }
            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = getItem(position)!!

        holder.binding.apply {
            holder.itemView.apply {
                name.text = currentItem.name
                profilePic.load(Constants.BASE_IMAGE_PATH + currentItem.profile_path) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(currentItem)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            PopularPeopleListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //setting up click listener on recyclerview items
    private var onItemClickListener: ((Person) -> Unit)? = null
    fun setOnItemClickListener(listener: (Person) -> Unit) {
        onItemClickListener = listener
    }
}