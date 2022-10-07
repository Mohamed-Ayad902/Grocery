package com.example.grocery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.grocery.databinding.CategoryItemBinding
import com.example.grocery.models.Category

class CategoriesAdapter(private val listener: OnCategoryClickListener) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesVH>() {

    inner class CategoriesVH(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoriesVH(CategoryItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: CategoriesVH, position: Int) {
        holder.binding.apply {
            val category = differ.currentList[position]
            tvName.text = category.name
            Glide.with(holder.itemView).load(category.image).transform(RoundedCorners(10))
                .into(imageView)
            holder.itemView.setOnClickListener {
                listener.onClick(category)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    interface OnCategoryClickListener {
        fun onClick(category: Category)
    }

}