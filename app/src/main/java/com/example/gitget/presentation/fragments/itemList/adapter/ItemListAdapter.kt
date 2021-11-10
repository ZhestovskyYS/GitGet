package com.example.gitget.presentation.fragments.itemList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitget.data.RepoItem
import com.example.gitget.databinding.ItemFragmentBinding

class ItemListAdapter(private val onItemClicked: (RepoItem) -> Unit) :
    ListAdapter<RepoItem, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemFragmentBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ItemFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepoItem) {
            binding.repositoryName.text = item.repoName
            binding.repositoryUrl.text = item.repoUrl
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RepoItem>() {
            override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
                return oldItem.repoName == newItem.repoName
            }
        }
    }
}