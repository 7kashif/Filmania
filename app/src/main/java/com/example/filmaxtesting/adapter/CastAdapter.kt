package com.example.filmaxtesting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.credits.Cast
import com.example.filmaxtesting.databinding.CastListItemBinding

class CastAdapter:ListAdapter<Cast,CastAdapter.CastItemViewHolder>(diffCallBack) {

    inner class CastItemViewHolder(val binding:CastListItemBinding):RecyclerView.ViewHolder(binding.root)

    companion object{
        val diffCallBack=object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem.id==newItem.id
            }
            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastItemViewHolder {
        return CastItemViewHolder(
            CastListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastItemViewHolder, position: Int) {
        val item=getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                Glide.with(context)
                    .load(Constants.BASE_IMAGE_PATH + item.profile_path)
                    .into(profile)

                actorName.text=item.name
                character.text=item.character
            }
        }
    }
}