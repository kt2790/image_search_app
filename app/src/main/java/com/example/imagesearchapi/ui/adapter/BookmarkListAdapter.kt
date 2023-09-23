package com.example.imagesearchapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapi.R
import com.example.imagesearchapi.databinding.BookmarkItemBinding
import com.example.imagesearchapi.model.PresModel

class BookmarkListAdapter (private val deleteBookmark : (PresModel) -> Unit) : RecyclerView.Adapter<BookmarkListAdapter.ViewHolder>() {

    private var imageList = mutableListOf<PresModel>()

    class ViewHolder(val binding: BookmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search: PresModel) {
            Glide.with(binding.root.context)
                .load(search.image_url)
                .placeholder(binding.root.resources.getIdentifier("symbol_questionmark", "drawable", binding.root.context.packageName))
                .into(binding.bookmarkImageview)
            binding.bookmarkItemTitle.text = search.sitename
            binding.bookmarkItemDate.text = search.datetime
        }
    }

    fun setList(imageList: MutableList<PresModel>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            binding.bookmarkImageview.setOnLongClickListener {
                deleteBookmark(imageList[adapterPosition])
                Toast.makeText(binding.root.context, binding.root.context.getString(R.string.BOOKMARK_DELETE_TEXT), Toast.LENGTH_SHORT).show()
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