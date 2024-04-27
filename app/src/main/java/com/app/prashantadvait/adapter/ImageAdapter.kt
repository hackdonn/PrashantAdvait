package com.app.prashantadvait.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.prashantadvait.R
import com.app.prashantadvait.databinding.ItemImageBinding
import com.app.prashantadvait.image.ImageLoader
import com.app.prashantadvait.model.Thumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageAdapter(
    private val thumbnails: List<Thumbnail>,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(thumbnail: Thumbnail) {
            val imageUrl = "${thumbnail.domain}/${thumbnail.basePath}/0/${thumbnail.key}"
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = imageLoader.loadImage(imageUrl)
                if (bitmap != null) {
                    binding.thumbnailImage.setImageBitmap(bitmap)
                } else {
                    binding.thumbnailImage.setImageResource(R.drawable.ic_error_placeholder)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(thumbnails[position])
    }

    override fun getItemCount(): Int = thumbnails.size
}