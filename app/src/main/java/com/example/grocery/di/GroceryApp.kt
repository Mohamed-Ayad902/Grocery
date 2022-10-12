package com.example.grocery.di

import android.app.Application
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GroceryApp : Application() {

    /* secret key
    sk_test_51LqJhBHZvi3oDrtQzP82AiHzSvZT6gH1nho8LcoEJriB38p96kvLRlpWewykdTKT27PfMmNS2xBn35klyqHM7IaS00PWXm8aqK
     */

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51LqJhBHZvi3oDrtQEXG0xrvRJPfRxdkJZ4zwUc8Zhl9FHjCR84NS0VU566aBR5Zevsy2aMevgp7DOnvVLzCN1bwA00J5SZmpTb"
        )
    }

//    override fun onTrimMemory(level: Int) {
//        super.onTrimMemory(level)
//        onTrimMemory(level)
//    }
}