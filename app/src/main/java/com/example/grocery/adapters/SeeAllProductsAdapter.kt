package com.example.grocery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.databinding.HotAndNewItemBinding
import com.example.grocery.models.Product

class SeeAllProductsAdapter(private val listener: OnProductClickListener) :
    RecyclerView.Adapter<SeeAllProductsAdapter.ProductVH>() {

    inner class ProductVH(val binding: HotAndNewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductVH(HotAndNewItemBinding.inflate(LayoutInflater.from(parent.context)))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SeeAllProductsAdapter.ProductVH, position: Int) {
        holder.binding.apply {
            val product = differ.currentList[position]
            Glide.with(holder.itemView).load(product.image).into(imageView)
            tvPrice.text = product.price.toString() + "$"
            tvName.text = product.name
            tvBrand.text = product.brand

            holder.itemView.setOnClickListener { listener.onClick(product) }
            btnAddToCart.setOnClickListener { listener.onAddToCart(product) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    interface OnProductClickListener {
        fun onClick(product: Product)
        fun onAddToCart(product: Product)
    }

}