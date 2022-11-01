package com.example.grocery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.databinding.OrderItemBinding
import com.example.grocery.models.Cart

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderVH>() {

    inner class OrderVH(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart) =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderVH(OrderItemBinding.inflate(LayoutInflater.from(parent.context)))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderVH, position: Int) {
        holder.binding.apply {
            val cart = differ.currentList[position]
            Glide.with(holder.itemView).load(cart.productImage).into(imageView)
            tvPrice.text = cart.productPrice.toString() + "$"
            tvName.text = cart.productName
            tvBrand.text = cart.productBrand
            tvBrand.text = "X${cart.productQuantity}"
        }
    }

}