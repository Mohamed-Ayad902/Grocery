package com.example.grocery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.grocery.databinding.FavoriteItemBinding
import com.example.grocery.models.Laptop

class FavoriteLaptopsAdapter(private val listener: OnFavoriteLaptopClickListener) :
    RecyclerView.Adapter<FavoriteLaptopsAdapter.FavoriteVH>() {

    inner class FavoriteVH(val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Laptop>() {
        override fun areItemsTheSame(oldItem: Laptop, newItem: Laptop) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Laptop, newItem: Laptop) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoriteVH(FavoriteItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {
        holder.binding.apply {
            val laptop = differ.currentList[position]
            tvName.text = laptop.name
            tvBrand.text = laptop.brand
            tvDescription.text = laptop.description
            Glide.with(holder.itemView).load(laptop.image).transform(RoundedCorners(10))
                .into(imageView)
            ivFavorite.setOnClickListener { listener.onSavedClick(laptop) }
            holder.itemView.setOnClickListener { listener.onItemClick(laptop) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    interface OnFavoriteLaptopClickListener {
        fun onSavedClick(laptop: Laptop)
        fun onItemClick(laptop: Laptop)
    }

}