package com.example.grocery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.grocery.databinding.FavoriteItemBinding
import com.example.grocery.models.Product

class FavoriteProductAdapter(private val listener: OnFavoriteProductClickListener) :
    RecyclerView.Adapter<FavoriteProductAdapter.FavoriteVH>() {

    inner class FavoriteVH(val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoriteVH(FavoriteItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {
        holder.binding.apply {
            val product = differ.currentList[position]
            tvName.text = product.name
            tvBrand.text = product.brand
            tvDescription.text = product.description
            Glide.with(holder.itemView).load(product.image).transform(RoundedCorners(10))
                .into(imageView)
            ivFavorite.setOnClickListener { listener.onSavedClick(product) }
            holder.itemView.setOnClickListener { listener.onItemClick(product) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    interface OnFavoriteProductClickListener {
        fun onSavedClick(product: Product)
        fun onItemClick(product: Product)
    }

}