package com.example.grocery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.grocery.databinding.OfferItemBinding
import com.example.grocery.models.Offer
import com.smarteist.autoimageslider.SliderViewAdapter

class OffersAdapter(
    private val listener: OnOfferClickListener,
    private var offersList: List<Offer>
) : SliderViewAdapter<OffersAdapter.OffersVH>() {

    inner class OffersVH(val binding: OfferItemBinding) :
        ViewHolder(binding.root)

    override fun getCount() = offersList.size

    fun submitList(list: List<Offer>) {
        offersList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?) =
        OffersVH(OfferItemBinding.inflate(LayoutInflater.from(parent?.context)))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: OffersVH?, position: Int) {
        viewHolder!!.binding.apply {
            val offer = offersList[position]
            Glide.with(viewHolder.itemView).load(offer.image).into(imageView)
            tvDiscount.text = offer.discount.toString() + "%"
            viewHolder.itemView.setOnClickListener { listener.onClick(offer) }
        }
    }

    interface OnOfferClickListener {
        fun onClick(offer: Offer)
    }

}