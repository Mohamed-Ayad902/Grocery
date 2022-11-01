package com.example.grocery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.databinding.OrdersItemBinding
import com.example.grocery.models.Order
import com.example.grocery.models.Status
import java.text.DateFormat
import java.util.*

class OrdersAdapter(private val listener: OnOrderClickListener) :
    RecyclerView.Adapter<OrdersAdapter.OrdersVH>() {

    inner class OrdersVH(val binding: OrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrdersVH(OrdersItemBinding.inflate(LayoutInflater.from(parent.context)))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrdersVH, position: Int) {
        holder.binding.apply {
            val order = differ.currentList[position]
            tvOrderDetails.text =
                order.products.size.toString() + " items -> " + order.totalPrice.toString() + "$"
            tvOrderId.text = order.id
            tvOrderLocation.text = order.orderLocation
            when (order.status) {
                Status.PLACED -> tvOrderStatus.text = "Placed"
                Status.CONFIRMED -> tvOrderStatus.text = "Confirmed"
                Status.COMING -> tvOrderStatus.text = "On it's way"
                Status.DELIVERED -> tvOrderStatus.text = "Delivered"
            }
            val time = DateFormat.getDateTimeInstance().format(Date(order.time))
            tvOrderTime.text = time

            holder.itemView.setOnClickListener { listener.onClick(order) }
        }
    }

    interface OnOrderClickListener {
        fun onClick(order: Order)
    }

}