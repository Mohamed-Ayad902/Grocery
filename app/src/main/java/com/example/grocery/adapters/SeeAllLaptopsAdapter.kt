package com.example.grocery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.databinding.HotAndNewItemBinding
import com.example.grocery.models.Laptop

class SeeAllLaptopsAdapter(private val listener: OnLaptopClickListener) :
    RecyclerView.Adapter<SeeAllLaptopsAdapter.LaptopVH>() {

    inner class LaptopVH(val binding: HotAndNewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Laptop>() {
        override fun areItemsTheSame(oldItem: Laptop, newItem: Laptop) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Laptop, newItem: Laptop) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LaptopVH(HotAndNewItemBinding.inflate(LayoutInflater.from(parent.context)))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SeeAllLaptopsAdapter.LaptopVH, position: Int) {
        holder.binding.apply {
            val laptop = differ.currentList[position]
            Glide.with(holder.itemView).load(laptop.image).into(imageView)
            tvPrice.text = laptop.price.toString() + "$"
            tvName.text = laptop.name
            tvBrand.text = laptop.brand

            holder.itemView.setOnClickListener { listener.onClick(laptop) }
            btnAddToCart.setOnClickListener { listener.onAddToCart(laptop) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    interface OnLaptopClickListener {
        fun onClick(laptop: Laptop)
        fun onAddToCart(laptop: Laptop)
    }

}