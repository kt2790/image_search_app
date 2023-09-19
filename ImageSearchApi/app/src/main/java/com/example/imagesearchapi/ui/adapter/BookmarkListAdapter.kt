package com.example.imagesearchapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapi.databinding.BookmarkItemBinding
import com.example.imagesearchapi.model.KakaoImage

class BookmarkListAdapter (private val deleteBookmark : (KakaoImage) -> Unit) : RecyclerView.Adapter<BookmarkListAdapter.ViewHolder>() {

    private var imageList = mutableListOf<KakaoImage>()

    class ViewHolder(val binding: BookmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search: KakaoImage) {
            Glide.with(binding.root.context).load(search.image_url).into(binding.bookmarkImageview)
            binding.bookmarkItemTitle.text = search.sitename
            binding.bookmarkItemDate.text = search.datetime
        }
    }

    fun setList(imageList: MutableList<KakaoImage>) {
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