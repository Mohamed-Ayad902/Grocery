package com.example.grocery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.databinding.HotAndNewItemBinding
import com.example.grocery.models.HotAndNew

class HotAndNewAdapter(
    private val listener: OnHotItemClickListener
) : RecyclerView.Adapter<HotAndNewAdapter.HotAndNewVH>() {

    inner class HotAndNewVH(val binding: HotAndNewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<HotAndNew>() {
        override fun areItemsTheSame(oldItem: HotAndNew, newItem: HotAndNew) =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: HotAndNew, newItem: HotAndNew) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HotAndNewVH(HotAndNewItemBinding.inflate(LayoutInflater.from(parent.context)))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HotAndNewVH, position: Int) {
        holder.binding.apply {
            val hot = differ.currentList[position]
            Glide.with(holder.itemView).load(hot.image).into(imageView)
            tvPrice.text = hot.price.toString() + "$"
            tvName.text = hot.name
            tvBrand.text = hot.brand

            holder.itemView.setOnClickListener { listener.onClick(hot) }
            btnAddToCart.setOnClickListener { listener.onAddToCart(hot) }
        }
    }

    interface OnHotItemClickListener {
        fun onClick(hotAndNew: HotAndNew)
        fun onAddToCart(hotAndNew: HotAndNew)
    }

}