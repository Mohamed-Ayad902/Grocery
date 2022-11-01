package com.example.grocery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.grocery.databinding.CartItemBinding
import com.example.grocery.models.Cart

class CartAdapter(private val listener: OnCartClickListener) :
    RecyclerView.Adapter<CartAdapter.CartVH>() {

    inner class CartVH(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart) =
            oldItem.itemId == newItem.itemId

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CartVH(CartItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: CartVH, position: Int) {
        holder.binding.apply {
            val cart = differ.currentList[position]
            tvName.text = cart.productName
            productQuantityEditText.text = cart.productQuantity.toString()
            tvBrand.text = cart.productBrand
            tvTotalPrice.text = (cart.productPrice * cart.productQuantity).toString()
            Glide.with(holder.itemView).load(cart.productImage).transform(RoundedCorners(10))
                .into(imageView)
            btnPlus.setOnClickListener { listener.onPlus(cart) }
            btnMinus.setOnClickListener { listener.onMinus(cart) }
            btnDelete.setOnClickListener { listener.onDelete(cart) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    interface OnCartClickListener {
        fun onPlus(cart: Cart)
        fun onMinus(cart: Cart)
        fun onDelete(cart: Cart)
    }

}