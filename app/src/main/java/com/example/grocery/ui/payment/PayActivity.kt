package com.example.grocery.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grocery.databinding.ActivityPayBinding
import com.example.grocery.other.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PayActivity mohamed"

@AndroidEntryPoint
class PayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayBinding
    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.authenticationRequest()
        lifecycleScope.launchWhenStarted {
            viewModel.auth.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d(TAG, "auth observe :loading ")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "auth observe success: ${response.data.toString()}")
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "auth observe error: ${response.message}")
                    }
                    is Resource.Idle -> TODO()
                }
            }
        }
//        viewModel.orderRegistration(PaymentRequest())

    }
}