package com.guntur.storyapps.view.main.adapter

import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.guntur.storyapps.data.response.ListStoryItem
import com.guntur.storyapps.databinding.ItemViewBinding
import com.guntur.storyapps.view.detail.DetailStoryActivity

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemViewBinding):
        ViewHolder(binding.root){
            fun bind(list : ListStoryItem){
                binding.apply {
                    Glide.with(itemView)
                        .load(list.photoUrl)
                        .into(imageView)
                    namTextView.text = list.name
                    descTextView.text = list.description
                    itemView.setOnClickListener{
                        val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                        intent.putExtra(EXTRA_USER, list.id)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user !== null){
            holder.bind(user)
        }
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}