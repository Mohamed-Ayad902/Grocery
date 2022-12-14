package com.example.grocery.ui.base.details

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.grocery.R
import com.example.grocery.databinding.FragmentDetailsBinding
import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    val laptop by lazy { args.laptop }
    val product by lazy { args.product }
    lateinit var category: String

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        laptop?.let {
            category = it.category
            initLaptopViews(it)
        }
        product?.let {
            category = it.category
            initProductViews(it)
        }

        binding.apply {
            btnPlus.setOnClickListener { viewModel.increase() }
            btnMinus.setOnClickListener { viewModel.decrease() }
            ivFavorite.setOnClickListener { viewModel.favoriteClicked(laptop, product, category) }
            btnAddToCart.setOnClickListener { addToCart() }
        }

        subscribeToObservers()
    }

    private fun addToCart() {
        laptop?.let {
            viewModel.addToCart(
                Cart(
                    0,
                    it.name,
                    it.image,
                    it.id,
                    it.brand,
                    it.price,
                    viewModel.quantity.value
                )
            )
        }
        product?.let {
            viewModel.addToCart(
                Cart(
                    0,
                    it.name,
                    it.image,
                    it.id,
                    it.brand,
                    it.price,
                    viewModel.quantity.value
                )
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.quantity.collectLatest(viewLifecycleOwner) {
            binding.productQuantityEditText.text = it.toString()
        }

        viewModel.addToCart.collectLatest(viewLifecycleOwner) {
            if (it == "added")
                showToast("product added to cart")
        }

        laptop?.let {
            viewModel.isLaptopSaved(it.id).collectLatest(viewLifecycleOwner) { laptop ->
                if (laptop == null)
                    binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                else
                    binding.ivFavorite.setImageResource(R.drawable.ic_favorite_yes)

            }
        }
        product?.let {
            viewModel.isProductSaved(it.id).collectLatest(viewLifecycleOwner) { product ->
                if (product == null)
                    binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                else
                    binding.ivFavorite.setImageResource(R.drawable.ic_favorite_yes)

            }
        }
    }

    private fun initLaptopViews(laptop: Laptop) {
        binding.apply {
            tvProcessor.text = laptop.processor
            tvProcessorSpeed.text = laptop.cpuSpeed.toString() + "-GHZ"
            tvName.text = laptop.name
            tvBrand.text = laptop.brand
            tvDescription.text = laptop.description
            productPriceTextView.text = laptop.price.toString() + "$"
            Glide.with(requireContext()).load(laptop.image).into(imageView)

            val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fast_fade_in)
            val fallDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in)

            lifecycleScope.launch {
                binding.imageView.startAnimation(fallDown)
                delay(200)
                binding.apply {
                    tvName.visibility = View.VISIBLE
                    tvName.startAnimation(fadeIn)
                    tvBrand.visibility = View.VISIBLE
                    tvBrand.startAnimation(fadeIn)
                    tvDescription.visibility = View.VISIBLE
                    tvDescription.startAnimation(fadeIn)
                    tvProcessor.visibility = View.VISIBLE
                    tvProcessor.startAnimation(fadeIn)
                    tvProcessorSpeed.visibility = View.VISIBLE
                    tvProcessorSpeed.startAnimation(fadeIn)
                }
            }

            ramLayout.tvTitle.text = "Ram"
            ramLayout.tvValue.text = laptop.ram.toString()
            ramLayout.progressBar.max = 32

            hddLayout.tvTitle.text = "HDD"
            hddLayout.tvValue.text = laptop.hdd.toString()
            hddLayout.progressBar.max = 1000

            ssdLayout.tvTitle.text = "SSD"
            ssdLayout.tvValue.text = laptop.ssd.toString()
            ssdLayout.progressBar.max = 1000

            cameraLayout.tvTitle.text = "Camera"
            cameraLayout.tvValue.text = laptop.camera.toString()
            cameraLayout.progressBar.max = 1080

            ValueAnimator.ofInt(0, laptop.ram).apply {
                duration = 900
                addUpdateListener {
                    binding.ramLayout.progressBar.progress = it.animatedValue.toString().toInt()
                }
            }.start()

            ValueAnimator.ofInt(0, laptop.hdd).apply {
                duration = 1500
                addUpdateListener {
                    binding.hddLayout.progressBar.progress = it.animatedValue.toString().toInt()
                }
            }.start()

            ValueAnimator.ofInt(0, laptop.ssd).apply {
                duration = 1500
                addUpdateListener {
                    binding.ssdLayout.progressBar.progress = it.animatedValue.toString().toInt()
                }
            }.start()

            ValueAnimator.ofInt(0, laptop.camera).apply {
                duration = 1500
                addUpdateListener {
                    binding.cameraLayout.progressBar.progress = it.animatedValue.toString().toInt()
                }
            }.start()
        }
    }

    private fun initProductViews(product: Product) {
        binding.apply {
            tvName.text = product.name
            tvBrand.text = product.brand
            tvDescription.text = product.description
            productPriceTextView.text = product.price.toString() + "$"
            Glide.with(requireContext()).load(product.image).into(imageView)

            val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fast_fade_in)
            val fallDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in)

            lifecycleScope.launch {
                binding.imageView.startAnimation(fallDown)
                delay(200)
                binding.apply {
                    tvName.visibility = View.VISIBLE
                    tvName.startAnimation(fadeIn)
                    tvBrand.visibility = View.VISIBLE
                    tvBrand.startAnimation(fadeIn)
                    tvDescription.visibility = View.VISIBLE
                    tvDescription.startAnimation(fadeIn)
                }
            }

            linearLayout.visibility = View.GONE
        }
    }

}