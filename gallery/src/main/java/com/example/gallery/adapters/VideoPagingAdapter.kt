package com.example.gallery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.data.model.Video
import com.example.gallery.databinding.VideoItemBinding
import com.example.gallery.extensions.formatDuration
import com.example.gallery.extensions.loadThumb

class VideoPagingAdapter :
    PagingDataAdapter<Video, VideoPagingAdapter.VideoViewHolder>(Comparator) {

    companion object {
        val Comparator = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem == newItem
            }
        }
    }


    class VideoViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            binding.apply {
                duration.text = video.duration.formatDuration()
                thumb.loadThumb(video.filePath)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VideoItemBinding.inflate(inflater, parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        video?.let {
            holder.bind(it)
        }
    }

}