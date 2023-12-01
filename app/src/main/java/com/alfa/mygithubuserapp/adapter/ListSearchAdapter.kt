package com.alfa.mygithubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfa.mygithubuserapp.databinding.ItemRowUserBinding
import com.alfa.mygithubuserapp.response.ItemsSearch
import com.bumptech.glide.Glide

class ListSearchAdapter(private val listUser: ArrayList<ItemsSearch>) :
    RecyclerView.Adapter<ListSearchAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatar) = listUser[position]
        holder.binding.tvItemUsername.text = username
        Glide.with(holder.binding.imgItemPhoto.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    fun updateData(newList: List<ItemsSearch>) {
        listUser.clear()
        listUser.addAll(newList)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsSearch)
    }
}
