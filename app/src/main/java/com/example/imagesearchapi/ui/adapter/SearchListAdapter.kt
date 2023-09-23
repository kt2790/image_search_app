package com.example.imagesearchapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapi.R
import com.example.imagesearchapi.databinding.SearchItemBinding
import com.example.imagesearchapi.model.PresModel

class SearchListAdapter (private val addToBookmarkList : (PresModel) -> Unit) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    private var imageList = mutableListOf<PresModel>()

    class ViewHolder (val binding : SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search: PresModel) {
            Glide.with(binding.root.context)
                .load(search.image_url)
                .placeholder(binding.root.resources.getIdentifier("symbol_questionmark", "drawable", binding.root.context.packageName))
                .into(binding.imageView)
            binding.searchItemTitle.text = search.sitename
            binding.searchItemDate.text = search.datetime
        }
    }

    fun setList(imageList : MutableList<PresModel>) {
        this.imageList = imageList
        notifyItemRangeChanged(0, this.imageList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            binding.imageView.setOnLongClickListener {
                addToBookmarkList(imageList[adapterPosition])
                Toast.makeText(binding.root.context, binding.root.context.getString(R.string.BOOKMARK_ADD_TEXT), Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }
}