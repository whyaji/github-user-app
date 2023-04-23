package com.whyaji.githubuser.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whyaji.githubuser.data.response.FollowResponseItem
import com.whyaji.githubuser.databinding.ItemUserBinding

class FollowAdapter : ListAdapter<FollowResponseItem, FollowAdapter.ViewHolder>(diffCallback) {

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FollowResponseItem) {
            binding.apply {
                tvUsername.text = item.login
                Glide.with(itemView)
                    .load(item.avatarUrl)
                    .into(ivUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<FollowResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowResponseItem, newItem: FollowResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FollowResponseItem, newItem: FollowResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setItems(newItems: ArrayList<FollowResponseItem>) {
        submitList(newItems)
    }
}